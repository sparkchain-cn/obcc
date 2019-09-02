package cn.obcc.driver.nonce.strategy;

import cn.obcc.driver.nonce.INonceStrategy;
import net.jodah.expiringmap.ExpirationPolicy;

import java.util.concurrent.TimeUnit;

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
    public Long adjustNonce(String chainCode, String address,Long num) throws Exception{
      //  String key = genAccountSeqKey(chainCode, address);
        Long nowSeq =num;
       // Long nextSeq = nowSeq + 1;
     //   map.put(key, nextSeq, ExpirationPolicy.ACCESSED, 20 * 60, TimeUnit.SECONDS);
        return nowSeq;
    }

}
