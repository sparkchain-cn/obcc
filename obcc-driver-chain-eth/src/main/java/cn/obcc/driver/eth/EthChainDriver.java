package cn.obcc.driver.eth;

import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.eth.module.*;
import cn.obcc.driver.eth.module.tech.EthCallBackListener;
import cn.obcc.driver.eth.module.tech.EthMemoParser;
import cn.obcc.driver.module.*;
import cn.obcc.driver.tech.IMemoParser;
import cn.obcc.exception.enums.EDriverHandlerType;
import org.web3j.protocol.Web3j;

import cn.obcc.connect.builder.ChainClientBuilder;
import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.base.BaseChainDriver;
import cn.obcc.driver.eth.conn.builder.EthClientBuilder;
import cn.obcc.driver.eth.module.tech.EthNonceCalculator;
import cn.obcc.driver.eth.module.tech.EthStateMonitor;
import cn.obcc.driver.tech.INonceCalculator;
import cn.obcc.driver.tech.IStateMonitor;
import cn.obcc.driver.tech.ITxSignatureHandler;

import java.util.HashMap;
import java.util.Map;

public class EthChainDriver extends BaseChainDriver<Web3j> implements IChainDriver<Web3j> {

    @Override
    protected Map<String, Class<? extends BaseHandler>> getHanlderClzes() {
        return new HashMap<String, Class<? extends BaseHandler>>() {{
            put(EDriverHandlerType.ContractHandler.getName(), EthContractHandler.class);
            put(EDriverHandlerType.BlockHandler.getName(), EthBlockHandler.class);
            put(EDriverHandlerType.CallbackListener.getName(), EthCallBackListener.class);
            put(EDriverHandlerType.TokenHandler.getName(), EthTokenHandler.class);
            put(EDriverHandlerType.AccountHandler.getName(), EthAccountHandler.class);
            put(EDriverHandlerType.StateMonitor.getName(), EthStateMonitor.class);
            put(EDriverHandlerType.NonceCalculator.getName(), EthNonceCalculator.class);
            put(EDriverHandlerType.TxSignatureHandler.getName(), EthTxSignatureHandler.class);
            put(EDriverHandlerType.MemoParser.getName(), EthMemoParser.class);
        }};
    }

    public EthChainDriver() {
        super();
    }


    public Class<? extends ChainClientBuilder<Web3j>> getChainClientBuilderClz() throws Exception {
        return EthClientBuilder.class;
    }


}
