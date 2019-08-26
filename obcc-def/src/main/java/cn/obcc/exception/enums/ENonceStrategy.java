package cn.obcc.exception.enums;

public enum ENonceStrategy {

    Chain("Chain", "Chain"),
    Memory("Memory", "Memory"),
    Redis("Redis", "Redis"),
    Biz("Biz", "Biz");
    private String name;
    private String desc;

    private ENonceStrategy(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }
}
