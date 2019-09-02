package cn.obcc.driver;

import cn.obcc.config.ObccConfig;
import cn.obcc.connect.builder.ChainClientBuilder;
import cn.obcc.db.DbFactory;
import cn.obcc.db.base.JdbcDao;
import cn.obcc.driver.module.*;
import cn.obcc.driver.tech.*;

/**
 * @author mgicode
 * @desc IDriverManager
 * @email 546711211@qq.com
 * @date 2019年8月22日 上午9:50:21
 */
public interface IChainDriver<T> {

    /**
     * pengrk created or updated at 2019年8月22日 上午10:01:33
     */
    public IChainDriver<T> init(ObccConfig config, DbFactory db) throws Exception;

    public DbFactory getLocalDb() throws Exception;
    /*********************************************************************/
    /**
     * 获取区块链的客户端类，如eth中使用Web3j<br>
     * pengrk created or updated at 2019年8月23日 上午11:16:52
     *
     * @return
     * @throws Exception
     */
    public T getChainClient() throws Exception;


    public T getChainClient(String uuid) throws Exception;

    public Class<? extends ChainClientBuilder<T>> getChainClientBuilderClz() throws Exception;

    /*********************************************************************/

    public IAccountHandler<T> getAccountHandler() throws Exception;

    public ITokenHandler<T> getTokenHandler() throws Exception;

    public IContractHandler<T> getContractHandler() throws Exception;

    public IBlockHandler<T> getBlockHandler() throws Exception;

    public ICallbackListener<T> getCallbackListener() throws  Exception;

    /*****************************************************************/

    public IStateMonitor<T> getStateMonitor() throws Exception;

    public INonceCalculator<T> getNonceCalculator() throws Exception;

    public ITxSignature<T> getTxSignature() throws Exception;

    public IMemoParser<T> getMemoParser() throws Exception;

    public ISpeedAdjuster<T> getSpeedAdjuster() throws Exception;

    /*****************************************************************/

    public void destory();

}
