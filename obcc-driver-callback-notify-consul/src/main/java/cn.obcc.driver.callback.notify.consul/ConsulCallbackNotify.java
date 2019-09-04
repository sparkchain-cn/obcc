package cn.obcc.driver.callback.notify.consul;

import cn.obcc.driver.callback.notify.ICallBackNotify;
import cn.obcc.driver.callback.notify.base.UrlCallbackNotify;
import cn.obcc.vo.driver.BlockTxInfo;

/**
 * @author mgicode
 * @version 1.0
 * @ClassName ConsulCallbackNotify
 * @desc
 * @data 2019/9/3 14:50
 **/
public class ConsulCallbackNotify extends UrlCallbackNotify implements ICallBackNotify {

    @Override
    public void notify(BlockTxInfo txInfo) throws Exception {

    }

    @Override
    public boolean filter(BlockTxInfo txInfo) throws Exception {

        return true;
    }
}
