package cn.obcc.driver.eth;

import cn.obcc.driver.tech.IMemoParser;
import org.web3j.protocol.Web3j;

import cn.obcc.connect.builder.ChainClientBuilder;
import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.base.BaseChainDriver;
import cn.obcc.driver.eth.conn.builder.EthClientBuilder;
import cn.obcc.driver.eth.module.EthAccountHandler;
import cn.obcc.driver.eth.module.EthContractHandler;
import cn.obcc.driver.eth.module.tech.EthNonceCalculator;
import cn.obcc.driver.eth.module.tech.EthStateMonitor;
import cn.obcc.driver.module.IAccountHandler;
import cn.obcc.driver.module.IBlockHandler;
import cn.obcc.driver.module.IContractHandler;
import cn.obcc.driver.module.ITokenHandler;
import cn.obcc.driver.tech.INonceCalculator;
import cn.obcc.driver.tech.IStateMonitor;
import cn.obcc.driver.tech.ITxSignature;

public class EthChainDriver extends BaseChainDriver<Web3j> implements IChainDriver<Web3j> {

    public EthChainDriver() {
        super();
    }

    @Override
    public IContractHandler getContractHandler() throws Exception {
        EthContractHandler handler = new EthContractHandler();
        handler.initObccConfig(obccConfig, this);
        return handler;
    }

    @Override
    public IBlockHandler<Web3j> getBlockHandler() throws Exception {
        return null;
    }

    @Override
    public ITokenHandler<Web3j> getTokenHandler() throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IAccountHandler getAccountHandler() throws Exception {
        EthAccountHandler handler = new EthAccountHandler();
        handler.initObccConfig(obccConfig, this);
        return handler;
    }

    @Override
    public IStateMonitor getStateMonitor() throws Exception {
        EthStateMonitor handler = new EthStateMonitor();
        handler.initObccConfig(obccConfig, this);
        return handler;
    }

    @Override
    public INonceCalculator getNonceCalculator() throws Exception {
        EthNonceCalculator handler = new EthNonceCalculator();
        handler.initObccConfig(obccConfig, this);
        return handler;
    }

    @Override
    public ITxSignature<Web3j> getTxSignature() throws Exception {
        return null;
    }

    @Override
    public IMemoParser<Web3j> getMemoParser() throws Exception {
        return null;
    }


    public Class<? extends ChainClientBuilder<Web3j>> getChainClientBuilderClz() throws Exception {
        return EthClientBuilder.class;
    }


}
