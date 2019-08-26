package cn.obcc.driver.nonce.strategy;

import cn.obcc.driver.nonce.INonceStrategy;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName BizNonceStrategy
 * @desc TODO
 * @date 2019/8/26 0026  9:44
 **/
public class BizNonceStrategy implements INonceStrategy {
    @Override
    public Long getNonceFromChain(String address) throws Exception {
        return null;
    }

    @Override
    public Long computNonce(String chainCode, String address) throws Exception {
        return null;
    }
}
