package cn.obcc.stmt;

import cn.obcc.config.ObccConfig;
import cn.obcc.driver.IChainDriver;

public interface IStatement {

	public void init(ObccConfig config);

	public void setDriverManager(IChainDriver driverManager);
	
	public void destory();

}
