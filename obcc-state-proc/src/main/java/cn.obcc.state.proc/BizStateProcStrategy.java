package cn.obcc.state.proc;

import cn.obcc.def.state.IBizStateStrategy;
import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.tech.IStateMonitor;
import cn.obcc.stragegy.BaseStrategy;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.vo.BizState;
import cn.obcc.vo.db.RecordInfo;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName RedisNonceStrategy
 * @desc TODO
 * @date 2019/8/26 0026  9:37
 **/
public class BizStateProcStrategy extends BaseStrategy<IChainDriver> implements IBizStateStrategy<IChainDriver> {
    public static final Logger logger = LoggerFactory.getLogger(BizStateProcStrategy.class);
    //todo:采用redis进行
    //bizId->state
    public static ExpiringMap<String, BizState> BizStateMap = ExpiringMap.builder()
            .variableExpiration()
            .build();


    @Override
    public void setBizState(String bizId, BizState bizState) throws Exception {
        if (StringUtils.isNullOrEmpty(bizId)) {
            logger.warn("set hash state,but the hash is null or empty.");
            return;
        }
        BizStateMap.put(bizId, bizState, ExpirationPolicy.ACCESSED, 20, TimeUnit.MINUTES);
    }

    @Override
    public BizState getBizState(String bizId) throws Exception {
        if (!BizStateMap.containsKey(bizId)) {
            logger.warn("IStateMonitor.StateMap no exist bizId:{} 's key.", bizId);
            return null;
        }
        return BizStateMap.get(bizId);
    }


    @Override
    public boolean existBizState(String bizId) throws Exception {
        return BizStateMap.containsKey(bizId);
    }

    @Override
    public void delBizState(String bizId) throws Exception {
        BizState state = getBizState(bizId);
        if (state == null) {
            return;
        }
        BizStateMap.remove(bizId);
//
//        Arrays.stream(state.getHashes().split(",")).forEach((hash) -> {
//            try {
//                delHashState(hash);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });

    }


}
