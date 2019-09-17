package cn.obcc.db;

import cn.obcc.config.ObccConfig;
import cn.obcc.db.dao.*;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName DbFactory
 * @desc TODO
 * @date 2019/8/28 0028  18:03
 **/
public class DbFactory {
    ObccConfig config;

    static DbFactory instance;
    AccountInfoDaoBase accountInfoDao;
    ContractInfoDaoBase contractInfoDao;
    TokenInfoDaoBase tokenInfoDao;
    RecordInfoDaoBase recordInfoDao;
    TxInfoDaoBase txInfoDao;
    TableInfoDaoBase tableInfoDao;


    private DbFactory(ObccConfig config) throws Exception {
        this.config = config;
        initDao();
    }

    private void initDao() throws Exception {
        accountInfoDao = new AccountInfoDaoBase();
        accountInfoDao.init(config);
        accountInfoDao.createTable();
        contractInfoDao = new ContractInfoDaoBase();
        contractInfoDao.init(config);
        contractInfoDao.createTable();
        tokenInfoDao = new TokenInfoDaoBase();
        tokenInfoDao.init(config);
        tokenInfoDao.createTable();
        recordInfoDao = new RecordInfoDaoBase();
        recordInfoDao.init(config);
        recordInfoDao.createTable();
        txInfoDao = new TxInfoDaoBase();
        txInfoDao.init(config);
        txInfoDao.createTable();
        tableInfoDao.init(config);
        tableInfoDao.createTable();

    }

    public static DbFactory getInstance(ObccConfig config) throws Exception {
        if (instance == null) {
            DbFactory.instance = new DbFactory(config);
        }
        return instance;
    }

    public AccountInfoDaoBase getAccountInfoDao() {
        return accountInfoDao;
    }

    public ContractInfoDaoBase getContractInfoDao() {
        return contractInfoDao;
    }

    public RecordInfoDaoBase getRecordInfoDao() {
        return recordInfoDao;
    }

    public TokenInfoDaoBase getTokenInfoDao() {
        return tokenInfoDao;
    }

    public TxInfoDaoBase getTxInfoDao() {
        return txInfoDao;
    }

    public TableInfoDaoBase getTableInfoDao() {
        return tableInfoDao;
    }

}
