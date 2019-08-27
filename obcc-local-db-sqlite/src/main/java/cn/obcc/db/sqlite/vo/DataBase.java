package cn.obcc.db.sqlite.vo;

/**
 * DataBase
 *
 * @author ecasona
 * @version 1.4
 * @date 2019/8/15 14:13
 * @details
 */
public class DataBase {

    private Long id;

    private String jdbcUrl;

    private String user;

    private String password;

    private Integer type;

    private String sql;

    private String standard;

    private String tag;


    public DataBase(Integer type, String tag, String jdbcUrl, String user, String password, String sql, String standard) {
        this.jdbcUrl = jdbcUrl;
        this.user = user;
        this.password = password;
        this.type = type;
        this.sql = sql;
        this.standard = standard;
        this.tag = tag;
    }

    public DataBase(Long id, Integer type, String tag, String jdbcUrl, String user, String password,
                    String sql, String standard) {
        this.id = id;
        this.jdbcUrl = jdbcUrl;
        this.user = user;
        this.password = password;
        this.type = type;
        this.sql = sql;
        this.standard = standard;
        this.tag = tag;
    }

    public DataBase() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }
}
