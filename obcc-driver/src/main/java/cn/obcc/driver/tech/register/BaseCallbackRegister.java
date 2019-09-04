package cn.obcc.driver.tech.register;

import cn.obcc.driver.module.fn.IUpchainFn;
import cn.obcc.exception.ObccException;
import cn.obcc.exception.enums.EExceptionCode;
import net.jodah.expiringmap.ExpiringMap;

/**
 * @author mgicode
 * @version 1.0
 * @ClassName BaseCallbackRegister
 * @desc
 * @data 2019/9/4 9:58
 **/
public class BaseCallbackRegister implements ICallbackRegister {

    public ExpiringMap<String, IUpchainFn> map = ExpiringMap.builder()
            .variableExpiration()
            .build();

    @Override
    public void register(String bizId, IUpchainFn fn) throws Exception {
        if (map.containsKey(bizId)) {
            throw ObccException.create(EExceptionCode.KEY_REPEAT, "注册回调函数时存在重复的Key.");
        }
        map.put(bizId, fn);
    }

    @Override
    public boolean exist(String bizId) throws Exception {
        return map.containsKey(bizId);
    }

    @Override
    public IUpchainFn getFn(String bizId) {
        return map.get(bizId);
    }

    @Override
    public void unRegister(String bizId) throws Exception {
        map.remove(bizId);
    }
}
