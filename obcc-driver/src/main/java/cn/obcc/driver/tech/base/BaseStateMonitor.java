package cn.obcc.driver.tech.base;

import cn.obcc.config.ExProps;
import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.module.base.AccountBaseHandler;
import cn.obcc.driver.tech.IStateMonitor;
import cn.obcc.exception.enums.ETransferStatus;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.vo.BizState;
import net.jodah.expiringmap.ExpirationPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName BaseStateMonitor
 * @desc TODO
 * @date 2019/9/7 0007  22:32
 **/
public abstract class BaseStateMonitor<T> extends BaseHandler<T> implements IStateMonitor<T> {
    public static final Logger logger = LoggerFactory.getLogger(AccountBaseHandler.class);

    @Override
    public void setBizState(String bizId, BizState bizState) throws Exception {
        if (StringUtils.isNullOrEmpty(bizId)) {
            logger.warn("set hash state,but the hash is null or empty.");
            return;
        }
        IStateMonitor.BizStateMap.put(bizId, bizState, ExpirationPolicy.ACCESSED, 20, TimeUnit.MINUTES);
    }

    @Override
    public BizState getBizState(String bizId) throws Exception {
        if (!IStateMonitor.StateMap.containsKey(bizId)) {
            logger.warn("IStateMonitor.StateMap no exist bizId:{} 's key.", bizId);
            return null;
        }
        return IStateMonitor.BizStateMap.get(bizId);
    }


    @Override
    public boolean existBizState(String bizId) throws Exception {
        return IStateMonitor.BizStateMap.containsKey(bizId);
    }

    @Override
    public void delBizState(String bizId) throws Exception {
        BizState state = getBizState(bizId);
        if (state == null) return;
        IStateMonitor.BizStateMap.remove(bizId);
        if (state.isSingle()) {
            delState(state.getHash());
        } else {
            Arrays.stream(state.getHash().split(",")).forEach((hash) -> {
                try {
                    delState(hash);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void setState(String hash, BizState status) throws Exception {
        if (StringUtils.isNullOrEmpty(hash)) {
            logger.warn("set hash state,but the hash is null or empty.");
            return;
        }
        IStateMonitor.StateMap.put(hash, status, ExpirationPolicy.ACCESSED, 20, TimeUnit.MINUTES);
    }

    @Override
    public BizState getState(String hash) throws Exception {
        if (!IStateMonitor.StateMap.containsKey(hash)) {
            logger.warn("IStateMonitor.StateMap no exist hash:{} 's key.", hash);
            return null;
        }
        return IStateMonitor.StateMap.get(hash);
    }

    @Override
    public boolean exist(String hash) throws Exception {
        return IStateMonitor.StateMap.containsKey(hash);
    }

    @Override
    public void delState(String hash) throws Exception {
        IStateMonitor.StateMap.remove(hash);
    }


}
