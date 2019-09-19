package cn.obcc.enums;

public enum EChainTxType {

	Orign("Orign", "Orign"),
	Token("Token", "Token"),
	Contract("Contract", "Contract");

	private String name;
	private String token;

	private EChainTxType(String name, String token) {
		this.name = name;
		this.token = token;
	}
}
