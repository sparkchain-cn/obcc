package cn.obcc.driver.tech.base;

import cn.obcc.config.ObccConfig;
import cn.obcc.def.state.IBizIdCheckStrategy;
import cn.obcc.def.state.IBizStateStrategy;
import cn.obcc.def.state.IHashStateStrategy;
import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.IDriverHandler;
import cn.obcc.driver.base.BaseDriverHandler;
import cn.obcc.driver.handler.base.BaseAccountHandler;
import cn.obcc.driver.tech.IStateMonitor;
import cn.obcc.exception.ObccException;
import cn.obcc.enums.EExceptionCode;
import cn.obcc.utils.base.BeanUtils;
import cn.obcc.vo.BizState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName BaseStateMonitor
 * @desc TODO
 * @date 2019/9/7 0007  22:32
 **/
public class BaseStateMonitor<T> extends BaseDriverHandler<T> implements IStateMonitor<T> {
    public static final Logger logger = LoggerFactory.getLogger(BaseAccountHandler.class);

    IBizStateStrategy bizStateStrategy;
    IHashStateStrategy hashStateStrategy;
    IBizIdCheckStrategy bizIdCheckStrategy;

    @Override
    public IDriverHandler init(ObccConfig config, IChainDriver<T> driver) throws Exception {
        super.init(config, driver);

        bizStateStrategy = (IBizStateStrategy) BeanUtils.instance(config.getBizStateStrategyClzName());
        bizStateStrategy.init(driver);
        hashStateStrategy = (IHashStateStrategy) BeanUtils.instance(config.getHashStateStrategyClzName());
        hashStateStrategy.init(driver);

        bizIdCheckStrategy = (IBizIdCheckStrategy) BeanUtils.instance(config.getBizIdCheckStrategyClzName());
        bizIdCheckStrategy.init(driver);

        return this;
    }

    @Override
    public void onAfterInit() throws Exception {
        bizStateStrategy.onAfterInit();
        hashStateStrategy.onAfterInit();
        bizIdCheckStrategy.onAfterInit();
    }

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
    public void checkAndSupplyBizId(String bizId) throws Exception {
        //直接调用
        if (!existBizId(bizId)) {
            setBizId(bizId);
        }
        boolean flag = getDriver().getLocalDb().getRecordInfoDao().existBizId(bizId);
        if (flag == true) {
            throw ObccException.create(EExceptionCode.EXIST_BIZID,
                    " bizid:{} has exist in db,maybe you have unfinished biz.", bizId);
        }

    }

    @Override
    public void setBizId(String bizId) throws Exception {
        bizIdCheckStrategy.setBizId(bizId);
    }

    @Override
    public boolean existBizId(String bizId) throws Exception {
        return bizIdCheckStrategy.existBizId(bizId);
    }


    @Override
    public void setBizState(String bizId, BizState bizState) throws Exception {
        bizStateStrategy.setBizState(bizId, bizState);
    }

    @Override
    public BizState getBizState(String bizId) throws Exception {
        return bizStateStrategy.getBizState(bizId);
    }


    @Override
    public boolean existBizState(String bizId) throws Exception {
        return bizStateStrategy.existBizState(bizId);
    }

    @Override
    public void delBizState(String bizId) throws Exception {
        bizStateStrategy.delBizState(bizId);

    }

    @Override
    public void setHashState(String hash, BizState status) throws Exception {
        hashStateStrategy.setHashState(hash, status);
    }

    @Override
    public BizState getHashState(String hash) throws Exception {
        return hashStateStrategy.getHashState(hash);
    }

    @Override
    public boolean existHash(String hash) throws Exception {
        return hashStateStrategy.existHash(hash);
    }

    @Override
    public void delHashState(String hash) throws Exception {
        hashStateStrategy.delHashState(hash);

    }


}
