package cn.obcc.state.redis;

import cn.obcc.def.state.IHashStateStrategy;
import cn.obcc.driver.IChainDriver;
import cn.obcc.stragegy.BaseStrategy;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.vo.BizState;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName RedisNonceStrategy
 * @desc TODO
 * @date 2019/8/26 0026  9:37
 **/
public class HashStateRedisStrategy extends BaseStrategy<IChainDriver> implements IHashStateStrategy<IChainDriver> {
    public static final Logger logger = LoggerFactory.getLogger(HashStateRedisStrategy.class);

    //hash->state
    public static ExpiringMap<String, BizState> StateMap = ExpiringMap.builder()
            .variableExpiration()
            .build();

    @Override
    public void setHashState(String hash, BizState status) throws Exception {
        if (StringUtils.isNullOrEmpty(hash)) {
            logger.warn("set hash state,but the hash is null or empty.");
            return;
        }
        StateMap.put(hash, status, ExpirationPolicy.ACCESSED, 20, TimeUnit.MINUTES);
    }

    @Override
    public BizState getHashState(String hash) throws Exception {
        if (!StateMap.containsKey(hash)) {
            logger.warn("IStateMonitor.StateMap no exist hash:{} 's key.", hash);
            return null;
        }
        return StateMap.get(hash);
    }

    @Override
    public boolean existHash(String hash) throws Exception {
        return StateMap.containsKey(hash);
    }

    @Override
    public void delHashState(String hash) throws Exception {
        StateMap.remove(hash);
    }


}
