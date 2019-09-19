package cn.obcc.driver.handler.base;

import cn.obcc.config.ExConfig;
import cn.obcc.driver.base.BaseDriverHandler;
import cn.obcc.driver.handler.IAccountHandler;
import cn.obcc.driver.handler.excutor.AccountTxExcutor;
import cn.obcc.driver.handler.excutor.AcountTransExcutor;
import cn.obcc.driver.handler.excutor.RecordExcutor;
import cn.obcc.driver.handler.excutor.StateCbExcutor;
import cn.obcc.vo.driver.Account;
import cn.obcc.vo.driver.BizTxInfo;
import cn.obcc.vo.driver.ChainPipe;
import cn.obcc.vo.driver.FromAccount;
import cn.obcc.enums.EExceptionCode;
import cn.obcc.enums.ETransferState;
import cn.obcc.exception.ObccException;
import cn.obcc.listener.IStateListener;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.uuid.UuidUtils;
import cn.obcc.vo.BizState;
import cn.obcc.vo.db.AccountInfo;
import cn.obcc.vo.db.BlockTxInfo;
import cn.obcc.vo.db.RecordInfo;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName AccountBaseHandler
 * @desc TODO
 * @date 2019/8/25 0025  12:44
 **/
public abstract class BaseAccountHandler<T> extends BaseDriverHandler<T> implements IAccountHandler<T> {
    public static final Logger logger = LoggerFactory.getLogger(BaseAccountHandler.class);


    public abstract String onTransfer(ChainPipe pipe) throws Exception;

    @Override
    public AccountInfo createAccount(String bizId, String username, String pwd, ExConfig config) throws Exception {
        //创建完成调用 db保存
        Account ret = this.createAccount();
        if (ret == null) {
            throw ObccException.create(EExceptionCode.CREATE_ACCOUNT_FAIL, "创建 account error.");
        }
        Account ac = (Account) ret;
        AccountInfo acInfo = new AccountInfo();
        acInfo.setId(UuidUtils.get());
        acInfo.setBizId(bizId);
        acInfo.setUserName(username);
        acInfo.setPassword(pwd);
        acInfo.setAddress(ac.getAddress());
        acInfo.setSecret(ac.getSecret());
        acInfo.setState(0);
        getDriver().getLocalDb().getAccountInfoDao().add(acInfo);
        return acInfo;
    }

    @Override
    public String pay(String bizId, FromAccount account, String amount,
                      String destAddr, IStateListener callback, ExConfig config) throws Exception {
        ChainPipe pipe = AcountTransExcutor.toChainPipe(bizId, account, amount, destAddr, config, callback);
        return transfer(pipe);
    }

    @Override
    public String transfer(@NonNull ChainPipe pipe) throws Exception {

        //判断缓存中存在，数据库中不存在
        driver.getStateMonitor().checkAndSupplyBizId(pipe.getBizState().getBizId());
        //流水表中保存
        RecordInfo ri = RecordExcutor.saveRecord(pipe, driver, config);
        //设定状态
        BizState state = pipe.getBizState();
        state.setChainCode(config.getChain().getName());
        state.setRecId(pipe.getRecordInfo().getId() + "");
        state.setTransferState(ETransferState.STATE_SPC_QUEUE);
        getDriver().getStateMonitor().setBizState(state.getBizId(), state);
        //异步上链
        getDriver().getSpeedAdjuster().offer(pipe);
        //通过状态回调更新流水表
        IStateListener fn = pipe.getStateListener();
        IStateListener listener = (BizState bizState, Object resp) -> {
            if (bizState.getTransferState() == ETransferState.STATE_CHAIN_ACCEPT) {
                RecordInfo r = (RecordInfo) resp;
                RecordExcutor.chainRecvUpdate(r, driver);
            } else if (bizState.getTransferState() == ETransferState.STATE_CHAIN_CONSENSUS) {
                BlockTxInfo txInfo = (BlockTxInfo) resp;
                RecordExcutor.consenseUpdate(bizState,txInfo, driver);
            }
            if (fn != null) {
                fn.exec(bizState, resp);
            }
        };
        pipe.setStateListener(listener);

        //状态回调 ETransferState.STATE_SPC_QUEUE
        StateCbExcutor.call(pipe, null);
        //流水表的ID
        return pipe.getBizState().getRecId();
    }


    @Override
    public String syncTransfer(@NonNull ChainPipe pipe) throws Exception {

        if (pipe.getBizState() == null || StringUtils.isNullOrEmpty(pipe.getBizState().getBizId())) {
            throw ObccException.create(EExceptionCode.PARAMETER_INVALID, "chainpipe中的bizState和其bizId不能为空");
        }
        if (pipe.getRecordInfo() == null || pipe.getRecordInfo().getId() < 1) {
            logger.warn("chainPipe should has recordInfo and the recordInfo has id.");
        }

        //状态回调 ETransferState.STATE_WRITE_CHAIN
        pipe.getBizState().setTransferState(ETransferState.STATE_WRITE_CHAIN);
        StateCbExcutor.call(pipe, null);

        //上链
        String hashes = AcountTransExcutor.multiTransfer(pipe, driver, this);

        return hashes;

    }

    @Override
    public BizTxInfo getTxByBizId(String bizId, ExConfig config) throws Exception {
        return AccountTxExcutor.getTxByBizId(bizId, config, getDriver(), this);
    }

    @Override
    public BizTxInfo getTxByHashes(@NonNull String hashes, ExConfig config) throws Exception {
        return AccountTxExcutor.getTxByHashs(hashes, config, this);
    }


}
