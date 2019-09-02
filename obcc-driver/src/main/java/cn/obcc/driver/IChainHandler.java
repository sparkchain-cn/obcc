package cn.obcc.driver;

import cn.obcc.config.ObccConfig;

public interface IChainHandler<T> {

    public IChainHandler initObccConfig(ObccConfig config, IChainDriver<T> driver) throws Exception;

    public ObccConfig getObccConfig() throws Exception;

    public T getClient() throws Exception;

    public T getClient(String uuid) throws Exception;

    public default void destory() throws Exception {

    }
}
