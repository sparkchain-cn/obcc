package cn.obcc.driver.eth.module.tech;

import cn.obcc.config.ObccConfig;
import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.IDriverHandler;
import cn.obcc.driver.tech.base.CallbackHandler;
import cn.obcc.driver.eth.module.tech.callback.EthTxMonitor;
import cn.obcc.driver.handler.ICallbackHandler;
import io.reactivex.disposables.Disposable;
import org.web3j.protocol.Web3j;

import java.util.List;

public class EthCallBackHandler extends CallbackHandler<Web3j> implements ICallbackHandler<Web3j> {

    List<Disposable> subcribeList;

    @Override
    public IDriverHandler init(ObccConfig config, IChainDriver<Web3j> driver) throws Exception {
        super.init(config, driver);
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
