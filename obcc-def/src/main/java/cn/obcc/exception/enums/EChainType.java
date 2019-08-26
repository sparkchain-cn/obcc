package cn.obcc.exception.enums;

public enum EChainType {

	ETHER("eth", "ETH");

	private String name;
	private String token;

	private EChainType(String name, String token) {
		this.name = name;
		this.token = token;
	}
}
