package cn.obcc.driver.module.base;

import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.module.IAccountHandler;
import cn.obcc.driver.module.fn.ITransferFn;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.vo.driver.AccountInfo;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.config.ReqConfig;
import cn.obcc.vo.RetData;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName AccountBaseHandler
 * @desc TODO
 * @date 2019/8/25 0025  12:44
 **/
public abstract class AccountBaseHandler<T> extends BaseHandler<T> implements IAccountHandler<T> {



    @Override
    public RetData<AccountInfo> createAccount(String bizId, String username, String pwd) throws Exception {
        //创建完成调用 db保存
        return null;
    }

    @Override
    public RetData<String> asyncTransfer(String bizId, SrcAccount account, BigInteger amount,
                                         String destAddress, ReqConfig<T> config, ITransferFn callback) throws Exception {
        //4、adjuster

        Map map = new HashMap<String, Object>() {
            {
                put("bizId", bizId);
                put("account", account);
                put("callback", callback);
                put("type", "transfer");
                put("amount", amount);
                put("destAddress", destAddress);
            }
        };
        getDriver().getSpeedAdjuster().offer(map);

        return RetData.succuess(map.hashCode());
    }

    @Override
    public RetData<String> transfer(String bizId, SrcAccount account, BigInteger amount,
                                    String destAddress, ReqConfig<T> config, ITransferFn callback) throws Exception {

        //如果传入的没有，那么就直接计算
        //1、计算nonce
        if (StringUtils.isNullOrEmpty(account.getNonce())) {
            account.setNonce((String) getDriver().getNonceCalculator().getNonce(account.getAccount(), config).getData());
        }
        //2、转换memo
        //todo:多条返回多个hash
        account.setMemos((String) getDriver().getMemoParser().encodeOne(bizId, account.getMemos()));
        //2、计算 gas


        return innerTranfer(bizId, account, amount, destAddress, config, callback);
    }


    public abstract RetData<String> innerTranfer(String bizId, SrcAccount account, BigInteger amount,
                                                 String destAddress, ReqConfig<T> config, ITransferFn callback) throws Exception;
}
