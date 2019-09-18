package cn.obcc.driver.state;

import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.module.base.BaseAccountHandler;
import cn.obcc.driver.tech.IStateMonitor;
import cn.obcc.exception.ObccException;
import cn.obcc.exception.enums.EExceptionCode;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.vo.BizState;
import cn.obcc.vo.driver.RecordInfo;
import net.jodah.expiringmap.ExpirationPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName BaseStateMonitor
 * @desc TODO
 * @date 2019/9/7 0007  22:32
 **/
public abstract class BaseStateMonitor<T> extends BaseHandler<T> implements IStateMonitor<T> {
    public static final Logger logger = LoggerFactory.getLogger(BaseAccountHandler.class);

    @Override
    public void checkAndSetBizId(String bizId) throws Exception {
        checkBizId(bizId);
        setBizId(bizId);
    }

    @Override
    public void checkBizId(String bizId) throws Exception {
        if (existBizId(bizId)) {
            throw ObccException.create(EExceptionCode.EXIST_BIZID,
                    " bizid:{} has exist in cache,maybe you have unfinished biz.", bizId);
        }
    }

    @Override
    public void checkAndsupplyBizId(String bizId) throws Exception {
        //直接调用
        if (!existBizId(bizId)) {
            setBizId(bizId);
        }
        boolean flag = getDriver().getLocalDb().getRecordInfoDao().existBizId(bizId);
        if (flag == true) {
            throw ObccException.create(EExceptionCode.EXIST_BIZID,
                    " bizid:{} has exist in db,maybe you have unfinished biz.", bizId);
        }

        //throw ObccException.create(EExceptionCode.EXIST_BIZID,
        // " bizid:{} has not exist in cache,maybe you have unfinished biz.", bizId);

    }

    @Override
    public void setBizId(String bizId) throws Exception {
        IStateMonitor.BizIdMap.put(bizId, bizId);
    }

    @Override
    public boolean existBizId(String bizId) throws Exception {
        return IStateMonitor.BizIdMap.containsKey(bizId);
    }

    @Override
    public void onAfterInit() throws Exception {
        RecordInfo r;
        List<String> list = getDriver().getLocalDb().
                getRecordInfoDao().getValues(" select biz_id from record_info ", new Object[]{});
        list.stream().forEach((s) -> {
            IStateMonitor.BizIdMap.put(s, s);
        });
    }


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
        if (state == null) {
            return;
        }
        IStateMonitor.BizStateMap.remove(bizId);

        Arrays.stream(state.getHashes().split(",")).forEach((hash) -> {
            try {
                delHashState(hash);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public void setHashState(String hash, BizState status) throws Exception {
        if (StringUtils.isNullOrEmpty(hash)) {
            logger.warn("set hash state,but the hash is null or empty.");
            return;
        }
        IStateMonitor.StateMap.put(hash, status, ExpirationPolicy.ACCESSED, 20, TimeUnit.MINUTES);
    }

    @Override
    public BizState getHashState(String hash) throws Exception {
        if (!IStateMonitor.StateMap.containsKey(hash)) {
            logger.warn("IStateMonitor.StateMap no exist hash:{} 's key.", hash);
            return null;
        }
        return IStateMonitor.StateMap.get(hash);
    }

    @Override
    public boolean existHash(String hash) throws Exception {
        return IStateMonitor.StateMap.containsKey(hash);
    }

    @Override
    public void delHashState(String hash) throws Exception {
        IStateMonitor.StateMap.remove(hash);
    }


}
