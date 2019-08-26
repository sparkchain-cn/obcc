package cn.obcc.config.conn.pool;

import java.util.ArrayList;
import java.util.List;

public class ConnPoolConfig {

	private float enterRate = 1f;
	// 连接数量
	private int poolSize=5;
	// 最大连接数量
	private int maxPoolSize=10;
	//最小连接数
	private int minPoolSize=1;

	// 初始连接数量
	private int initConnectSize=3;

	// 清除的时间间隔
	private int clearInteval=6000;

	int maxIdleSecond = 300;// 最大空闲时间（秒），超过该时间的空闲时间的连接将被关闭
	int checkInvervalSecond = 3;// 每隔多少秒，检测一次空闲连接（默认60秒）


	private boolean allowCheck = false;

	private List<ChainNodeWeight> nodeWeights = new ArrayList<>();

	
	public ConnPoolConfig adjust() {

		// todo:copy this object
		// todo:如果当前对象没有传入什么参数或者参数不对，这里进行调整
		
		return new ConnPoolConfig();

	}

	public int getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public int getInitConnectSize() {
		return initConnectSize;
	}

	public void setInitConnectSize(int initConnectSize) {
		this.initConnectSize = initConnectSize;
	}

	public int getClearInteval() {
		return clearInteval;
	}

	public void setClearInteval(int clearInteval) {
		this.clearInteval = clearInteval;
	}



	public int getMinPoolSize() {
		return minPoolSize;
	}

	public void setMinPoolSize(int minPoolSize) {
		this.minPoolSize = minPoolSize;
	}

	public float getEnterRate() {
		return enterRate;
	}

	public void setEnterRate(float enterRate) {
		this.enterRate = enterRate;
	}

	public int getMaxIdleSecond() {
		return maxIdleSecond;
	}

	public void setMaxIdleSecond(int maxIdleSecond) {
		this.maxIdleSecond = maxIdleSecond;
	}

	public int getCheckInvervalSecond() {
		return checkInvervalSecond;
	}

	public void setCheckInvervalSecond(int checkInvervalSecond) {
		this.checkInvervalSecond = checkInvervalSecond;
	}

	public List<ChainNodeWeight> getNodeWeights() {
		return nodeWeights;
	}

	public void setNodeWeights(List<ChainNodeWeight> nodeWeights) {
		this.nodeWeights = nodeWeights;
	}

	public boolean isAllowCheck() {
		return allowCheck;
	}

	public void setAllowCheck(boolean allowCheck) {
		this.allowCheck = allowCheck;
	}

}
