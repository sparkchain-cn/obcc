package cn.obcc.driver.tech;

import cn.obcc.driver.IChainHandler;
import cn.obcc.config.ReqConfig;
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
    public RetData<Long> getNonce(String address, ReqConfig<T> config) throws Exception;


}
