package cn.obcc.driver.nonce.strategy;

import cn.obcc.driver.nonce.INonceStrategy;
import cn.obcc.utils.base.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName RedisNonceStrategy
 * @desc TODO
 * @date 2019/8/26 0026  9:37
 **/
public abstract class BaseRedisNonceStrategy implements INonceStrategy {
    public static final Logger logger = LoggerFactory.getLogger(BaseMemoryNonceStrategy.class);



    public abstract String getKeyFromRedis(String key);

    public abstract String setValueIntoRedis(String key, Long value, Long time);


    @Override
    public Long computNonce(String chainCode, String address) throws Exception {
        // ,eth不能减，所以只有先到链上取，然后重试加，这种方案不行，这样的命名没有先把redis中取，大于再减的效率高
        // 1 先看redis有没有？
        Long nowSeq = storeNounceAndGetNow(null, chainCode, address);
        if (nowSeq == null) {
            nowSeq = getNounceFormRedis(chainCode, address);
            // 2 没有，那么从链上取
            //nowSeq = bigInteger.longValue();
            // 3，保存其+1的数据到redis，让其它服务器取下一个
            storeNounceAndGetNow(nowSeq, chainCode, address);
        }
        return nowSeq;
        // return nowSeq;
    }

    // RedisEthService redisEthService;
    public String genAccountSeqKey(String chainCode, String account) {
        return "nonce_" + chainCode + "_" + account;
    }

    public Long getNounceFormRedis(String chainCode, String account) {
        String key = genAccountSeqKey(chainCode, account);
        String value = getKeyFromRedis(key);// null;// redisEthService.get(key);
        if (value == null) {
            return null;
        }
        return Long.parseLong(value);
    }


    public Long storeNounceAndGetNow(Long nowSeq, String chainCode, String account) {

        String key = genAccountSeqKey(chainCode, account);
        String value = getKeyFromRedis(key);// redisEthService.get(key);

        // 取redis中nounce
        Long oldSeq = null;
        if (StringUtils.isNotNullOrEmpty(value)) {
            oldSeq = Long.parseLong(value);
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
        setValueIntoRedis(key, nextSeq, 20 * 60L);
        return nowSeq;

    }

    @Override
    public Long adjustNonce(String chainCode, String address, Long num) throws Exception{
        String key = genAccountSeqKey(chainCode, address);
        Long nowSeq =num;
        Long nextSeq = nowSeq + 1;
        setValueIntoRedis(key, nextSeq, 20 * 60L);
        return nowSeq;
    }


}
