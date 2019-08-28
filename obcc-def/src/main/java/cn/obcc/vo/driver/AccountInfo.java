package cn.obcc.vo.driver;

import cn.obcc.vo.Entity;

import javax.persistence.Column;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName AccountInfo
 * @desc TODO
 * @date 2019/8/27 0027  16:06
 **/
//@Table()
public class AccountInfo extends Entity {

    private String bizId;
    private String userName;
    private String password;
    private String address;
    private String secret;
    private String publicKey;

    //激活不在这里做
    private int state;//0:创建，1：已激活

    @Column(name = "biz_id")
    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }
    @Column(name = "user_name")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
