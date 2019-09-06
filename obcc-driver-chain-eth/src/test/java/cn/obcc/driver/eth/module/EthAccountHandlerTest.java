package cn.obcc.driver.eth.module;

import cn.obcc.config.ExProps;
import cn.obcc.config.ObccConfig;
import cn.obcc.db.DbFactory;
import cn.obcc.db.utils.BeanUtil;
import cn.obcc.driver.eth.EthChainDriver;
import cn.obcc.driver.eth.conn.builder.EthClientBuilderTest;
import cn.obcc.driver.module.fn.IUpchainFn;
import cn.obcc.driver.vo.Account;
import cn.obcc.driver.vo.BizTxInfo;
import cn.obcc.driver.vo.ChainPipe;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.exception.enums.EChainTxType;
import cn.obcc.exception.enums.ETransferStatus;
import cn.obcc.exception.enums.EUpchainType;
import cn.obcc.utils.base.DateUtils;
import cn.obcc.uuid.UuidUtils;
import cn.obcc.vo.driver.AccountInfo;
import cn.obcc.vo.driver.BlockTxInfo;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.*;

import static org.testng.Assert.*;

public class EthAccountHandlerTest {
    public static final Logger logger = LoggerFactory.getLogger(EthAccountHandlerTest.class);

    EthAccountHandler ethAccountHandler;
    EthChainDriver chainDriver;

    String srcAddr = "0x5d8c773055adb77e277b009b513323ab092d004c";
    String secret = "f89eee0cb710893e3f4bf3a737ac633848c8652a479e32c54376ad3cffa98412";

    String bizId;
    String destAddr;
    String destSecret;
    BlockTxInfo txInfo;


    String hash;
    String hash2;

    @BeforeClass
    public void setUp() throws Exception {
        chainDriver = new EthChainDriver();
        ObccConfig config = new ObccConfig();
        config.setNodeUrl("http://47.98.144.109:8745");

        DbFactory localDb = DbFactory.getInstance(config);
        chainDriver.init(config, localDb);

        ethAccountHandler = (EthAccountHandler) chainDriver.getAccountHandler();
    }


    @Test
    public void testCreateAccount() throws Exception {
        Account account = ethAccountHandler.createAccount();
        destAddr = account.getAddress();
        destSecret = account.getSecret();

        Assert.assertNotNull(destAddr);
        Assert.assertNotNull(destSecret);
        boolean f1 = ethAccountHandler.checkAccount(new SrcAccount() {{
            setAccount(destAddr);
            setSecret(destSecret);
        }}, new ExProps());
        Assert.assertTrue(f1);
    }

    @Test(dependsOnMethods = {"testCreateAccount"})
    public void testTransfer() throws Exception {

        String bizId = UuidUtils.get() + "";
        SrcAccount account = new SrcAccount() {{
            setAccount(srcAddr);
            setSecret(secret);
        }};
        String amount = "0.0000001";
        String destAddr = this.destAddr;
        ExProps config = new ExProps();
        IUpchainFn<BlockTxInfo> callback = new IUpchainFn<BlockTxInfo>() {
            @Override
            public void exec(String bizId, String hash, EUpchainType upchainType, ETransferStatus state, BlockTxInfo resp) throws Exception {
                Assert.assertNotNull(bizId);
                logger.debug(bizId + "," + JSON.toJSONString(resp));
                Assert.assertEquals(ethAccountHandler.getBalance(destAddr, new ExProps()), amount);
                hash2 = hash;
            }
        };
        String sphash = ethAccountHandler.transfer(bizId, account, amount, destAddr, config, callback);
        logger.debug(sphash);
        Assert.assertNotNull(sphash);


        DateUtils.sleep(40 * 1000);
        Assert.assertEquals(ethAccountHandler.getBalance(destAddr, new ExProps()), amount);
    }

