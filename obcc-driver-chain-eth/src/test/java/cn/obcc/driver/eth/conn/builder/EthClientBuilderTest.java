package cn.obcc.driver.eth.conn.builder;

import cn.obcc.config.ObccConfig;
import cn.obcc.connect.pool.fn.ClientAdviceFn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.web3j.protocol.Web3j;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class EthClientBuilderTest {
    public static final Logger logger = LoggerFactory.getLogger(EthClientBuilderTest.class);

    //    0x5d8c773055adb77e277b009b513323ab092d004c
//    f89eee0cb710893e3f4bf3a737ac633848c8652a479e32c54376ad3cffa9841
    @Test
    public void testInit() throws Exception {
        EthClientBuilder builder = new EthClientBuilder();
        builder.init("http://47.98.144.109:8745", null);
        Web3j web3j = builder.newNativeClient();
        Assert.assertNull(builder.getCallback());
        BigInteger bn = web3j.ethBlockNumber().send().getBlockNumber();
        Assert.assertNotNull(bn);

        List<Integer> counts = new ArrayList<Integer>() {{
            add(0);
            add(0);
            add(0);
        }};

        builder.init("http://47.98.144.109:8745", new ClientAdviceFn() {
            @Override
            public void requestCountInc() {
                counts.set(0, counts.get(0) + 1);
                logger.debug(" request  once again.");
            }

            @Override
            public void ioErrorCountInc() {
                counts.set(1, counts.get(1) + 1);
                logger.debug(" request  io Error.");
            }

            @Override
            public void bizErrorCountInc() {

            }
        });

        web3j = builder.newNativeClient();
        Assert.assertNotNull(builder.getCallback());
        BigInteger bn1 = web3j.ethBlockNumber().send().getBlockNumber();
        Assert.assertNotNull(bn1);
        Assert.assertEquals(1, counts.get(0).intValue());
        web3j.ethBlockNumber().send().getBlockNumber();
        Assert.assertEquals(2, counts.get(0).intValue());
        web3j.ethBlockNumber().send().getBlockNumber();
        Assert.assertEquals(3, counts.get(0).intValue());

    }

    @Test
    public void testGetCallback() {
    }

    @Test
    public void testSetCallback() throws Exception {
        EthClientBuilder builder = new EthClientBuilder();
        builder.init("http://47.98.144.109:8745", null);
        Web3j web3j = builder.newNativeClient();
        Assert.assertNull(builder.getCallback());

        List<Integer> counts = new ArrayList<Integer>() {{
            add(0);
            add(0);
            add(0);
        }};

        ClientAdviceFn fn = new ClientAdviceFn() {
            @Override
            public void requestCountInc() {
                counts.set(0, counts.get(0) + 1);
                logger.debug(" request  once again.");
            }

            @Override
            public void ioErrorCountInc() {
                counts.set(1, counts.get(1) + 1);
                logger.debug(" request  io Error.");
            }

            @Override
            public void bizErrorCountInc() {

            }
        };
        builder.setCallback(fn);

        Assert.assertNotNull(builder.getCallback());

        BigInteger bn1 = web3j.ethBlockNumber().send().getBlockNumber();
        Assert.assertNotNull(bn1);
        Assert.assertEquals(1, counts.get(0).intValue());
        web3j.ethBlockNumber().send().getBlockNumber();
        Assert.assertEquals(2, counts.get(0).intValue());
        web3j.ethBlockNumber().send().getBlockNumber();
        Assert.assertEquals(3, counts.get(0).intValue());
    }


    @Test
    public void testNewNativeClient() throws Exception {

        EthClientBuilder builder = new EthClientBuilder();
        builder.init("http://47.98.144.109:8745", null);
        Web3j web3j = builder.newNativeClient();
        BigInteger bn1 = web3j.ethBlockNumber().send().getBlockNumber();
        Assert.assertNotNull(bn1);

        builder.init("ws://47.98.144.109:8756", null);
        Web3j web3j1 = builder.newNativeClient();
        BigInteger bn2 = web3j.ethBlockNumber().send().getBlockNumber();
        Assert.assertNotNull(bn2);
    }


    @Test
    public void testIsDead() throws Exception {
        EthClientBuilder builder = new EthClientBuilder();
        builder.init("ws://47.98.144.109:8756", null);
        Web3j web3j = builder.newNativeClient();

        BigInteger bn2 = web3j.ethBlockNumber().send().getBlockNumber();
        Assert.assertNotNull(bn2);

        web3j.shutdown();
        boolean f = builder.isDead(web3j);
        Assert.assertEquals(true, f);
    }

    @Test
    public void testIsOpen() throws Exception {
        EthClientBuilder builder = new EthClientBuilder();
        builder.init("ws://47.98.144.109:8756", null);
        Web3j web3j = builder.newNativeClient();

        BigInteger bn2 = web3j.ethBlockNumber().send().getBlockNumber();
        Assert.assertNotNull(bn2);

        boolean f = builder.isOpen(web3j);
        Assert.assertEquals(true, f);
    }

    @Test
    public void testOpen() throws Exception {
        EthClientBuilder builder = new EthClientBuilder();
        builder.init("ws://47.98.144.109:8756", null);
        Web3j web3j = builder.newNativeClient();

        BigInteger bn2 = web3j.ethBlockNumber().send().getBlockNumber();
        Assert.assertNotNull(bn2);

        boolean flag = builder.open(web3j);
        Assert.assertEquals(true, flag);

        boolean f = builder.isOpen(web3j);
        Assert.assertEquals(true, f);
    }


    @Test
    public void testClose() throws Exception {
        EthClientBuilder builder = new EthClientBuilder();
        builder.init("ws://47.98.144.109:8756", null);
        Web3j web3j = builder.newNativeClient();


        BigInteger bn2 = web3j.ethBlockNumber().send().getBlockNumber();
        Assert.assertNotNull(bn2);

        builder.close(web3j);
        Assert.assertEquals(builder.isDead(web3j), true);
        Assert.assertEquals(builder.isOpen(web3j), false);
    }


    @Test
    public void testGetClient() throws Exception {
        ObccConfig config = new ObccConfig();
        config.setNodeUrl("ws://47.98.144.109:8756");
        Web3j web3j = EthClientBuilder.getClient(config);

        BigInteger bn2 = web3j.ethBlockNumber().send().getBlockNumber();
        Assert.assertNotNull(bn2);

    }

    @Test
    public void testTestGetClient() {
    }
}