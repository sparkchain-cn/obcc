package cn.obcc.exception.enums;

public enum ECallbackStrategy {

    Subscribe("subscribe", "subscribe"),
    Pull("Pull", "Pull");

    private String name;
    private String token;

    private ECallbackStrategy(String name, String token) {
        this.name = name;
        this.token = token;
    }
}
