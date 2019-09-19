package cn.obcc.nonce.direct;

import cn.obcc.def.nonce.INonceStrategy;
import cn.obcc.driver.IChainDriver;
import cn.obcc.stragegy.BaseStrategy;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName ChainNonceStrategy
 * @desc TODO
 * @date 2019/8/26 0026  8:48
 **/
public class NonceDirectStrategy extends BaseStrategy<IChainDriver> implements INonceStrategy<IChainDriver> {

    @Override
    public Long getNonceFromChain(String address) throws Exception {
        return driver.getNonceCalculator().getNonceFromChain(address);
    }

    @Override
    public Long computNonce(String chainCode, String address) throws Exception {
        return getNonceFromChain(address);
    }

    @Override
    public Long adjustNonce(String chainCode, String address, Long num) throws Exception {
        //  String key = genAccountSeqKey(chainCode, address);
        Long nowSeq = num;
        // Long nextSeq = nowSeq + 1;
        //   map.put(key, nextSeq, ExpirationPolicy.ACCESSED, 20 * 60, TimeUnit.SECONDS);
        return nowSeq;
    }

}
