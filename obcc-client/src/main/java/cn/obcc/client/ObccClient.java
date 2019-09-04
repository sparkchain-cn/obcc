package cn.obcc.client;

import java.util.HashMap;
import java.util.Map;

import cn.obcc.db.DbFactory;
import cn.obcc.db.base.JdbcDao;
import cn.obcc.stmt.IStatement;
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

    private DbFactory localDb;
    private IChainDriver driver;

    private Map<String,IStatement> statementMap=new HashMap<>();



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

    private DbFactory initOrGetLocalDb() throws Exception {
        if (this.localDb == null) {
            DbFactory localDb = DbFactory.getInstance(config);
            this.localDb = localDb;
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

    public <T> T getStatement(Class<? extends IStatement> clz) throws Exception{
           if(!statementMap.containsKey(clz.getSimpleName())){
               statementMap.put(clz.getSimpleName(),clz.newInstance());
           }
           return (T)statementMap.get(clz.getSimpleName());

    }


    /**************************************************/

    public boolean close() {

        clientMap.remove(this.config.getClientId());

        statementMap.forEach((key,value)->{
            value.destory();
        });
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
