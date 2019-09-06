package cn.obcc.driver.tech;

import cn.obcc.driver.IChainHandler;
import cn.obcc.config.ExProps;
import cn.obcc.vo.RetData;

public interface INonceCalculator<T> extends IChainHandler<T> {


    /**
     * pengrk created or updated at 2019年3月29日 上午11:34:35
     *
     * @param address
     * @param config
     * @return
     * @throws Exception
     */
    public Long getNonce(String address, ExProps config) throws Exception;


    public Long adjustNonce(String address, Long num, ExProps config) throws Exception;


    public Long getNonceFromChain(String address) throws Exception;
}
