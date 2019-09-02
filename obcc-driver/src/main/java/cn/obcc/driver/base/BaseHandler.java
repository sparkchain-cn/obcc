package cn.obcc.driver.base;

import cn.obcc.config.ObccConfig;
import cn.obcc.connect.pool.ChainClientPoolFactory;
import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.IChainHandler;

public class BaseHandler<T> implements IChainHandler<T> {

    protected ObccConfig config;

    protected IChainDriver<T> driver;

    @Override
    public IChainHandler initObccConfig(ObccConfig config, IChainDriver<T> driver) throws Exception {
        this.config = config;
        this.driver = driver;
        return this;

    }

    @Override
    public ObccConfig getObccConfig() {
        return config;
    }

    @Override
    public T getClient() throws Exception {
        return driver.getChainClient();
    }

    @Override
    public T getClient(String uuid) throws Exception {
        return driver.getChainClient(uuid);
    }

    public IChainDriver<T> getDriver() throws Exception {
        return this.driver;
    }
}
