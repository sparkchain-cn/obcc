package cn.obcc.config.conn.pool;

import lombok.Data;

import java.util.Map;

@Data
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


}
