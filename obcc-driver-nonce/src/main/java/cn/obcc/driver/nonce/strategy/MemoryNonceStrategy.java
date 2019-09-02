package cn.obcc.driver.nonce.strategy;

import cn.obcc.driver.nonce.INonceStrategy;
import cn.obcc.exception.ObccException;
import cn.obcc.utils.base.StringUtils;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName MemoryNonceStrategy
 * @desc TODO
 * @date 2019/8/26 0026  8:52
 **/
public abstract class MemoryNonceStrategy implements INonceStrategy {
    public static final Logger logger = LoggerFactory.getLogger(MemoryNonceStrategy.class);
    //chainCode这里需要注意：一个链多个Client,也必须共用一个集合，
    //多个链，多个client,其链Code必须不同，不然其nonce可能会冲突
    public static ExpiringMap<String, Long> map = ExpiringMap.builder()
            .variableExpiration()
            .build();

    private String genAccountSeqKey(String chainCode, String account) {
        return "nonce_" + chainCode + "_" + account;
    }

    @Override
    public Long computNonce(String chainCode, String account) throws Exception {
        String key = genAccountSeqKey(chainCode, account);
        // 取redis中nounce
        Long oldSeq = map.get(key);

        Long nowSeq = null;
        if (oldSeq == null) {
            // 2 没有，那么从链上取
            Long bigInteger = getNonceFromChain(account);
            nowSeq = bigInteger.longValue();
            // map.put(key, nowSeq, 20 * 60, TimeUnit.SECONDS);
        } else {
            nowSeq = oldSeq;
        }

        logger.info(String.format("oldseq:" + oldSeq + " |seq:" + nowSeq + ":time:" + System.currentTimeMillis()));

        Long nextSeq = nowSeq + 1;
        map.put(key, nextSeq, ExpirationPolicy.ACCESSED, 20 * 60, TimeUnit.SECONDS);
        return nowSeq;

    }
    public Long adjustNonce(String chainCode, String address,Long num) throws Exception{
        String key = genAccountSeqKey(chainCode, address);
        Long nowSeq =num;
        Long nextSeq = nowSeq + 1;
        map.put(key, nextSeq, ExpirationPolicy.ACCESSED, 20 * 60, TimeUnit.SECONDS);
        return nowSeq;
    }

}
