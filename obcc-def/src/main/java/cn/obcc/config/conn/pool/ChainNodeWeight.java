package cn.obcc.config.conn.pool;

import java.util.Map;

public class ChainNodeWeight {
	private String url;
	private int weight;
	private Map<String, Object> extras;

	public ChainNodeWeight() {
	}

	public ChainNodeWeight(String url, int weight) {
		this.url = url;
		this.weight = weight;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public Map<String, Object> getExtras() {
		return extras;
	}

	public void setExtras(Map<String, Object> extras) {
		this.extras = extras;
	}

}
