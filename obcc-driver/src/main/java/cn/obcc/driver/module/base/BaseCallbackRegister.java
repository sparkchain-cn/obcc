package cn.obcc.driver.module.base;

import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.module.fn.IStateListener;
import cn.obcc.driver.ICallbackRegister;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author mgicode
 * @version 1.0
 * @ClassName BaseCallbackRegister
 * @desc
 * @data 2019/9/4 9:58
 **/
public class BaseCallbackRegister extends BaseHandler implements ICallbackRegister {

    public static final Logger logger = LoggerFactory.getLogger(BaseAccountHandler.class);

    public ExpiringMap<String, IStateListener> stateListenerMap = ExpiringMap.builder()
            .variableExpiration()
            .build();

    @Override
    public void register(String bizId, IStateListener fn) throws Exception {
        if (stateListenerMap.containsKey(bizId)) {
            logger.warn("注册回调函数时存在重复的bizId:{}", bizId);
            //过期了？不应该
            if (stateListenerMap.get(bizId) != null) {
                logger.warn("注册回调函数时存在重复的bizId:{},,忽略该条注册", bizId);
                return;
            }
        }
        stateListenerMap.put(bizId, fn, ExpirationPolicy.ACCESSED, 50, TimeUnit.MINUTES);
    }

    @Override
    public boolean exist(String bizId) throws Exception {
        return stateListenerMap.containsKey(bizId);
    }

    @Override
    public IStateListener getStateListener(String bizId) {
        return stateListenerMap.get(bizId);
    }

    @Override
    public void unRegister(String bizId) throws Exception {
        stateListenerMap.remove(bizId);
    }
}
