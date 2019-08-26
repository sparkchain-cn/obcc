package cn.obcc.driver.nonce.strategy;

import cn.obcc.driver.nonce.INonceStrategy;
import cn.obcc.exception.ObccException;
import cn.obcc.utils.base.StringUtils;
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
    public Long computNonce(String chainCode, String address) throws Exception {
        // ,eth不能减，所以只有先到链上取，然后重试加，这种方案不行，这样的命名没有先把redis中取，大于再减的效率高
        // 1 先看redis有没有？
        Long nowSeq = storeNounce(null, chainCode, address);
        if (nowSeq == null) {
            Long bigInteger = getNonceFromChain(address);
            // 2 没有，那么从链上取
            nowSeq = bigInteger.longValue();
            // 3，保存其+1的数据到redis，让其它服务器取下一个
            storeNounce(nowSeq, chainCode, address);
        }

        return nowSeq;
        // return nowSeq;
    }


    private Long storeNounce(Long nowSeq, String chainCode, String account) {

        String key = genAccountSeqKey(chainCode, account);
        Long value = map.get(key);// redisEthService.get(key);

        // 取redis中nounce
        Long oldSeq = null;
        if (value == null) {
            oldSeq = value;
        }

        if (nowSeq == null && oldSeq == null) {
            // 没有传入，redis中没有，那就返回null
            return null;
        } else if (nowSeq == null && oldSeq != null) {
            // 没有传入，redis中有，那么就是redis中大，取redis中当作当前的nounce，并计算下一个写到redis
            nowSeq = oldSeq;
        } else if (nowSeq != null && oldSeq == null) {
            nowSeq = nowSeq;
            // 传入，redis中没有，那么就是传入中大，取nowSeq中当作当前的nounce，并计算下一个写到redis
        } else if (nowSeq != null && oldSeq != null) {
            // 传入，redis中有，比大小，取大的中当作当前的nounce，并计算下一个写到redis
            if (oldSeq > nowSeq) {
                nowSeq = oldSeq;
            } else {
                nowSeq = nowSeq;
            }
        }

        logger.info(String.format("oldseq:" + oldSeq + " |seq:" + nowSeq + ":time:" + System.currentTimeMillis()));

        // 存入到redis是下一个seq
        // Long nextSeq = nowSeq + 1;
        Long nextSeq = nowSeq;
        // 20分钟进行重置，从链上取代表其
        //redisEthService.set(key, nextSeq, 20 * 60);
        map.put(key, nextSeq, 20 * 60, TimeUnit.SECONDS);
        return nowSeq;

    }

}
