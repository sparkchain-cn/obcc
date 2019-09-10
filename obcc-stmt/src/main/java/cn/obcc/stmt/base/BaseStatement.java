package cn.obcc.stmt.base;

import cn.obcc.config.ObccConfig;
import cn.obcc.db.DbFactory;
import cn.obcc.db.base.JdbcDao;
import cn.obcc.driver.IChainDriver;
import cn.obcc.stmt.IStatement;

public class BaseStatement implements IStatement {

    protected ObccConfig config;
    protected IChainDriver driver;

    protected DbFactory localDb;

    @Override
    public void init(ObccConfig config, DbFactory db, IChainDriver driver) {
        this.config = config;
        this.localDb = db;
        this.driver = driver;
    }


    @Override
    public DbFactory getLocalDb() {
        return localDb;
    }

    @Override
    public void destory() {
    }


    public IChainDriver getDriver() {
        return driver;
    }
}
