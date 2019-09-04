package cn.obcc.exception.enums;

public enum EUpchainType {
	Transfer("Transfer", "Transfer"),
	BlockBack("BlockBack", "BlockBack");
	private String name;
	private String descr;

	private EUpchainType(String name, String desc) {
		this.name = name;
		this.descr = desc;
	}
}
