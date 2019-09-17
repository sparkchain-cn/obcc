package cn.obcc.driver.eth;

import cn.obcc.config.ObccConfig;
import cn.obcc.db.DbFactory;
import cn.obcc.driver.eth.conn.builder.EthClientBuilderBase;
import cn.obcc.driver.eth.module.*;
import cn.obcc.driver.eth.module.tech.*;
import cn.obcc.driver.module.*;
import cn.obcc.driver.tech.*;
import cn.obcc.driver.module.base.BaseCallbackRegister;
import cn.obcc.driver.ICallbackRegister;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.web3j.protocol.Web3j;

/**
 * 主要用来测试ChainDriver的串通性
 *
 * @author mgicode
 * @version 1.0
 * @ClassName EthChainDriverTest
 * @desc
 * @data 2019/9/4 14:50
 */
public class EthChainDriverTest {

    EthChainDriver chainDriver;

    @BeforeClass
    public void setUp() throws Exception {
        chainDriver = new EthChainDriver();
        ObccConfig config = new ObccConfig();
        config.setNodeUrl("http://47.98.144.109:8745");
        //0x5d8c773055adb77e277b009b513323ab092d004c
        // f89eee0cb710893e3f4bf3a737ac633848c8652a479e32c54376ad3cffa98412
        DbFactory localDb = DbFactory.getInstance(config);
        chainDriver.init(config, localDb);
    }

    @Test
    public void testGetChainClientName() {
        String chainClientName = chainDriver.getChainClientName();
        Assert.assertEquals(chainClientName, "obcc-eth");
    }

    @Test
    public void testGetLocalDb() throws Exception {
        DbFactory dbFactory = chainDriver.getLocalDb();
        Assert.assertNotNull(dbFactory);
        Assert.assertNotNull(dbFactory.getAccountInfoDao());
        Assert.assertNotNull(dbFactory.getContractInfoDao());
        Assert.assertNotNull(dbFactory.getRecordInfoDao());
        Assert.assertNotNull(dbFactory.getTokenInfoDao());
        Assert.assertNotNull(dbFactory.getTxInfoDao());
    }

    @Test
    public void testGetChainClient() throws Exception {
        Web3j web3j = chainDriver.getChainClient();
        Assert.assertNotNull(web3j);
    }

    @Test
    public void testTestGetChainClient() {
    }

    @Test
    public void testGetSpeedAdjuster() throws Exception {
        ISpeedAdjuster<Web3j> handler = chainDriver.getSpeedAdjuster();
        Assert.assertNotNull(handler);
        //  SpeedAdjuster handlerImpl = (SpeedAdjuster) handler;

    }

    @Test
    public void testGetCallbackRegister() throws Exception {
        ICallbackRegister handler = chainDriver.getCallbackRegister();
        Assert.assertNotNull(handler);
        BaseCallbackRegister handlerImpl = (BaseCallbackRegister) handler;
    }

    @Test
    public void testGetContractHandler() throws Exception {
        IContractHandler<Web3j> contractHandler = chainDriver.getContractHandler();
        Assert.assertNotNull(contractHandler);
        EthBaseContractHandler ethContractHandler = (EthBaseContractHandler) contractHandler;

        Assert.assertEquals(ethContractHandler.getDriver(), chainDriver);
        Assert.assertEquals(ethContractHandler.getObccConfig(), chainDriver.getObccConfig());
    }

    @Test
    public void testGetBlockHandler() throws Exception {

        IBlockHandler<Web3j> handler = chainDriver.getBlockHandler();
        Assert.assertNotNull(handler);
        EthBlockHandler handlerImpl = (EthBlockHandler) handler;

        Assert.assertEquals(handlerImpl.getDriver(), chainDriver);
        Assert.assertEquals(handlerImpl.getObccConfig(), chainDriver.getObccConfig());
    }

    @Test
    public void testGetCallbackListener() throws Exception {

        ICallbackListener<Web3j> handler = chainDriver.getCallbackListener();
        Assert.assertNotNull(handler);
        EthCallBackListener handlerImpl = (EthCallBackListener) handler;

        Assert.assertEquals(handlerImpl.getDriver(), chainDriver);
        Assert.assertEquals(handlerImpl.getObccConfig(), chainDriver.getObccConfig());

    }

    @Test
    public void testGetTokenHandler() throws Exception {
        ITokenHandler<Web3j> handler = chainDriver.getTokenHandler();
        Assert.assertNotNull(handler);
        EthTokenHandler handlerImpl = (EthTokenHandler) handler;

        Assert.assertEquals(handlerImpl.getDriver(), chainDriver);
        Assert.assertEquals(handlerImpl.getObccConfig(), chainDriver.getObccConfig());
    }

    @Test
    public void testGetAccountHandler() throws Exception {
        IAccountHandler<Web3j> handler = chainDriver.getAccountHandler();
        Assert.assertNotNull(handler);
        EthAccountHandler handlerImpl = (EthAccountHandler) handler;

        Assert.assertEquals(handlerImpl.getDriver(), chainDriver);
        Assert.assertEquals(handlerImpl.getObccConfig(), chainDriver.getObccConfig());
    }

    @Test
    public void testGetStateMonitor() throws Exception {
        IStateMonitor<Web3j> handler = chainDriver.getStateMonitor();
        Assert.assertNotNull(handler);
        EthStateMonitor handlerImpl = (EthStateMonitor) handler;

        Assert.assertEquals(handlerImpl.getDriver(), chainDriver);
        Assert.assertEquals(handlerImpl.getObccConfig(), chainDriver.getObccConfig());
    }

    @Test
    public void testGetNonceCalculator() throws Exception {
        INonceCalculator<Web3j> handler = chainDriver.getNonceCalculator();
        Assert.assertNotNull(handler);
        EthBaseNonceCalculator handlerImpl = (EthBaseNonceCalculator) handler;

        Assert.assertEquals(handlerImpl.getDriver(), chainDriver);
        Assert.assertEquals(handlerImpl.getObccConfig(), chainDriver.getObccConfig());
    }

    @Test
    public void testGetTxSignature() throws Exception {
        ITxSignatureHandler<Web3j> handler = chainDriver.getTxSignatureHandler();
        Assert.assertNotNull(handler);
        EthTxSignatureHandler handlerImpl = (EthTxSignatureHandler) handler;

        Assert.assertEquals(handlerImpl.getDriver(), chainDriver);
        Assert.assertEquals(handlerImpl.getObccConfig(), chainDriver.getObccConfig());
    }

    @Test
    public void testGetMemoParser() throws Exception {
        IMemoParser<Web3j> handler = chainDriver.getMemoParser();
        Assert.assertNotNull(handler);
        EthBaseMemoParser handlerImpl = (EthBaseMemoParser) handler;

        Assert.assertEquals(handlerImpl.getDriver(), chainDriver);
        Assert.assertEquals(handlerImpl.getObccConfig(), chainDriver.getObccConfig());
    }

    @Test
    public void testGetChainClientBuilderClz() throws Exception {
        Class handler = chainDriver.getChainClientBuilderClz();
        Assert.assertEquals(handler, EthClientBuilderBase.class);

    }


    @AfterClass
    public void destory() {
        chainDriver.destory();
    }

}