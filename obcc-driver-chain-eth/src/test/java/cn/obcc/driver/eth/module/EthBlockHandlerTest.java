package cn.obcc.driver.eth.module;

import cn.obcc.config.ObccConfig;
import cn.obcc.config.ExConfig;
import cn.obcc.db.DbFactory;
import cn.obcc.driver.eth.EthChainDriver;
import cn.obcc.driver.eth.conn.builder.EthClientBuilderTest;
import cn.obcc.vo.driver.BlockInfo;
import cn.obcc.vo.driver.BlockTxInfo;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.web3j.protocol.Web3j;

import java.math.BigInteger;

public class EthBlockHandlerTest {
    public static final Logger logger = LoggerFactory.getLogger(EthClientBuilderTest.class);

    EthBlockHandler ethBlockHandler;
    EthChainDriver chainDriver;

    String srcAddr = "0x5d8c773055adb77e277b009b513323ab092d004c";
    String secret = "f89eee0cb710893e3f4bf3a737ac633848c8652a479e32c54376ad3cffa98412";

    @BeforeClass
    public void setUp() throws Exception {

        chainDriver = new EthChainDriver();
        ObccConfig config = new ObccConfig();
        config.setNodeUrl("http://47.98.144.109:8745");

        DbFactory localDb = DbFactory.getInstance(config);
        chainDriver.init(config, localDb);

        ethBlockHandler = new EthBlockHandler();
        ethBlockHandler.initObccConfig(config, chainDriver);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        chainDriver.destory();
        ethBlockHandler.destory();
    }

    @Test
    public void testInitObccConfig() throws Exception {
        EthChainDriver chainDriver = new EthChainDriver();
        ObccConfig config = new ObccConfig();
        config.setNodeUrl("http://47.98.144.109:8745");

        DbFactory localDb = DbFactory.getInstance(config);
        chainDriver.init(config, localDb);

        EthBlockHandler ethBlockHandler = new EthBlockHandler();
        ethBlockHandler.initObccConfig(config, chainDriver);

        Assert.assertNotNull(ethBlockHandler.getObccConfig());
        Assert.assertNotNull(ethBlockHandler.getDriver());
    }


    @Test
    public void testGetClient() throws Exception {
        Web3j web3j = ethBlockHandler.getClient();
        BigInteger bn2 = web3j.ethBlockNumber().send().getBlockNumber();
        Assert.assertNotNull(bn2);
    }


    @Test
    public void testGetInterval() throws Exception {
        Web3j web3j = ethBlockHandler.getClient();
        Assert.assertEquals((long) ethBlockHandler.getInterval(), 10 * 1000L);
    }

    @Test
    public void testGetBlockHeight() throws Exception {
        Web3j web3j = ethBlockHandler.getClient();
        BigInteger bn2 = web3j.ethBlockNumber().send().getBlockNumber();
        long bn3 = (Long) ethBlockHandler.getBlockHeight();
        Assert.assertEquals(bn2.longValue(), bn3);
        logger.debug("block height:" + bn2.longValue());
    }

    @Test
    public void testGetBlockInfo() throws Exception {
        Web3j web3j = ethBlockHandler.getClient();
        BlockInfo blockInfo = ethBlockHandler.getBlockInfo(ethBlockHandler.getBlockHeight(), new ExConfig());
        Assert.assertNotNull(blockInfo);

        logger.debug("block Info:" + JSON.toJSONString(blockInfo));
    }

    @Test
    public void testGetBlockTxInfo() throws Exception {
        Web3j web3j = ethBlockHandler.getClient();
        BlockInfo blockInfo = ethBlockHandler.getBlockInfo(ethBlockHandler.getBlockHeight(), new ExConfig());
        Assert.assertNotNull(blockInfo);

        if (!blockInfo.getTransactions().isEmpty()) {
            String hash = blockInfo.getTransactions().get(0);
            try {
                BlockTxInfo txInfo = ethBlockHandler.getBlockTxInfo(hash, new ExConfig());
                Assert.assertNotNull(txInfo);
                Assert.assertEquals(txInfo.getHash(), hash);
                logger.debug("block Info:" + JSON.toJSONString(txInfo));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        blockInfo.getTransactions().forEach((t) -> {
//            try {
//                BlockTxInfo txInfo = ethBlockHandler.getBlockTxInfo(t, new ExProps());
//                Assert.assertNotNull(txInfo);
//                Assert.assertEquals(txInfo.getHash(), t);
//                logger.debug("block Info:" + JSON.toJSONString(txInfo));
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
        logger.debug("block Info:" + JSON.toJSONString(blockInfo));

    }
}