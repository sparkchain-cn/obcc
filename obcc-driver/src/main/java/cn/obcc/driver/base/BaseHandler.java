package cn.obcc.driver.base;

import cn.obcc.config.ObccConfig;
import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.IDriverHandler;

public class BaseHandler<T> implements IDriverHandler<T> {

    protected ObccConfig config;


    protected IChainDriver<T> driver;

    @Override
    public IDriverHandler init(ObccConfig config, IChainDriver<T> driver) throws Exception {
        this.config = config;
        this.driver = driver;
        return this;
    }

    @Override
    public ObccConfig getConfig() {
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

    public void onAfterInit() throws Exception {
    }

}
