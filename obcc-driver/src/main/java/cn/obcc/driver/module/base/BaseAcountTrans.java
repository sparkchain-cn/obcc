package cn.obcc.driver.module.base;

import cn.obcc.config.ExConfig;
import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.module.IAccountHandler;
import cn.obcc.driver.module.fn.IStateListener;
import cn.obcc.driver.utils.JunctionUtils;
import cn.obcc.driver.vo.ChainPipe;
import cn.obcc.driver.vo.FromAccount;
import cn.obcc.exception.ObccException;
import cn.obcc.exception.enums.EExceptionCode;
import cn.obcc.exception.enums.ETransferState;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.vo.driver.RecordInfo;
import cn.obcc.vo.driver.TxRecv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName MultiTranfer
 * @desc TODO
 * @date 2019/9/7 0007  17:27
 **/
public class BaseAcountTrans {

    public static final Logger logger = LoggerFactory.getLogger(BaseAcountTrans.class);

    private static void checkTransfer(ChainPipe pipe, IChainDriver driver, IAccountHandler accountHandler) throws Exception {

        FromAccount account = pipe.getFromAccount();

        if (StringUtils.isNullOrEmpty(pipe.getDestAddr())) {
            throw ObccException.create(EExceptionCode.PARAMETER_INVALID, "DestAddr is empty.");
        }
        //账户的格式 处理
        account.setSrcAddr(JunctionUtils.hexAddrress(account.getSrcAddr()));
        String destAddr = JunctionUtils.hexAddrress(pipe.getDestAddr());

        //检测地址和私钥的正确性
        boolean flag = accountHandler.checkAccount(account, pipe.getExConfig());

        if (flag == false) {
            throw ObccException.create(EExceptionCode.ACCOUNT_SECRET_NOT_FOLLOW_ADDR,
                    "private key and address can not agree.");
        }
    }

    private static String transferOne(String hexMemo, ChainPipe pipe, IChainDriver driver, BaseAccountHandler accountHandler) throws Exception {

        ChainPipe p1 = pipe.copy();
        //分隔的memo
        p1.getFromAccount().setMemos(hexMemo);
        String hash = transferOne(pipe, driver, accountHandler);
        return hash;
    }


    private static void computeNonce(ChainPipe p1, IChainDriver driver) throws Exception {
        if (StringUtils.isNullOrEmpty(p1.getFromAccount().getNonce())) {
            Long nonce = driver.getNonceCalculator().getNonce(p1.getFromAccount().getSrcAddr(), p1.getExConfig());
            p1.getFromAccount().setNonce(nonce.toString());
        }
    }

    private static void computeGas(ChainPipe p1, IChainDriver driver) throws Exception {
        driver.getAccountHandler().calGas(p1.getFromAccount(), p1.getAmount(), p1.getDestAddr(), p1.getExConfig());
    }

    private static void registerBlockCallback(ChainPipe p1, IChainDriver driver) throws Exception {
        driver.getCallbackRegister().register(p1.getBizState().getBizId(), p1.getStateListener());
    }

    private static void dealException(TxRecv recv, Exception e1) {
        if (e1 instanceof ObccException) {
            recv.setErrorCode(((ObccException) e1).getErrorCode());
            recv.setErrorMsg(((ObccException) e1).getMsg());
        } else {
            recv.setErrorMsg(StringUtils.exception(e1));
            recv.setErrorCode(EExceptionCode.UNDEAL_ERROR + "");
        }
    }


    private static void setHashState(TxRecv recv, ChainPipe p1, IChainDriver driver) throws Exception {
        //bizState是共用的，其hash会变化的，一个hash对应公用的bizState.
        driver.getStateMonitor().setHashState(recv.getHash(), p1.getBizState());
    }

    private static void setBizState(ChainPipe p1, IChainDriver driver) throws Exception {
        //bizState是共用的，其hash会变化的，一个hash对应公用的bizState.
        driver.getStateMonitor().setBizState(p1.getBizState().getBizId(), p1.getBizState());
    }

    private static void dealException(RecordInfo recv, Exception e1) {
        if (e1 instanceof ObccException) {
            recv.setErrorCode(((ObccException) e1).getErrorCode());
            recv.setErrorMsg(((ObccException) e1).getMsg());
        } else {
            recv.setErrorMsg(StringUtils.exception(e1));
            recv.setErrorCode(EExceptionCode.UNDEAL_ERROR + "");
        }
    }

