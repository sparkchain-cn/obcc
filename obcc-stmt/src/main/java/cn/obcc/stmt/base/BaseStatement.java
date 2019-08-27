package cn.obcc.stmt.base;

import java.sql.DriverManager;

import cn.obcc.config.ObccConfig;
import cn.obcc.db.ILocalDb;
import cn.obcc.driver.IChainDriver;
import cn.obcc.stmt.IStatement;
import com.sun.org.apache.bcel.internal.generic.ILOAD;

public class BaseStatement implements IStatement {

    protected ObccConfig config;
    protected IChainDriver driverManager;

    protected ILocalDb localDb;

    @Override
    public void init(ObccConfig config, ILocalDb db) {
        this.config = config;
        this.localDb = db;
    }

    @Override
    public void setDriverManager(IChainDriver driverManager) {
        this.driverManager = driverManager;
    }

    @Override
    public ILocalDb getLocalDb() {
        return localDb;
    }

    @Override
    public void destory() {


    }

}
