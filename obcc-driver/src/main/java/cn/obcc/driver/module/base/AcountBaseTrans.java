package cn.obcc.driver.module.base;

import cn.obcc.config.ExProps;
import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.module.IAccountHandler;
import cn.obcc.driver.module.fn.IUpchainFn;
import cn.obcc.driver.utils.JunctionUtils;
import cn.obcc.driver.vo.ChainPipe;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.exception.ObccException;
import cn.obcc.exception.enums.EChainTxType;
import cn.obcc.exception.enums.EExceptionCode;
import cn.obcc.exception.enums.ETransferStatus;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.vo.BizState;
import cn.obcc.vo.driver.BlockTxInfo;
import cn.obcc.vo.driver.RecordInfo;
import cn.obcc.vo.driver.TxRecv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName MultiTranfer
 * @desc TODO
 * @date 2019/9/7 0007  17:27
 **/
public class AcountBaseTrans {

    public static final Logger logger = LoggerFactory.getLogger(AcountBaseTrans.class);

    private static void checkTransfer(ChainPipe pipe, IChainDriver driver, IAccountHandler accountHandler) throws Exception {

        SrcAccount account = pipe.getSrcAccount();

        if (StringUtils.isNullOrEmpty(pipe.getDestAddr())) {
            throw ObccException.create(EExceptionCode.PARAMETER_INVALID, "DestAddr is empty.");
        }
        //账户的格式 处理
        account.setSrcAddr(JunctionUtils.hexAddrress(account.getSrcAddr()));
        String destAddr = JunctionUtils.hexAddrress(pipe.getDestAddr());

        // 1 、检测地址和私钥的正确性
        boolean flag = accountHandler.checkAccount(account, pipe.getConfig());

        if (flag == false) {
            throw ObccException.create(EExceptionCode.ACCOUNT_SECRET_NOT_FOLLOW_ADDR,
                    "private key and address can not agree.");
        }
    }

    private static String transferOne(String memo, ChainPipe pipe, IChainDriver driver, BaseAccountHandler accountHandler) throws Exception {
        ChainPipe p1 = pipe.copy();
        p1.getSrcAccount().setMemos(memo);
        String hash = transferOne(pipe, driver, accountHandler);
        //  hashs.add(hash);
        return hash;
    }


    private static String transferOne(ChainPipe p1, IChainDriver driver, BaseAccountHandler accountHandler) throws Exception {
        TxRecv recv = new TxRecv();
        try {
            //1、计算nonce,如果传入的没有，那么就直接计算
            if (StringUtils.isNullOrEmpty(p1.getSrcAccount().getNonce())) {
                Long nonce = driver.getNonceCalculator().getNonce(p1.getSrcAccount().getSrcAddr(), p1.getConfig());
                p1.getSrcAccount().setNonce(nonce.toString());
            }
            //2、计算 gas
            accountHandler.calGas(p1.getSrcAccount(), p1.getAmount(), p1.getDestAddr(), p1.getConfig());
            //3、transfer
            recv.setHash(accountHandler.onTransfer(p1));
        } catch (Exception e1) {
            if (e1 instanceof ObccException) {
                recv.setErrorCode(((ObccException) e1).getErrorCode());
                recv.setErrorMsg(((ObccException) e1).getMsg());
            } else {
                recv.setErrorMsg(e1.getMessage());
                recv.setErrorCode(EExceptionCode.UNDEAL_ERROR + "");
            }
        }
        //4 try transfer之后的结果,传给状态回调函数进行处理，在accountHandler中transfer中
        recv = buildTxRecv(p1, recv);
        p1.getConfig().getRecordInfo().getTxRecvList().add(recv);
        //5、 register into state
        BizStateExcutor.setHashState(driver, p1, recv.getHash(), ETransferStatus.STATE_CHAIN_ACCEPT);
        //6、状态回调
        StateCbExcutor.call(p1, recv.getHash(), ETransferStatus.STATE_CHAIN_ACCEPT_HALF, p1.getConfig().getRecordInfo());
        return recv.getHash();

    }


    private static TxRecv buildTxRecv(ChainPipe p1, TxRecv recv) {
        recv.setNonce(p1.getSrcAccount().getNonce());
        recv.setGasLimit(p1.getSrcAccount().getGasLimit() + "");
        recv.setGasPrice(p1.getSrcAccount().getGasPrice() + "");
        return recv;

    }

    public static String multiTransfer(ChainPipe pipe, IChainDriver driver, BaseAccountHandler accountHandler) throws Exception {

        RecordInfo recordInfo = pipe.getConfig().getRecordInfo();

        try {
            //1.check
            checkTransfer(pipe, driver, accountHandler);
            //2.区块回调的处理
            driver.getCallbackRegister().register(pipe.getBizId(), pipe.getCallbackFn());

            if (!pipe.getConfig().isNeedSplit()) {
                //一般是需要编码，但是对于合约等不需要，因为已经编码好了
                if (pipe.getConfig().isNeedHandleMemo()) {
                    pipe.getSrcAccount().setMemos(driver.getMemoParser().encodeOne(pipe.getBizId(), pipe.getSrcAccount().getMemos()));
                }
                String hash = transferOne(pipe, driver, accountHandler);
                recordInfo.setHashs(hash);
                recordInfo.setTxSize(1);

            } else {
                //3、转换memo,多条返回多个hash
                List<String> memos = driver.getMemoParser().encode(pipe.getBizId(), pipe.getSrcAccount().getMemos());
                recordInfo.setTxSize(memos.size());

                //每个memo的记录执行
                List<String> hashList = new ArrayList<>();
                for (String memo : memos) {
                    hashList.add(transferOne(memo, pipe, driver, accountHandler));
                }
                recordInfo.setHashs(StringUtils.cat(hashList, ","));
            }

            //注册biz State
            BizStateExcutor.setBizState(driver, pipe, recordInfo.getHashs(), ETransferStatus.STATE_CHAIN_ACCEPT);

        } catch (Exception e1) {
            if (e1 instanceof ObccException) {
                recordInfo.setErrorCode(((ObccException) e1).getErrorCode());
                recordInfo.setErrorMsg(((ObccException) e1).getMsg());
            } else {
                recordInfo.setErrorMsg(e1.getMessage());
                recordInfo.setErrorCode(EExceptionCode.UNDEAL_ERROR + "");
            }
        }

        //状态回调
        StateCbExcutor.call(pipe, recordInfo.getHashs(), ETransferStatus.STATE_CHAIN_ACCEPT, pipe.getConfig().getRecordInfo());
        return recordInfo.getHashs();

    }


    public static ChainPipe toChainPipe(String chainCode, String bizId, SrcAccount account, String amount,
                                        String destAddr, ExProps config, IUpchainFn<BlockTxInfo> callback) throws Exception {
        ChainPipe pipe = new ChainPipe();
        pipe.setChainTxType(EChainTxType.Orign);
        pipe.setChainCode(chainCode);
        pipe.setBizId(bizId);
        pipe.setSrcAccount(account);
        pipe.setDestAddr(destAddr);
        pipe.setAmount(amount);
        pipe.setConfig(config);
        pipe.setCallbackFn(callback);
        pipe.setContractAddr(null);
        return pipe;
    }
}
