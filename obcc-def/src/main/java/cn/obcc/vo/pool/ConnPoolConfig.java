package cn.obcc.vo.pool;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
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

}
