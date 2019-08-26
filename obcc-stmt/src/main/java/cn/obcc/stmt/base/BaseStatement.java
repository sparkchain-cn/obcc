package cn.obcc.stmt.base;

import java.sql.DriverManager;

import cn.obcc.config.ObccConfig;
import cn.obcc.driver.IChainDriver;
import cn.obcc.stmt.IStatement;

public class BaseStatement implements IStatement {

	protected ObccConfig config;
	protected IChainDriver driverManager;

	@Override
	public void init(ObccConfig config) {
		this.config = config;

	}

	@Override
	public void setDriverManager(IChainDriver driverManager) {
		this.driverManager = driverManager;
	}

	@Override
	public void destory() {
		

	}

}
