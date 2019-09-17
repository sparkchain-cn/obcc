package cn.obcc.client;

import java.util.HashMap;
import java.util.Map;

import cn.obcc.client.config.ConfigUtils;
import cn.obcc.db.DbFactory;
import cn.obcc.db.utils.BeanUtils;
import cn.obcc.stmt.IStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.obcc.config.ObccConfig;
import cn.obcc.driver.IChainDriver;

public class ObccClient {

    public static final Logger logger = LoggerFactory.getLogger(ObccClient.class);

    private ObccConfig config;

    private DbFactory localDb;
    private IChainDriver driver;

    private Map<String, IStatement> statementMap = new HashMap<>();

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
        this.config = ConfigUtils.initConfig(clientId, nodeurl, config);
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
            IChainDriver driver = (IChainDriver) BeanUtils.newInstance(config.getDriverName());
            driver.init(config, initOrGetLocalDb());
            this.driver = driver;
        }
        return this.driver;

    }

    private ObccClient() {
    }

    private ObccClient(ObccConfig config) {
        this.config = config;
    }


    public ObccClient adjustConfig(ObccConfig config) {
        return this;

    }

    /*********************************************/

    public <T> T getStatement(Class<? extends IStatement> clz) throws Exception {
        if (!statementMap.containsKey(clz.getSimpleName())) {
            statementMap.put(clz.getSimpleName(), clz.newInstance());
        }
        IStatement statement = statementMap.get(clz.getSimpleName());
        statement.init(config, initOrGetLocalDb(), initOrGetDriver());

        return (T) statement;
    }


    /**************************************************/

    public boolean close() {
        clientMap.remove(this.config.getClientId());
        statementMap.forEach((key, value) -> {
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
