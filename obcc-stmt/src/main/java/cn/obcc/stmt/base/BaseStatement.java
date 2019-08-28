package cn.obcc.stmt.base;

import cn.obcc.config.ObccConfig;
import cn.obcc.db.DbFactory;
import cn.obcc.db.base.JdbcDao;
import cn.obcc.driver.IChainDriver;
import cn.obcc.stmt.IStatement;

public class BaseStatement implements IStatement {

    protected ObccConfig config;
    protected IChainDriver driverManager;

    protected DbFactory localDb;

    @Override
    public void init(ObccConfig config, DbFactory db) {
        this.config = config;
        this.localDb = db;
    }

    @Override
    public void setDriverManager(IChainDriver driverManager) {
        this.driverManager = driverManager;
    }

    @Override
    public DbFactory getLocalDb() {
        return localDb;
    }

    @Override
    public void destory() {


    }

}
