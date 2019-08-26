package cn.obcc.driver.nonce.strategy;

import cn.obcc.driver.nonce.INonceStrategy;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName ChainNonceStrategy
 * @desc TODO
 * @date 2019/8/26 0026  8:48
 **/
public abstract class ChainNonceStrategy implements INonceStrategy {

    @Override
    public Long computNonce(String chainCode, String address) throws Exception {
        return getNonceFromChain(address);
    }
}
