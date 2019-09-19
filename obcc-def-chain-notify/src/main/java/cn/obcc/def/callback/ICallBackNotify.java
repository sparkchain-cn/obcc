package cn.obcc.def.callback;

import cn.obcc.stragegy.IStragegy;
import cn.obcc.vo.driver.BlockTxInfo;

/**
 * @author mgicode
 * @version 1.0
 * @ClassName ICallBackNotify
 * @desc
 * @data 2019/9/3 14:45
 **/
public interface ICallBackNotify<D> extends IStragegy<D> {

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
