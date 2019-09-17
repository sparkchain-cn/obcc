package cn.obcc.driver.module;

import cn.obcc.driver.IChainHandler;
import cn.obcc.vo.driver.BlockInfo;
import cn.obcc.vo.driver.BlockTxInfo;
import cn.obcc.config.ExProps;
import cn.obcc.vo.RetData;

/**
 * @author mgicode
 * @desc IBlockCallback
 * @email 546711211@qq.com
 * @date 2019年8月22日 上午10:04:56
 */
public interface IBlockHandler<T> extends IChainHandler<T> {

    /**
     * 时间间隔
     *
     * @return
     */
    public Long getInterval() throws Exception;

    /**
     * 取得最新的区块高度
     *
     * @return
     * @throws Exception
     */
    public Long getBlockHeight() throws Exception;

    /**
     * 从区块中找到所有交易hash
     *
     * @param blockHeight
     * @param config
     * @return
     * @throws Exception
     */
    public BlockInfo getBlockInfo(long blockHeight, ExProps config) throws Exception;


    /**
     * 给一个区块高度取得该区块所有流水
     *
     * @param hash
     * @return
     * @throws Exception
     */
    public BlockTxInfo getBlockTxInfo(String hash, ExProps config) throws Exception;


    /**
     * 生成SpcTxInfo，分库分表写到数据库中， 如果存在就抛弃，这时也返回true
     *
     * @param txInfo
     * @return
     */
    // public boolean writeToDb(BlockTxInfo txInfo, ReqConfig<T> config) throws Exception;

    /**
     * CbBlockInfo，分库分表写到数据库中， 如果存在就抛弃，这时也返回true 保存
     *
     * @param blockInfo
     * @return
     */
    // boolean writeToDb(BlockInfo blockInfo, ReqConfig<T> config) throws Exception;


}
