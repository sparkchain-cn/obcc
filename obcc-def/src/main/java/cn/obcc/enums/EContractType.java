package cn.obcc.enums;

public enum EContractType {
	SOLC("solc", "solc");
	private String name;
	private String descr;

	private EContractType(String name, String desc) {
		this.name = name;
		this.descr = desc;
	}
}
