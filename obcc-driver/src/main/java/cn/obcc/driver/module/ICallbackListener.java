package cn.obcc.driver.module;

import cn.obcc.driver.IChainHandler;
import cn.obcc.vo.driver.BlockInfo;
import cn.obcc.vo.driver.BlockTxInfo;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName ICallbackListener
 * @desc TODO
 * @date 2019/8/29 0029  14:44
 **/
public interface ICallbackListener<T> extends IChainHandler<T> {


    public void notify(BlockTxInfo txInfo) throws Exception;

    /**
     * 生成SpcTxInfo，分库分表写到数据库中， 如果存在就抛弃，这时也返回true<br>
     * 订阅或pull主引擎的回调，写到数据<p>
     *
     * @param txInfo
     * @return
     */
    public boolean writeToDb(BlockTxInfo txInfo) throws Exception;

    /**
     * BlockInfo，分库分表写到数据库中， 如果存在就抛弃，这时也返回true 保存<br>
     * <p>
     * 订阅或pull主引擎的回调，写到数据<p>
     *
     * @param blockInfo
     * @return
     */
    boolean writeToDb(BlockInfo blockInfo) throws Exception;


}