package cn.obcc.driver.module.base;

import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.module.IAccountHandler;
import cn.obcc.driver.module.fn.ITransferFn;
import cn.obcc.driver.module.fn.IUpchainFn;
import cn.obcc.driver.utils.JunctionUtils;
import cn.obcc.driver.vo.Account;
import cn.obcc.driver.vo.BizTxInfo;
import cn.obcc.driver.vo.ChainPipe;
import cn.obcc.driver.vo.SrcAccount;

import cn.obcc.exception.ObccException;
import cn.obcc.exception.enums.EExceptionCode;
import cn.obcc.exception.enums.ETransferStatus;
import cn.obcc.exception.enums.EUpchainType;
import cn.obcc.uuid.UuidUtils;
import cn.obcc.vo.driver.AccountInfo;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.config.ReqConfig;
import cn.obcc.vo.RetData;
import cn.obcc.vo.driver.BlockTxInfo;
import cn.obcc.vo.driver.RecordInfo;

import java.math.BigInteger;
import java.util.*;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName AccountBaseHandler
 * @desc TODO
 * @date 2019/8/25 0025  12:44
 **/
public abstract class AccountBaseHandler<T> extends BaseHandler<T> implements IAccountHandler<T> {


    public abstract RetData<String> onTransfer(String bizId, SrcAccount account, String amount,
                                               String destAddr, ReqConfig<T> config, ITransferFn callback) throws Exception;


    @Override
    public RetData<AccountInfo> createAccount(String bizId, String username, String pwd) throws Exception {
        //创建完成调用 db保存
        RetData<Account> ret = this.createAccount();
        if (!ret.isSuccess()) {
            throw ObccException.create(EExceptionCode.CREATE_ACCOUNT_FAIL, ret.getCode() + ret.getMessage());
        }

        Account ac = (Account) ret.getData();
        AccountInfo acInfo = new AccountInfo();
        acInfo.setId(UuidUtils.get());
        acInfo.setBizId(bizId);
        acInfo.setUserName(username);
        acInfo.setPassword(pwd);
        acInfo.setAddress(ac.getAddress());
        acInfo.setSecret(ac.getSecret());
        acInfo.setState(0);
        getDriver().getLocalDb().getAccountInfoDao().add(acInfo);
        return RetData.succuess(ac);
    }

    @Override
    public RetData<String> transfer(String bizId, SrcAccount account, BigInteger amount,
                                    String destAddr, ReqConfig<T> config, ITransferFn callback) throws Exception {
        ChainPipe pipe = new ChainPipe();
        pipe.setBizId(bizId);
        pipe.setAccount(account);
        pipe.setDestAddr(destAddr);
        pipe.setAmount(amount);
        pipe.setConfig(config);
        pipe.setFn(callback);
        getDriver().getSpeedAdjuster().offer(pipe);
        return RetData.succuess(pipe.hashCode());
    }

    public RetData<String> doTransfer(ChainPipe pipe) throws Exception {
        String hashStrs = null;
        SrcAccount account = pipe.getAccount();
        String bizId = pipe.getBizId();
        String amount = pipe.getAmount().toString(10);
        String destAddress = pipe.getDestAddr();
        ITransferFn callback = (ITransferFn) pipe.getFn();
        ReqConfig config = pipe.getConfig();
        try {
            //区块回调的处理
            getDriver().getCallbackRegister().register(bizId, callback);

            //账户的格式 处理
            account.setAccount(JunctionUtils.hexAddrress(account.getAccount()));
            final String destAddr = JunctionUtils.hexAddrress(destAddress);

            // 1 、检测地址和私钥的正确性
            boolean flag = checkAccount(account, config);
            if (flag == false) {
                throw ObccException.create(EExceptionCode.ACCOUNT_SECRET_NOT_FOLLOW_ADDR,
                        "private key and address can not agree.");
            }

            //2、计算nonce,如果传入的没有，那么就直接计算
            if (StringUtils.isNullOrEmpty(account.getNonce())) {
                account.setNonce((String) getDriver().getNonceCalculator()
                        .getNonce(account.getAccount(), config).getData());
            }
            //3、转换memo,多条返回多个hash
            List<String> memos = getDriver().getMemoParser().encode(bizId, account.getMemos());
            List<String> hashs = new ArrayList<>();
            memos.stream().forEach((m) -> {
                try {
                    String hexMemo = JunctionUtils.hexData(m);
                    account.setMemos(hexMemo);
                    //4、计算 gas
                    calGas(account, amount, destAddr, config);
                    // 5、transfer
                    RetData<String> ret = onTransfer(bizId, account, amount, destAddr, config, callback);
                    //6、hash deal
                    if (StringUtils.isNullOrEmpty((String) ret.getData())) {
                        hashs.add(ret.getData() + "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            hashStrs = StringUtils.join(hashs, ",");
            if (callback != null) {
                callback.exec(bizId, hashStrs, EUpchainType.Transfer, ETransferStatus.STATE_CHAIN_RECEIPT, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //todo:分情况：明确的错误
            getDriver().getCallbackRegister().unRegister(bizId);
        }

        return RetData.succuess(hashStrs);
    }


    public RetData<BizTxInfo> getTxByBizId(String bizId, ReqConfig<T> config) throws Exception {
        RecordInfo recordInfo = getDriver().getLocalDb().getRecordInfoDao().findOne("biz_id=?", new Object[]{bizId});
        if (recordInfo == null) {
            return RetData.error("根据BizId:" + bizId + "不能找到对应的hash");
        }
        RetData<BizTxInfo> bizInfos = getTxByHashs(recordInfo.getHashs(), config);
        return bizInfos;
    }


    @Override
    public RetData<BizTxInfo> getTxByHashs(String hashs, ReqConfig<T> config) throws Exception {
        if (StringUtils.isNullOrEmpty(hashs)) return RetData.error("参数Hash is null.");
        List<BlockTxInfo> list = new ArrayList<>();

        BizTxInfo bizTx = new BizTxInfo();
        bizTx.setHashs(hashs);
        StringBuffer sb = new StringBuffer();
        Arrays.stream(hashs.split("[,，]")).forEach((s) -> {
            try {
                RetData<BlockTxInfo> ret = getTxByHash(hashs, config);
                BlockTxInfo tx = (BlockTxInfo) ret.getData();
                sb.append(tx.getMemosObj().getData());
                //todo:判断多个hash的流水的bizid是否相同
                bizTx.setBizId(tx.getMemosObj().getBizId());
                bizTx.setSourceAddress(tx.getSrcAddr());
                bizTx.setDestinationAddress(tx.getDestAddr());
                list.add(tx);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        bizTx.setMemos(sb.toString());
        bizTx.setRecordInfos(list);
        bizTx.setState(1 + "");
        return RetData.succuess(bizTx);
    }


}
