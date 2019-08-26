package cn.obcc.exception.enums;

public enum EDbOperaType {
	Table("Table", "Table"), Procedure("Procedure", "Procedure");
	private String name;
	private String descr;

	private EDbOperaType(String name, String desc) {
		this.name = name;
		this.descr = desc;
	}
}
