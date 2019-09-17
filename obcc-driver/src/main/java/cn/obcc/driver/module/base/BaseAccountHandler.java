package cn.obcc.driver.module.base;

import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.module.IAccountHandler;
import cn.obcc.driver.module.fn.IUpchainFn;
import cn.obcc.driver.vo.Account;
import cn.obcc.driver.vo.BizTxInfo;
import cn.obcc.driver.vo.ChainPipe;
import cn.obcc.driver.vo.SrcAccount;

import cn.obcc.exception.ObccException;
import cn.obcc.exception.enums.*;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.uuid.UuidUtils;
import cn.obcc.vo.BizState;
import cn.obcc.vo.driver.*;
import cn.obcc.config.ExProps;
import com.alibaba.fastjson.JSON;
import lombok.NonNull;
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
public abstract class BaseAccountHandler<T> extends BaseHandler<T> implements IAccountHandler<T> {
    public static final Logger logger = LoggerFactory.getLogger(BaseAccountHandler.class);


    protected abstract String onTransfer(ChainPipe pipe) throws Exception;

    @Override
    public AccountInfo createAccount(String bizId, String username, String pwd, ExProps config) throws Exception {
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
    public String transfer(String bizId, SrcAccount account, String amount,
                           String destAddr, ExProps config, IUpchainFn<BlockTxInfo> callback) throws Exception {
        ChainPipe pipe = AcountBaseTrans.toChainPipe(getObccConfig().getChain().getName(),
                bizId, account, amount, destAddr, config, callback);
        return transfer(pipe);
    }

    @Override
    public String transfer(ChainPipe pipe) throws Exception {
        //判断缓存中存在，数据库中不存在
        driver.getStateMonitor().checkAndsupplyBizId(pipe.getBizId());
        RecordExcutor.saveRecord(pipe, driver);
        getDriver().getSpeedAdjuster().offer(pipe);

        getDriver().getStateMonitor().setBizState(pipe.getBizId(),
                new BizState(pipe.getBizId(), null,
                        pipe.getConfig().getRecordInfo().getId() + "", ETransferStatus.STATE_SPC_QUEUE));
        IUpchainFn fn = pipe.getCallbackFn();

        pipe.setCallbackFn(new IUpchainFn() {
            @Override
            public void exec(String bizId, String hash, EUpchainType upchainType, ETransferStatus state, Object resp) throws Exception {
                if (state == ETransferStatus.STATE_CHAIN_ACCEPT) {
                    RecordInfo r = (RecordInfo) resp;
                    RecordExcutor.chainRecvUpdate(r, driver);
                } else if (state == ETransferStatus.STATE_CHAIN_CONSENSUS) {
                    BlockTxInfo txInfo = (BlockTxInfo) resp;
                    RecordExcutor.consenseUpdate(txInfo, driver);
                }
                if (fn != null) {
                    fn.exec(bizId, hash, upchainType, state, resp);
                }
            }
        });

        //状态回调
        pipe.getCallbackFn().exec(pipe.getBizId(), null, pipe.getConfig().
                getUpchainType(), ETransferStatus.STATE_SPC_QUEUE, null);

        //流水表的ID
        return pipe.getConfig().getRecordInfo().getId() + "";
    }


    @Override
    public String transferSync(ChainPipe pipe) throws Exception {
        BizStateExcutor.setBizState(driver, pipe, null, ETransferStatus.STATE_WRITE_CHAIN);
        //状态回调
        StateCbExcutor.call(pipe, null, ETransferStatus.STATE_WRITE_CHAIN, null);
        return AcountBaseTrans.multiTransfer(pipe, getDriver(), this);

    }

    @Override
    public BizTxInfo getTxByBizId(String bizId, ExProps config) throws Exception {
        return AccountBaseTx.getTxByBizId(bizId, config, getDriver(), this);
    }

    @Override
    public BizTxInfo getTxByHashs(@NonNull String hashs, ExProps config) throws Exception {
        return AccountBaseTx.getTxByHashs(hashs, config, this);
    }


}
