package cn.obcc.stmt;

import cn.obcc.config.ObccConfig;
import cn.obcc.db.DbFactory;
import cn.obcc.driver.IChainDriver;

public interface IStatement {

	public void init(ObccConfig config, DbFactory db);

	public void setDriverManager(IChainDriver driverManager);

	public DbFactory getLocalDb();

	public void destory();

}