    @Test
    public void testDoTransfer() throws Exception {

        String bizId = UuidUtils.get() + "";
        this.bizId = bizId;
        SrcAccount account = new SrcAccount() {{
            setAccount(srcAddr);
            setSecret(secret);
        }};
        String amount = "0.0000001";
        String destAddr = this.destAddr;
        ExProps config = new ExProps();
        IUpchainFn<BlockTxInfo> callback = new IUpchainFn<BlockTxInfo>() {
            @Override
            public void exec(String bizId, String hash, EUpchainType upchainType, ETransferStatus state, BlockTxInfo resp) throws Exception {
                Assert.assertNotNull(bizId);
                logger.debug(bizId + "," + JSON.toJSONString(resp));
                Assert.assertEquals(ethAccountHandler.getBalance(destAddr, new ExProps()), amount);
            }
        };

        ChainPipe pipe = new ChainPipe();

        pipe.setChainCode(ethAccountHandler.getObccConfig().getChain().getName());
        pipe.setBizId(bizId);
        pipe.setTxType(EChainTxType.Orign);
        pipe.setAccount(account);

        pipe.setDestAddr(destAddr);
        pipe.setAmount(amount);
        pipe.setConfig(new ExProps());
        pipe.setFn(callback);

        String hash = ethAccountHandler.doTransfer(pipe);
        this.hash = hash;
        logger.debug(hash);
        Assert.assertNotNull(hash);

        DateUtils.sleep(40 * 1000);
        BlockTxInfo txInfo = ethAccountHandler.getTxByHash(hash, new ExProps());
        this.txInfo = txInfo;
        Assert.assertNotNull(txInfo);
        Assert.assertEquals(ethAccountHandler.getBalance(destAddr, new ExProps()), amount);
        Assert.assertEquals(txInfo.getAmount(), amount);
    }

    @Test(dependsOnMethods = {"testDoTransfer"})
    public void testGetTxByBizId() throws Exception {
        BizTxInfo info = ethAccountHandler.getTxByBizId(this.bizId, new ExProps());
        Assert.assertNotNull(info);
        Assert.assertNotNull(info.getRecordInfos().get(0));
        Assert.assertEquals(info.getRecordInfos().get(0).getAmount(), txInfo.getAmount());
    }

    @Test(dependsOnMethods = {"testDoTransfer"})
    public void testGetTxByHashs() throws Exception {
        //testGetTxByBizId和testGetTxByHashs相同
        BizTxInfo info = ethAccountHandler.getTxByHashs(this.hash, new ExProps());
        Assert.assertNotNull(info);
        Assert.assertNotNull(info.getRecordInfos().get(0));
        Assert.assertEquals(info.getRecordInfos().get(0).getAmount(), txInfo.getAmount());
    }

    @Test(dependsOnMethods = {"testDoTransfer"})
    public void testGetTxByHash() throws Exception {
        BlockTxInfo info = ethAccountHandler.getTxByHash(this.hash, new ExProps());
        Assert.assertNotNull(info);
        Assert.assertEquals(info.getAmount(), txInfo.getAmount());
    }

    @Test(dependsOnMethods = {"testDoTransfer"})
    public void testGetBalance() throws Exception {

        String balance = ethAccountHandler.getBalance(this.destAddr, new ExProps());
        Assert.assertNotNull(balance);

    }

    @Test
    public void testTestCreateAccount() throws Exception {
        String bizId = UuidUtils.get() + "";
        AccountInfo accountInfo = ethAccountHandler.createAccount(bizId, "user1", "user1");

        Assert.assertNotNull(accountInfo.getAddress());
        Assert.assertNotNull(accountInfo.getPassword());

        Assert.assertEquals(accountInfo.getUserName(), "user1");
    }

    @Test
    public void testCalGas() {
    }

    @Test
    public void testCheckAccount() throws Exception {
        boolean f1 = ethAccountHandler.checkAccount(new SrcAccount() {{
            setAccount(destAddr);
            setSecret(destSecret);
        }}, new ExProps());

        Assert.assertTrue(f1);

        boolean f2 = ethAccountHandler.checkAccount(new SrcAccount() {{
            setAccount(destAddr);
            setSecret(secret);
        }}, new ExProps());

        Assert.assertTrue(!f2);
    }


    @AfterClass
    public void tearDown() {
        chainDriver.destory();
    }

}