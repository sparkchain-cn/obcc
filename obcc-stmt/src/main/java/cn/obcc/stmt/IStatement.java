package cn.obcc.stmt;

import cn.obcc.config.ObccConfig;
import cn.obcc.db.ILocalDb;
import cn.obcc.driver.IChainDriver;

public interface IStatement {

	public void init(ObccConfig config, ILocalDb db);

	public void setDriverManager(IChainDriver driverManager);

	public ILocalDb getLocalDb();

	public void destory();

}
