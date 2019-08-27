package cn.obcc.exception.enums;

public enum EMemoStrategy {

    JSON("JSON", "JSON"),
    Binary("Binary", "Binary");

    private String name;
    private String desc;

    private EMemoStrategy(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }
}
