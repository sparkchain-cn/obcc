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
public class AcountBaseTrans {

    public static final Logger logger = LoggerFactory.getLogger(AcountBaseTrans.class);

    private static void checkTransfer(ChainPipe pipe, IChainDriver driver, IAccountHandler accountHandler) throws Exception {
        SrcAccount account = pipe.getSrcAccount();
        String bizId = pipe.getBizId();
        if (StringUtils.isNullOrEmpty(pipe.getDestAddr())) {
            throw ObccException.create(EExceptionCode.PARAMETER_INVALID, "DestAddr is empty.");
        }
        //区块回调的处理
        //  driver.getCallbackRegister().register(bizId, pipe.getCallbackFn());

        //账户的格式 处理
        account.setSrcAddr(JunctionUtils.hexAddrress(account.getSrcAddr()));
        final String destAddr = JunctionUtils.hexAddrress(pipe.getDestAddr());
        // 1 、检测地址和私钥的正确性
        boolean flag = accountHandler.checkAccount(account, pipe.getConfig());
        if (flag == false) {
            throw ObccException.create(EExceptionCode.ACCOUNT_SECRET_NOT_FOLLOW_ADDR,
                    "private key and address can not agree.");
        }
    }

    private static void transferOne(String memo, ChainPipe pipe, List<String> hashs,
                                    IChainDriver driver, BaseAccountHandler accountHandler) {
        ChainPipe p1 = pipe.copy();
        p1.getSrcAccount().setMemos(memo);
        String hash = transferOne(pipe, driver, accountHandler);
        hashs.add(hash);
//
//        try {
//            ChainPipe p1 = pipe.copy();
//            p1.getSrcAccount().setMemos(memo);
//            //2、计算nonce,如果传入的没有，那么就直接计算
//            if (StringUtils.isNullOrEmpty(p1.getSrcAccount().getNonce())) {
//                Long nonce = driver.getNonceCalculator()
//                        .getNonce(p1.getSrcAccount().getSrcAddr(), p1.getConfig());
//                p1.getSrcAccount().setNonce(nonce.toString());
//            }
//            //4、计算 gas
//            accountHandler.calGas(p1.getSrcAccount(), p1.getAmount(), p1.getDestAddr(), p1.getConfig());
//            // 5、transfer
//            String hash = accountHandler.onTransfer(p1);
//            //6、hash deal
//            if (StringUtils.isNotNullOrEmpty(hash)) {
//                // register into state
//                driver.getStateMonitor().setState(hash, new BizState(p1.getBizId(), hash, ETransferStatus.STATE_CHAIN_ACCEPT));
//                //状态回调
//                pipe.getCallbackFn().exec(pipe.getBizId(), hash, pipe.getConfig().getUpchainType(), ETransferStatus.STATE_CHAIN_ACCEPT_HALF, pipe);
//                hashs.add(hash);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


    private static String transferOne(ChainPipe pipe, IChainDriver driver, BaseAccountHandler accountHandler) {
        try {
            ChainPipe p1 = pipe;
            //2、计算nonce,如果传入的没有，那么就直接计算
            if (StringUtils.isNullOrEmpty(p1.getSrcAccount().getNonce())) {
                Long nonce = driver.getNonceCalculator()
                        .getNonce(p1.getSrcAccount().getSrcAddr(), p1.getConfig());
                p1.getSrcAccount().setNonce(nonce.toString());
            }
            //4、计算 gas
            accountHandler.calGas(p1.getSrcAccount(), p1.getAmount(), p1.getDestAddr(), p1.getConfig());
            // 5、transfer
            String hash = accountHandler.onTransfer(p1);
            //6、hash deal
            if (StringUtils.isNotNullOrEmpty(hash)) {
                // register into state
                driver.getStateMonitor().setState(hash, new BizState(p1.getBizId(), hash, ETransferStatus.STATE_CHAIN_ACCEPT));
                //状态回调
                pipe.getCallbackFn().exec(pipe.getBizId(), hash, pipe.getConfig().getUpchainType(), ETransferStatus.STATE_CHAIN_ACCEPT_HALF, pipe);
            }
            return hash;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String multiTransfer(ChainPipe pipe, IChainDriver driver, BaseAccountHandler accountHandler) throws Exception {
        String hashStrs = null;
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
                hashStrs = transferOne(pipe, driver, accountHandler);
            } else {
                //3、转换memo,多条返回多个hash
                List<String> memos = driver.getMemoParser().encode(pipe.getBizId(), pipe.getSrcAccount().getMemos());
                List<String> hashs = new ArrayList<>();
                //每个memo的记录执行
                memos.stream().forEach((memo) -> {
                    transferOne(memo, pipe, hashs, driver, accountHandler);
                });
                //合并
                hashStrs = StringUtils.join(hashs, ",");
            }

            driver.getStateMonitor().setBizState(pipe.getBizId(),
                    new BizState(pipe.getBizId(), hashStrs, ETransferStatus.STATE_CHAIN_ACCEPT));
            //状态回调
            pipe.getCallbackFn().exec(pipe.getBizId(), hashStrs, pipe.getConfig().getUpchainType(), ETransferStatus.STATE_CHAIN_ACCEPT, null);

            return hashStrs;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上链出错", e);
            //todo:分情况：明确的错误
            driver.getCallbackRegister().unRegister(pipe.getBizId());
            //todo:分情况设状态，状态超时写入数据库
            driver.getStateMonitor().setBizState(pipe.getBizId(),
                    new BizState(pipe.getBizId(), hashStrs, ETransferStatus.STATE_CHAIN_DEFINITE_FAILURE));
        }

        return null;
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
