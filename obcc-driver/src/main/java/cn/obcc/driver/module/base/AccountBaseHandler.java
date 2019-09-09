package cn.obcc.driver.module.base;

import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.module.IAccountHandler;
import cn.obcc.driver.module.fn.IUpchainFn;
import cn.obcc.driver.vo.Account;
import cn.obcc.driver.vo.BizTxInfo;
import cn.obcc.driver.vo.ChainPipe;
import cn.obcc.driver.vo.SrcAccount;

import cn.obcc.exception.ObccException;
import cn.obcc.exception.enums.EChainTxType;
import cn.obcc.exception.enums.EExceptionCode;
import cn.obcc.exception.enums.ETransferStatus;
import cn.obcc.exception.enums.EUpchainType;
import cn.obcc.uuid.UuidUtils;
import cn.obcc.vo.BizState;
import cn.obcc.vo.driver.AccountInfo;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.config.ExProps;
import cn.obcc.vo.driver.BlockTxInfo;
import cn.obcc.vo.driver.RecordInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotEmpty;
import java.util.*;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName AccountBaseHandler
 * @desc TODO
 * @date 2019/8/25 0025  12:44
 **/
public abstract class AccountBaseHandler<T> extends BaseHandler<T> implements IAccountHandler<T> {
    public static final Logger logger = LoggerFactory.getLogger(AccountBaseHandler.class);


    public abstract String onTransfer(ChainPipe pipe) throws Exception;

    @Override
    public AccountInfo createAccount(String bizId, String username, String pwd) throws Exception {
        //创建完成调用 db保存
        Account ret = this.createAccount();
        if (ret == null) {
            throw ObccException.create(EExceptionCode.CREATE_ACCOUNT_FAIL, "创建");
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
    public String transfer(String bizId, SrcAccount account, String amount,
                           String destAddr, ExProps config, IUpchainFn<BlockTxInfo> callback) throws Exception {
        ChainPipe pipe = AcountBaseTrans.toChainPipe(getObccConfig().getChain().getName(),
                bizId, account, amount, destAddr, config, callback);
        return transfer(pipe);
    }

    @Override
    public String transfer(ChainPipe pipe) throws Exception {

        getDriver().getSpeedAdjuster().offer(pipe);

        getDriver().getStateMonitor().setBizState(pipe.getBizId(),
                new BizState(pipe.getBizId(), null, ETransferStatus.STATE_SPC_QUEUE));
        //状态回调
        pipe.getCallbackFn().exec(pipe.getBizId(), null,
                pipe.getConfig().getUpchainType(), ETransferStatus.STATE_SPC_QUEUE, null);

        return UuidUtils.get() + "";
    }

    public String doTransfer(ChainPipe pipe) throws Exception {

        getDriver().getStateMonitor().setBizState(pipe.getBizId(), new BizState(pipe.getBizId(), null, ETransferStatus.STATE_WRITE_CHAIN));
        //状态回调
        pipe.getCallbackFn().exec(pipe.getBizId(), null,   pipe.getConfig().getUpchainType(), ETransferStatus.STATE_WRITE_CHAIN, null);

        return AcountBaseTrans.multiTransfer(pipe, getDriver(), this);
    }

    public BizTxInfo getTxByBizId(String bizId, ExProps config) throws Exception {
        return AccountBaseTx.getTxByBizId(bizId, config, getDriver(), this);
    }

    @Override
    public BizTxInfo getTxByHashs(@NotEmpty String hashs, ExProps config) throws Exception {
        return AccountBaseTx.getTxByHashs(hashs, config, this);
    }


}
