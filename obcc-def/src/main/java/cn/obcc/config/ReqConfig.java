package cn.obcc.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReqConfig<T> {

	public static final String INIT_TYPE = "1";
	public static final String DYNC_TYPE = "2";
	public static final String MANUL_TYPE = "4";

	/**
	 * 调用的应用系统的ID
	 */
	private String appid;

	private String sparkHash;

	Map<String, Object> params;

	private List<String> uuids = new ArrayList<>();

	private String type;

	//是否是需要拆分
	private boolean needSplit;

	// 关联的连接对象
	private T client;

	public ReqConfig() {
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getSparkHash() {
		return sparkHash;
	}

	public void setSparkHash(String sparkHash) {
		this.sparkHash = sparkHash;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public List<String> getUuids() {
		return uuids;
	}

	public void setUuids(List<String> uuids) {
		this.uuids = uuids;
	}

	public T getClient() {
		return client;
	}

	public void setClient(T client) {
		this.client = client;
	}

	public boolean isNeedSplit() {
		return needSplit;
	}

	public void setNeedSplit(boolean needSplit) {
		this.needSplit = needSplit;
	}
}
