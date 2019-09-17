package cn.obcc.driver.eth.module.tech;

import cn.obcc.config.ObccConfig;
import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.IChainHandler;
import cn.obcc.driver.callback.CallbackListener;
import cn.obcc.driver.eth.module.tech.callback.EthTxMonitor;
import cn.obcc.driver.module.ICallbackListener;
import io.reactivex.disposables.Disposable;
import org.web3j.protocol.Web3j;

import java.util.List;

public class EthCallBackListener extends CallbackListener<Web3j> implements ICallbackListener<Web3j> {

    List<Disposable> subcribeList;

    @Override
    public IChainHandler initObccConfig(ObccConfig config, IChainDriver<Web3j> driver) throws Exception {
        super.initObccConfig(config, driver);
        subcribeList = new EthTxMonitor().init(getClient(),driver, config);
        return this;
    }

    @Override
    public void destory() throws Exception {
        subcribeList.stream().forEach((d) -> {
            d.dispose();
        });
        subcribeList.clear();
    }


}
