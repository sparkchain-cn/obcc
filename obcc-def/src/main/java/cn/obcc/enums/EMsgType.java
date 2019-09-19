package cn.obcc.enums;

public enum EMsgType {
    Msg("msg", "msg"), File("file", "file");
    private String name;
    private String descr;

    private EMsgType(String name, String desc) {
        this.name = name;
        this.descr = desc;
    }

	public String getName() {
		return name;
	}


}
