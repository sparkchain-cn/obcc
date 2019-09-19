package cn.obcc.driver.eth;

import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.eth.module.*;
import cn.obcc.driver.eth.module.tech.EthCallBackHandler;
import cn.obcc.driver.eth.module.tech.EthBaseMemoParser;
import cn.obcc.enums.EDriverHandlerType;
import org.web3j.protocol.Web3j;

import cn.obcc.connect.builder.ChainClientBuilder;
import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.base.BaseChainDriver;
import cn.obcc.driver.eth.conn.builder.EthClientBuilder;
import cn.obcc.driver.eth.module.tech.EthBaseNonceCalculator;
import cn.obcc.driver.eth.module.tech.EthStateMonitor;

import java.util.HashMap;
import java.util.Map;

public class EthChainDriver extends BaseChainDriver<Web3j> implements IChainDriver<Web3j> {

    @Override
    protected Map<String, Class<? extends BaseHandler>> getHanlderClzes() {
        return new HashMap<String, Class<? extends BaseHandler>>() {{
            put(EDriverHandlerType.ContractHandler.getName(), EthBaseContractHandler.class);
            put(EDriverHandlerType.BlockHandler.getName(), EthBlockHandler.class);
            put(EDriverHandlerType.CallbackListener.getName(), EthCallBackHandler.class);
            put(EDriverHandlerType.TokenHandler.getName(), EthTokenHandler.class);
            put(EDriverHandlerType.AccountHandler.getName(), EthAccountHandler.class);
            put(EDriverHandlerType.StateMonitor.getName(), EthStateMonitor.class);
            put(EDriverHandlerType.NonceCalculator.getName(), EthBaseNonceCalculator.class);
            put(EDriverHandlerType.TxSignatureHandler.getName(), EthSignatureHandler.class);
            put(EDriverHandlerType.MemoParser.getName(), EthBaseMemoParser.class);

        }};
    }

    public EthChainDriver() {
        super();
    }

    @Override
    public Class<? extends ChainClientBuilder<Web3j>> getChainClientBuilderClz() throws Exception {
        return EthClientBuilder.class;
    }


}
