package cn.obcc.db.sqlite.vo;

/**
 * Record
 *
 * @author ecasona
 * @version 1.4
 * @date 2019/8/21 13:01
 * @details
 */
public class Record {

    private Long id;

    private String sql;
    private Long connectorId;
    private String user;
    private String password;

    private long createTime;
    private long lastCheckTime;

    private Integer upchain = 0;
    private Integer result = 0;

    private String lastUpChainPath;

    private String hash;

    public Record() {
    }

    public Record(Long connectorId, Integer upchain) {
        this.connectorId = connectorId;
        this.upchain = upchain;
    }

    public Record(Long id, Long connectorId, Integer upchain) {
        this.id = id;
        this.connectorId = connectorId;
        this.upchain = upchain;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getConnectorId() {
        return connectorId + "";
    }

    public Long getConnectorIdRaw() {
        return connectorId;
    }

    public void setConnectorId(Long connectorId) {
        this.connectorId = connectorId;
    }

    public void setConnectorId(String connectorId) {
        this.connectorId = connectorId == null ? null : Long.parseLong(connectorId);
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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public Integer getUpchain() {
        return upchain;
    }

    public void setUpchain(Integer upchain) {
        this.upchain = upchain;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getLastUpChainPath() {
        return lastUpChainPath;
    }

    public void setLastUpChainPath(String lastUpChainPath) {
        this.lastUpChainPath = lastUpChainPath;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public long getLastCheckTime() {
        return lastCheckTime;
    }

    public void setLastCheckTime(long lastCheckTime) {
        this.lastCheckTime = lastCheckTime;
    }
}
