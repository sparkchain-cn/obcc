package cn.obcc.driver.tech.base;

import cn.obcc.config.ExProps;
import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.module.base.AccountBaseHandler;
import cn.obcc.driver.tech.IStateMonitor;
import cn.obcc.exception.enums.ETransferStatus;
import cn.obcc.utils.base.StringUtils;
import net.jodah.expiringmap.ExpirationPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public void setState(String hash, ETransferStatus status) throws Exception {
        if (StringUtils.isNullOrEmpty(hash)) {
            logger.warn("set hash state,but the hash is null or empty.");
            return;
        }
        IStateMonitor.StateMap.put(hash, status, ExpirationPolicy.ACCESSED, 20, TimeUnit.MINUTES);
    }

    @Override
    public ETransferStatus getState(String hash) throws Exception {
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
