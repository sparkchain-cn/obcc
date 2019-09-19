package cn.obcc.driver.tech;

import cn.obcc.driver.IDriverHandler;
import cn.obcc.config.ExConfig;

public interface INonceCalculator<T> extends IDriverHandler<T> {


    /**
     * pengrk created or updated at 2019年3月29日 上午11:34:35
     *
     * @param address
     * @param config
     * @return
     * @throws Exception
     */
    public Long getNonce(String address, ExConfig config) throws Exception;


    public Long adjustNonce(String address, Long num, ExConfig config) throws Exception;


    public Long getNonceFromChain(String address) throws Exception;
}
