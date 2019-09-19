package cn.obcc.driver.tech.base;

import cn.obcc.config.ObccConfig;
import cn.obcc.def.callback.ICallBackNotify;
import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.IDriverHandler;
import cn.obcc.driver.base.BaseDriverHandler;
import cn.obcc.driver.handler.ICallbackHandler;
import cn.obcc.utils.base.BeanUtils;
import cn.obcc.vo.db.BlockInfo;
import cn.obcc.vo.db.BlockTxInfo;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName CallbackListener
 * @desc TODO
 * @date 2019/8/29 0029  14:48
 **/
public class CallbackHandler<T> extends BaseDriverHandler<T> implements ICallbackHandler<T> {

    ICallBackNotify callBackNotify;

    @Override
    public IDriverHandler init(ObccConfig config, IChainDriver<T> driver) throws Exception {
        super.init(config, driver);
        callBackNotify = (ICallBackNotify) BeanUtils.instance(config.getCallbackNotfyName());
        callBackNotify.init(driver);
        return this;
    }


    @Override
    public void notify(BlockTxInfo txInfo) throws Exception {
        //  if (callBackNotify.filter(txInfo) == true) {
        callBackNotify.notify(txInfo);
        // }
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
