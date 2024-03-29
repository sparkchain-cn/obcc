package cn.obcc.driver.base;

import cn.obcc.config.ObccConfig;
import cn.obcc.connect.pool.ChainClientPoolFactory;
import cn.obcc.db.DbFactory;
import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.handler.*;
import cn.obcc.driver.tech.*;
import cn.obcc.driver.handler.base.BaseCallbackRegister;
import cn.obcc.driver.tech.ICallbackRegister;
import cn.obcc.enums.EDriverHandlerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseChainDriver<T> implements IChainDriver<T> {
    public static final Logger logger = LoggerFactory.getLogger(BaseChainDriver.class);

    protected DbFactory localDb;
    protected ObccConfig obccConfig;

    protected abstract Map<String, Class<? extends BaseDriverHandler>> getHanlderClzes();

    protected Map<String, BaseDriverHandler> handlerMap = new HashMap<>();

    protected Map<String, Class<? extends BaseDriverHandler>> handlerClzMap =
            new HashMap<String, Class<? extends BaseDriverHandler>>() {{
                put(EDriverHandlerType.CallbackRegister.getName(), BaseCallbackRegister.class);
            }};

    public BaseChainDriver() {
    }

    @Override
    public IChainDriver init(ObccConfig config, DbFactory db) throws Exception {
        this.obccConfig = config;
        this.localDb = db;
        handlerClzMap.putAll(getHanlderClzes());
        handlerClzMap.putAll(initConfigAbleHandler());
        handlerClzMap.forEach((key, clz) -> {
            try {
                handlerMap.put(key, clz.newInstance());
                handlerMap.get(key).init(obccConfig, this);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        handlerMap.forEach((key, handler) -> {
            try {
                handler.onAfterInit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return this;
    }

    protected Map<String, Class<? extends BaseDriverHandler>> initConfigAbleHandler() throws Exception {
        Map<String, Class<? extends BaseDriverHandler>> handlerClzMap = new HashMap<>();

        //speedAdjuster
        String clzName = obccConfig.getSpeedAdjusterName();
        handlerClzMap.put(EDriverHandlerType.SpeedAdjuster.getName(), (Class<? extends BaseDriverHandler>) Class.forName(clzName));

        return handlerClzMap;
    }


    @Override
    public T getChainClient() throws Exception {
        return ChainClientPoolFactory.getClient(obccConfig, getChainClientBuilderClz());
    }

    @Override
    public T getChainClient(String uuid) throws Exception {
        return ChainClientPoolFactory.getClient(getChainClientName(), uuid, getChainClientBuilderClz());
    }

    @Override
    public ISpeedAdjuster<T> getSpeedAdjuster() throws Exception {
        return (ISpeedAdjuster) handlerMap.get(EDriverHandlerType.SpeedAdjuster.getName());
    }


    @Override
    public ICallbackRegister getCallbackRegister() throws Exception {
        //  return new BaseCallbackRegister();
        return (ICallbackRegister) handlerMap.get(EDriverHandlerType.CallbackRegister.getName());
    }

    @Override
    public IContractHandler getContractHandler() throws Exception {
        return (IContractHandler) handlerMap.get(EDriverHandlerType.ContractHandler.getName());
    }

    @Override
    public IBlockHandler<T> getBlockHandler() throws Exception {
        return (IBlockHandler) handlerMap.get(EDriverHandlerType.BlockHandler.getName());
    }

    @Override
    public ICallbackHandler<T> getCallbackListener() throws Exception {
        return (ICallbackHandler) handlerMap.get(EDriverHandlerType.CallbackListener.getName());
    }

    @Override
    public ITokenHandler<T> getTokenHandler() throws Exception {
        return (ITokenHandler) handlerMap.get(EDriverHandlerType.TokenHandler.getName());
    }

    @Override
    public IAccountHandler getAccountHandler() throws Exception {
        return (IAccountHandler) handlerMap.get(EDriverHandlerType.AccountHandler.getName());
    }

    @Override
    public IStateMonitor getStateMonitor() throws Exception {
        return (IStateMonitor) handlerMap.get(EDriverHandlerType.StateMonitor.getName());
    }

    @Override
    public INonceCalculator getNonceCalculator() throws Exception {
        return (INonceCalculator) handlerMap.get(EDriverHandlerType.NonceCalculator.getName());
    }

    @Override
    public ISignatureHandler<T> getTxSignatureHandler() throws Exception {
        return (ISignatureHandler) handlerMap.get(EDriverHandlerType.TxSignatureHandler.getName());
    }

    @Override
    public IMemoParser<T> getMemoParser() throws Exception {
        return (IMemoParser) handlerMap.get(EDriverHandlerType.MemoParser.getName());
    }

    public String getChainClientName() {
        return obccConfig.getClientId() + "-" + obccConfig.getChain().getName();
    }

    @Override
    public DbFactory getLocalDb() throws Exception {
        return this.localDb;
    }

    @Override
    public ObccConfig getConfig() {
        return obccConfig;
    }

    @Override
    public void destory() {
        handlerMap.clear();

    }


}
