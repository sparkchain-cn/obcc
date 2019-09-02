package cn.obcc.db;

import cn.obcc.config.ObccConfig;
import cn.obcc.db.dao.*;
import cn.obcc.vo.driver.ContractInfo;

import java.io.File;

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
    AccountInfoDao accountInfoDao;
    ContractInfoDao contractInfoDao;
    TokenInfoDao tokenInfoDao;
    RecordInfoDao recordInfoDao;
    TxInfoDao txInfoDao;


    private DbFactory(ObccConfig config) throws Exception {
        this.config = config;
        initDao();
    }

    private void initDao() throws Exception {
        accountInfoDao = new AccountInfoDao();
        accountInfoDao.init(config);
        accountInfoDao.createTable();
        contractInfoDao = new ContractInfoDao();
        contractInfoDao.init(config);
        contractInfoDao.createTable();
        tokenInfoDao = new TokenInfoDao();
        tokenInfoDao.init(config);
        tokenInfoDao.createTable();
        recordInfoDao = new RecordInfoDao();
        recordInfoDao.init(config);
        recordInfoDao.createTable();
        txInfoDao=new TxInfoDao();
        txInfoDao.createTable();

    }

    public static DbFactory getInstance(ObccConfig config) throws Exception {
        if (instance == null) {
            DbFactory.instance = new DbFactory(config);
        }
        return instance;
    }

    public AccountInfoDao getAccountInfoDao() {
        return accountInfoDao;
    }

    public ContractInfoDao getContractInfoDao() {
        return contractInfoDao;
    }

    public RecordInfoDao getRecordInfoDao() {
        return recordInfoDao;
    }

    public TokenInfoDao getTokenInfoDao() {
        return tokenInfoDao;
    }

    public TxInfoDao getTxInfoDao(){return txInfoDao;}
}
