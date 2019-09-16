package cn.obcc.driver.callback.notify.base;

import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.callback.notify.ICallBackNotify;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.vo.BcMemo;
import cn.obcc.vo.driver.BlockTxInfo;

/**
 * @author mgicode
 * @version 1.0
 * @ClassName UrlCallbackNotify
 * @desc
 * @data 2019/9/3 14:48
 **/
public class UrlCallbackNotify<T> extends BaseHandler<T> implements ICallBackNotify<T> {

    @Override
    public void notify(BlockTxInfo txInfo) throws Exception {

    }

    @Override
    public boolean filter(BlockTxInfo txInfo) throws Exception {
       // BcMemo memo=txInfo.getMemosObj();
        //return memo != null && !StringUtils.isNullOrEmpty(memo.getBizId())
        return true;
    }
}
