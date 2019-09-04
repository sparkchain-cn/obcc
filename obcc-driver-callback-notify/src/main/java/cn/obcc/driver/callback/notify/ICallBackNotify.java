package cn.obcc.driver.callback.notify;

import cn.obcc.driver.IChainHandler;
import cn.obcc.vo.driver.BlockTxInfo;

/**
 * @author mgicode
 * @version 1.0
 * @ClassName ICallBackNotify
 * @desc
 * @data 2019/9/3 14:45
 **/
public interface ICallBackNotify<T>   extends IChainHandler<T> {

    /**
     *
     * @param txInfo
     * @throws Exception
     */
    public void notify(BlockTxInfo txInfo) throws Exception ;

    /**
     *
     * @param txInfo
     * @return
     * @throws Exception
     */
    public boolean  filter(BlockTxInfo txInfo)throws Exception ;
}
