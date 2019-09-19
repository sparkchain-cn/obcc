package cn.obcc.driver;

import cn.obcc.config.ObccConfig;

public interface IDriverHandler<T> {

    public IDriverHandler init(ObccConfig config, IChainDriver<T> driver) throws Exception;

    public ObccConfig getConfig() throws Exception;

    public T getClient() throws Exception;

    public T getClient(String uuid) throws Exception;

    public default void destory() throws Exception {

    }
}
