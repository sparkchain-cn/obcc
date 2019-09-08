package cn.obcc.exception.enums;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName ECallBackLevel
 * @desc TODO
 * @date 2019/9/7 0007  22:45
 **/
public enum ECallBackLevel {
    SupportUpchain("SupportUpchain", "SupportUpchain"),
    SupportQuery("SupportQuery", "SupportQuery"),
    SupportAll("SupportAll", "SupportAll");

    private String name;
    private String desc;

    private ECallBackLevel(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }
}
