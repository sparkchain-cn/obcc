package cn.obcc.enums;

public enum EStoreType {
	Memo("memo", "memo"), Ipfs("ipfs", "ipfs"), FileSystem("file", "file");
	private String name;
	private String descr;

	private EStoreType(String name, String desc) {
		this.name = name;
		this.descr = desc;
	}
}
