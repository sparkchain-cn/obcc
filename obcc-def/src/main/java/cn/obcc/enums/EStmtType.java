package cn.obcc.enums;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName EStmtType
 * @desc TODO
 * @date 2019/9/17 0017  9:07
 **/
public enum EStmtType {
    Db_CREATE_TABLE("Db_CREATE_TABLE", "Db_CREATE_TABLE"),
    Db_CREATE_PROC("Db_CREATE_PROC", "Db_CREATE_PROC"),
    Db_TABLE_INSERT("Db_TABLE_INSERT", "Db_TABLE_INSERT"),
    Db_TABLE_UPDATE("Db_TABLE_UPDATE", "Db_TABLE_UPDATE"),
    Db_PROC_EXEC("Db_PROC_EXEC", "Db_PROC_EXEC"),

    STORAGE("STORAGE", "STORAGE"),


    LEDGER_CREATE_USER("Ledger_CREATE_USER", "Ledger_CREATE_USER"),
    LEDGER_ACTIVE_USER("LEDGER_ACTIVE_USER", "LEDGER_ACTIVE_USER"),
    LEDGER_CREATE_TOKEN("LEDGER_CREATE_TOKEN", "LEDGER_CREATE_TOKEN"),
    LEDGER_TOKEN_SEND("LEDGER_TOKEN_SEND", "LEDGER_TOKEN_SEND");
    private String name;
    private String descr;

    private EStmtType(String name, String desc) {
        this.name = name;
        this.descr = desc;
    }

    public String getName() {
        return name;
    }


}