    private static String transferOne(ChainPipe p1, IChainDriver driver, BaseAccountHandler accountHandler) throws Exception {
        TxRecv recv = new TxRecv();
        try {
            //1、计算nonce,如果传入的没有，那么就直接计算
            computeNonce(p1, driver);
            //2、计算 gas
            computeGas(p1, driver);
            //3.区块回调的注册（防重复）
            registerBlockCallback(p1, driver);
            //4、transfer
            recv.setHash(accountHandler.onTransfer(p1));
        } catch (Exception e1) {
            dealException(recv, e1);
        }
        //5、try transfer之后的结果,传给状态回调函数进行处理，在accountHandler中transfer中
        recv = buildTxRecv(p1, recv);
        p1.getRecordInfo().getTxRecvList().add(recv);

        //6、修改状态
        p1.getBizState().setTransferState(ETransferState.STATE_CHAIN_ACCEPT_HALF);
        p1.getBizState().setHashes(recv.getHash());

        //7、注册到Hash State
        //一个bizId,多个hash对应一个共用的bizState,
        setHashState(recv, p1, driver);
        //8、状态回调
        StateCbExcutor.call(p1, p1.getRecordInfo());

        return recv.getHash();

    }


    private static TxRecv buildTxRecv(ChainPipe p1, TxRecv recv) {
        recv.setNonce(p1.getFromAccount().getNonce());
        recv.setGasLimit(p1.getFromAccount().getGasLimit() + "");
        recv.setGasPrice(p1.getFromAccount().getGasPrice() + "");
        return recv;

    }

    private static void encodeOneMemo(ChainPipe pipe, IChainDriver driver) throws Exception {
        if (pipe.getExConfig().isNeedHandleMemo()) {
            pipe.getFromAccount().setMemos(driver.getMemoParser().encodeOne(pipe.getBizState().getBizId(), pipe.getFromAccount().getMemos()));
        }
    }

    private static List<String> encodeManyMemo(ChainPipe pipe, IChainDriver driver) throws Exception {
        // if (pipe.getExConfig().isNeedHandleMemo()) {
        List<String> memos = driver.getMemoParser().encode(pipe.getBizState().getBizId(), pipe.getFromAccount().getMemos());
        return memos;
        //  }
    }

    private static void dealNoSplitTransfer(ChainPipe pipe, IChainDriver driver, BaseAccountHandler accountHandler) throws Exception {
        //一般是需要编码，但是对于合约等不需要，因为已经编码好了
        RecordInfo recordInfo = pipe.getRecordInfo();
        encodeOneMemo(pipe, driver);
        //上链
        String hash = transferOne(pipe, driver, accountHandler);

        recordInfo.setHashes(hash);
        recordInfo.setTxSize(1);
        pipe.getBizState().setHashes(hash);
        pipe.getBizState().setTxSize(1);
    }

    private static void dealSplitTransfer(ChainPipe pipe, IChainDriver driver, BaseAccountHandler accountHandler) throws Exception {
        RecordInfo recordInfo = pipe.getRecordInfo();
        //3、转换memo,多条返回多个hash
        List<String> memos = encodeManyMemo(pipe, driver);
        recordInfo.setTxSize(memos.size());
        pipe.getBizState().setTxSize(memos.size());
        //每个memo的记录执行
        List<String> hashList = new ArrayList<>();
        //todo:改成并行？
        for (String memo : memos) {
            String hash = transferOne(memo, pipe, driver, accountHandler);
            hashList.add(hash);
        }

        String hashes = StringUtils.cat(hashList, ",");
        recordInfo.setHashes(hashes);
        pipe.getBizState().setHashes(hashes);
    }


    public static String multiTransfer(ChainPipe pipe, IChainDriver driver, BaseAccountHandler accountHandler) throws Exception {

        RecordInfo recordInfo = pipe.getRecordInfo();
        try {
            //1、check
            checkTransfer(pipe, driver, accountHandler);
            // 2、上链
            //不需要拆分，设定该参数，性能会高一点
            if (!pipe.getExConfig().isNeedSplit()) {
                dealNoSplitTransfer(pipe, driver, accountHandler);
            } else {
                //拆分多个memo上链
                dealSplitTransfer(pipe, driver, accountHandler);
            }
            //3、注册bizState状态
            pipe.getBizState().setTransferState(ETransferState.STATE_CHAIN_ACCEPT);
            setBizState(pipe, driver);

        } catch (Exception e1) {
            dealException(recordInfo, e1);
        }

        // 4、状态回调
        StateCbExcutor.call(pipe, pipe.getRecordInfo());

        return recordInfo.getHashes();

    }

    public static ChainPipe toChainPipe(String bizId, FromAccount account, String amount,
                                        String destAddr, ExConfig config, IStateListener callback) throws Exception {
        ChainPipe pipe = new ChainPipe();
        pipe.getBizState().setBizId(bizId);
        // pipe.setChainTxType(EChainTxType.Orign);
        pipe.setFromAccount(account);
        pipe.setDestAddr(destAddr);
        pipe.setAmount(amount);
        pipe.setExConfig(config);
        pipe.setStateListener(callback);
        pipe.setContractAddr(null);
        return pipe;
    }
}
