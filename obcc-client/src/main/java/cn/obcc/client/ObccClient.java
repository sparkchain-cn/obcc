package cn.obcc.client;

import java.util.HashMap;
import java.util.Map;

import cn.obcc.db.ILocalDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.obcc.config.ObccConfig;
import cn.obcc.driver.IChainDriver;
import cn.obcc.stmt.IDbStatement;
import cn.obcc.stmt.ILedgerStatement;
import cn.obcc.stmt.IStorageStatement;
import cn.obcc.utils.base.StringUtils;

public class ObccClient {

    public static final Logger logger = LoggerFactory.getLogger(ObccClient.class);

    private ObccConfig config;

    private IChainDriver driver;

    private IDbStatement dbStatement;
    private IStorageStatement storageStatement;
    private ILedgerStatement ledgerStatement;

    private ILocalDb localDb;


    private static Map<String, ObccClient> clientMap = new HashMap<String, ObccClient>();

    public static ObccClient get(String clientId, String nodeurl, ObccConfig config) throws Exception {

        if (clientMap.containsKey(clientId)) {
            return clientMap.get(clientId);
        }

        ObccClient client = new ObccClient();
        client.initConfig(clientId, nodeurl, config);
        client.initOrGetDriver();
        clientMap.put(clientId, client);
        return client;

    }

    private void initConfig(String clientId, String nodeurl, ObccConfig config) {
        if (config == null) {
            config = new ObccConfig();
        }

        config.setClientId(clientId);

        if (StringUtils.isNotNullOrEmpty(nodeurl)) {
            config.setNodeUrl(nodeurl);
        }

        this.config = config;
    }

    private ILocalDb initOrGetLocalDb() throws Exception {
        if (this.localDb == null) {
            // if (config.getChain().equals(EChainType.ETHER)) {
            ILocalDb localDb = (ILocalDb) newLocalDb();// new EthChainDriver();
            //driver.init(config);
            this.localDb = localDb;
            // }
        }
        return this.localDb;

    }


    private IChainDriver initOrGetDriver() throws Exception {
        if (this.driver == null) {
            // if (config.getChain().equals(EChainType.ETHER)) {
            IChainDriver driver = (IChainDriver) newDriver();// new EthChainDriver();
            driver.init(config, initOrGetLocalDb());
            this.driver = driver;
            // }
        }
        return this.driver;

    }

    ;

    private Object newLocalDb() {

        String clzName = config.getLocalDbName();
        try {
            return getClass().getClassLoader().loadClass(clzName).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            logger.error("不能加载或实例化LocalDb:" + clzName + ",请引用对应的JAR,并设定好对应的LocalDbr类名。");
        }

        return null;

    }

    private Object newDriver() {

        String clzName = config.getDriverName();
        try {
            return getClass().getClassLoader().loadClass(config.getDriverName()).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            logger.error("不能加载或实例化driver:" + clzName + ",请引用对应的JAR,并设定好对应的Driver类名。");
        }

        return null;

    }

    private ObccClient() {
    }

    private ObccClient(ObccConfig config) {
        this.config = config;
    }

    public void adjustConfig(ObccConfig config) {

    }

    /*********************************************/

    public IDbStatement getDbStatement() throws Exception {
        if (dbStatement == null) {

            dbStatement = (IDbStatement) newStatement(config.getDbStmtName());// new DbStatement();
            dbStatement.init(config, initOrGetLocalDb());
            dbStatement.setDriverManager(this.initOrGetDriver());
        }
        return dbStatement;
    }

    ;

    public IStorageStatement getStorageStatement() throws Exception {
        if (storageStatement == null) {
            storageStatement = (IStorageStatement) newStatement(config.getStorageStmtName());// new StorageStatement();
            storageStatement.init(config, initOrGetLocalDb());
            storageStatement.setDriverManager(this.initOrGetDriver());
        }
        return storageStatement;
    }

    public ILedgerStatement getLedgerStatement() throws Exception {
        if (ledgerStatement == null) {
            ledgerStatement = (ILedgerStatement) newStatement(config.getLedgerStmtName());// new LedgerStatement();
            ledgerStatement.init(config, initOrGetLocalDb());
            ledgerStatement.setDriverManager(this.initOrGetDriver());
        }
        return ledgerStatement;
    }

    private Object newStatement(String clzName) {
        try {
            return getClass().getClassLoader().loadClass(clzName).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            logger.error("不能加载或实例化statement:" + clzName + ",请引用对应的JAR,并设定好对应的statement类名。");
        }

        return null;

    }

    /**************************************************/

    public boolean close() {

        clientMap.remove(this.config.getClientId());

        if (this.dbStatement != null) {
            dbStatement.destory();
        }
        if (this.storageStatement != null) {
            storageStatement.destory();
        }
        if (this.ledgerStatement != null) {
            ledgerStatement.destory();
        }

        if (this.driver != null) {
            this.driver.destory();
        }

        return true;
    }

    public boolean gracefulClose() {
        this.close();
        return true;
    }

}
