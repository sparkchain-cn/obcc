package cn.obcc.driver.base;

import cn.obcc.config.ObccConfig;
import cn.obcc.connect.pool.ChainClientPoolFactory;
import cn.obcc.db.DbFactory;
import cn.obcc.db.base.JdbcDao;
import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.tech.ISpeedAdjuster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseChainDriver<T> implements IChainDriver<T> {
    public static final Logger logger = LoggerFactory.getLogger(BaseChainDriver.class);
    protected ISpeedAdjuster speedAdjuster;

    protected DbFactory localDb;

    public BaseChainDriver() {

    }

    protected ObccConfig obccConfig;


    protected String getChainClientName() {
        return obccConfig.getClientId() + "_" + obccConfig.getChain().name();
    }

    @Override
    public IChainDriver init(ObccConfig config, DbFactory db) throws Exception {
        this.obccConfig = config;
        this.localDb = db;
        return this;
    }

    @Override
    public DbFactory getLocalDb() throws Exception {
        return this.localDb;
    }

    @Override
    public T getChainClient() throws Exception {
        return ChainClientPoolFactory.getClient(obccConfig, getChainClientBuilderClz());
    }

    @Override
    public T getChainClient(String uuid) throws Exception {
        return ChainClientPoolFactory.getClient(getChainClientName(), uuid, getChainClientBuilderClz());
    }


    public ISpeedAdjuster<T> getSpeedAdjuster() throws Exception {
        if (speedAdjuster == null) {
            String clzName = obccConfig.getSpeedAdjusterName();
            //  speedAdjuster = new SpeedAdjuster<T>();
            speedAdjuster = (ISpeedAdjuster<T>) newHandler(clzName);
            speedAdjuster.start();
        }
        return speedAdjuster;
    }


    private BaseHandler newHandler(String name) {
        try {
            return (BaseHandler) getClass().getClassLoader().loadClass(name).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            logger.error("不能加载或实例化Handler:" + name + ",请引用对应的JAR,并设定好对应的Handler类名。");
        }

        return null;

    }

    @Override
    public void destory() {
        // TODO Auto-generated method stub

    }

}
