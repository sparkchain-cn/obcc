package cn.obcc.notify.uri;

import cn.obcc.def.callback.ICallBackNotify;
import cn.obcc.driver.IChainDriver;
import cn.obcc.stragegy.BaseStrategy;
import cn.obcc.vo.db.BlockTxInfo;

/**
 * @author mgicode
 * @version 1.0
 * @ClassName UrlCallbackNotify
 * @desc
 * @data 2019/9/3 14:48
 **/
public class UrlNotifyStrategy<T> extends BaseStrategy<IChainDriver> implements ICallBackNotify<IChainDriver> {

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
