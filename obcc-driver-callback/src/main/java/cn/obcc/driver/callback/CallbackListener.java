package cn.obcc.driver.callback;

import cn.obcc.config.ObccConfig;
import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.IChainHandler;
import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.callback.notify.ICallBackNotify;
import cn.obcc.driver.module.ICallbackListener;
import cn.obcc.utils.base.BeanUtils;
import cn.obcc.vo.driver.BlockInfo;
import cn.obcc.vo.driver.BlockTxInfo;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName CallbackListener
 * @desc TODO
 * @date 2019/8/29 0029  14:48
 **/
public class CallbackListener<T> extends BaseHandler<T> implements ICallbackListener<T> {

    ICallBackNotify callBackNotify;

    @Override
    public IChainHandler initObccConfig(ObccConfig config, IChainDriver<T> driver) throws Exception {
        super.initObccConfig(config, driver);
        callBackNotify = (ICallBackNotify) BeanUtils.instance(config.getCallbackNotfyName());
        callBackNotify.initObccConfig(config, driver);
        return this;
    }


    @Override
    public void notify(BlockTxInfo txInfo) throws Exception {
        if (callBackNotify.filter(txInfo) == true) {
            callBackNotify.notify(txInfo);
        }
    }

    @Override
    public boolean writeToDb(BlockTxInfo txInfo) throws Exception {
        driver.getLocalDb().getTxInfoDao().add(txInfo);
        return true;
    }

    @Override
    public boolean writeToDb(BlockInfo blockInfo) throws Exception {
        return false;
    }
}
