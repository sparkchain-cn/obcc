package cn.obcc.config;

import java.util.ArrayList;
import java.util.List;

import cn.obcc.config.conn.pool.ConnPoolConfig;
import cn.obcc.exception.enums.*;

public class ObccConfig {

    private String clientId = "obcc";

//    private String dbStmtName = "cn.obcc.stmt.db.DbStatement";
//    private String storageStmtName = "cn.obcc.stmt.storage.StorageStatement";
//    private String ledgerStmtName = "cn.obcc.stmt.ledger.LedgerStatement";



    private String localDbName = "cn.obcc.db.SqliteDb";
    private String speedAdjusterName = "cn.obcc.driver.adjuster.SpeedAdjuster";

    private String driverName = "cn.obcc.driver.eth.EthChainDriver";

    private ConnPoolConfig pollconfig;

    private String nodeUrl = "localhost:8545";

    private EChainType chain = EChainType.ETHER;

    private String dbConn = "d:/obcc/obcc.dbf";
    private String dbUserName = "root";
    private String dbPassword = "123456";

    private ENonceStrategy nonceStrategy = ENonceStrategy.Memory;
    private EMemoStrategy memoStrategy = EMemoStrategy.JSON;
    private ECallbackStrategy callbackStrategy = ECallbackStrategy.Subscribe;
    private EContractType contractType = EContractType.SOLC;

    private String solcPath = "";
    private String tempPath = "";
    private String jdbcTemplateName = "cn.obcc.db.sqlite.SqliteJdbcTemplate";

    private String memoPre = "&&spc";

    private List<String> uuids = new ArrayList<>();

    /**
     * 初始化账号(创世账号)
     */
    private String geneAccount = "0x787172b7f5e6465a06a699b2a88bb1143a01e138";
    /**
     * 账号私钥
     */
    private String genePrivateKey = "d66b74561cf0d96766e527122085bb93e777cc758a3a6f485598325cf315209b";

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getNodeUrl() {
        return nodeUrl;
    }

    public void setNodeUrl(String nodeUrl) {
        this.nodeUrl = nodeUrl;
    }

    public EChainType getChain() {
        return chain;
    }

    public void setChain(EChainType chain) {
        this.chain = chain;
    }

//    public String getDbStmtName() {
//        return dbStmtName;
//    }
//
//    public void setDbStmtName(String dbStmtName) {
//        this.dbStmtName = dbStmtName;
//    }
//
//    public String getStorageStmtName() {
//        return storageStmtName;
//    }
//
//    public void setStorageStmtName(String storageStmtName) {
//        this.storageStmtName = storageStmtName;
//    }
//
//    public String getLedgerStmtName() {
//        return ledgerStmtName;
//    }
//
//    public void setLedgerStmtName(String ledgerStmtName) {
//        this.ledgerStmtName = ledgerStmtName;
//    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public ConnPoolConfig getPollconfig() {
        return pollconfig;
    }

    public void setPollconfig(ConnPoolConfig pollconfig) {
        this.pollconfig = pollconfig;
    }


    public String getSpeedAdjusterName() {
        return speedAdjusterName;
    }

    public void setSpeedAdjusterName(String speedAdjusterName) {
        this.speedAdjusterName = speedAdjusterName;
    }

    public ENonceStrategy getNonceStrategy() {
        return nonceStrategy;
    }

    public void setNonceStrategy(ENonceStrategy nonceStrategy) {
        this.nonceStrategy = nonceStrategy;
    }

    public EMemoStrategy getMemoStrategy() {
        return memoStrategy;
    }

    public void setMemoStrategy(EMemoStrategy memoStrategy) {
        this.memoStrategy = memoStrategy;
    }

    public String getMemoPre() {
        return memoPre;
    }

    public void setMemoPre(String memoPre) {
        this.memoPre = memoPre;
    }

    public EContractType getContractType() {
        return contractType;
    }

    public void setContractType(EContractType contractType) {
        this.contractType = contractType;
    }

    public String getSolcPath() {
        return solcPath;
    }

    public void setSolcPath(String solcPath) {
        this.solcPath = solcPath;
    }

    public String getTempPath() {
        return tempPath;
    }

    public void setTempPath(String tempPath) {
        this.tempPath = tempPath;
    }

    public String getLocalDbName() {
        return localDbName;
    }

    public void setLocalDbName(String localDbName) {
        this.localDbName = localDbName;
    }

    public String getDbConn() {
        return dbConn;
    }

    public void setDbConn(String dbConn) {
        this.dbConn = dbConn;
    }

    public String getDbUserName() {
        return dbUserName;
    }

    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public ECallbackStrategy getCallbackStrategy() {
        return callbackStrategy;
    }

    public void setCallbackStrategy(ECallbackStrategy callbackStrategy) {
        this.callbackStrategy = callbackStrategy;
    }

    public String getJdbcTemplateName() {
        return jdbcTemplateName;
    }


    public void setJdbcTemplateName(String jdbcTemplateName) {
        this.jdbcTemplateName = jdbcTemplateName;
    }
}
