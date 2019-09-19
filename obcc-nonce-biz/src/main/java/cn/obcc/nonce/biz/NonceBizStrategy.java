package cn.obcc.nonce.biz;

import cn.obcc.def.nonce.INonceStrategy;
import cn.obcc.driver.IChainDriver;
import cn.obcc.stragegy.BaseStrategy;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName BizNonceStrategy
 * @desc TODO
 * @date 2019/8/26 0026  9:44
 **/
public class NonceBizStrategy extends BaseStrategy<IChainDriver> implements INonceStrategy<IChainDriver> {
    @Override
    public Long getNonceFromChain(String address) throws Exception {
        return null;
    }

    @Override
    public Long computNonce(String chainCode, String address) throws Exception {
        return null;
    }

    @Override
    public Long adjustNonce(String chainCode, String address, Long num) throws Exception {
        return num;
    }

}
