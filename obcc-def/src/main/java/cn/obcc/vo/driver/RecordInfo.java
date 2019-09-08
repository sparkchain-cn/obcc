package cn.obcc.vo.driver;

import cn.obcc.vo.Entity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName AccountInfo
 * @desc TODO
 * @date 2019/8/27 0027  16:06
 **/

@Table(name = "record_info")
public class RecordInfo extends Entity {
    //重发不在driver,driver只有网络IOException才重试
    private String bizId;
    private String hashs;
    private String userName;


    private int state;//

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getHashs() {
        return hashs;
    }

    public void setHashs(String hashs) {
        this.hashs = hashs;
    }
    @Column(name = "user_name")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
