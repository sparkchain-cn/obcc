package cn.obcc.driver.tech.base;

import cn.obcc.config.ExConfig;
import cn.obcc.config.ObccConfig;
import cn.obcc.db.utils.BeanUtils;
import cn.obcc.def.nonce.INonceStrategy;
import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.IDriverHandler;
import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.tech.INonceCalculator;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName NonceCu
 * @desc TODO
 * @date 2019/8/26 0026  8:36
 **/
public abstract class BaseNonceCalculator<T> extends BaseHandler<T> implements INonceCalculator<T> {
    protected INonceStrategy<T> nonceStrategy;

    @Override
    public IDriverHandler init(ObccConfig config, IChainDriver<T> driver) throws Exception {
        super.init(config, driver);
        nonceStrategy = (INonceStrategy<T>) BeanUtils.newInstance(config.getNonceStrategyClzName());
        return this;
    }

    @Override
    public Long getNonce(String address, final ExConfig config) throws Exception {
        String chainCode = getConfig().getChain().getName();
        return nonceStrategy.computNonce(chainCode, address);
    }


    @Override
    public Long adjustNonce(String address, Long num, ExConfig config) throws Exception {
        String chainCode = getConfig().getChain().getName();
        return nonceStrategy.adjustNonce(chainCode, address, num);
    }


}
