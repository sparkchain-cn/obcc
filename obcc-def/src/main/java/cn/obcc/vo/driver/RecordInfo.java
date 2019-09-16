package cn.obcc.vo.driver;

import cn.obcc.vo.BcMemo;
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

    private String storeType;//memo,IPFS,fileSys

    private String msgType;//msg,file

    private String lastHashs;

    private int state;//

    //每个bizId在一条链上
    private String chainCode;

    //每个bizId多条记录的blockNumber以，分隔
    private String blockNumbers;

    private String data;//原始的memo

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

    @Column(name = "store_type")
    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    @Column(name = "msg_type")
    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getLastHashs() {
        return lastHashs;
    }

    public void setLastHashs(String lastHashs) {
        this.lastHashs = lastHashs;
    }


    public String getChainCode() {
        return chainCode;
    }

    public void setChainCode(String chainCode) {
        this.chainCode = chainCode;
    }

    public String getBlockNumbers() {
        return blockNumbers;
    }

    public void setBlockNumbers(String blockNumbers) {
        this.blockNumbers = blockNumbers;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
