package cn.obcc.driver.vo;

import cn.obcc.driver.utils.JunctionUtils;
import cn.obcc.utils.base.StringUtils;

import java.util.List;

public class BizTransactionInfo {

    private String bizId;
    private String hashs;
    private String sourceAddress;
    private String destinationAddress;
    //合并的memos，合并其data部分，形成数据
    private String memos;

    private String state;

    private List<TransactionInfo> recordInfos;

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

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getMemos() {
        return memos;
    }

    public void setMemos(String memos) {
        this.memos = memos;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<TransactionInfo> getRecordInfos() {
        return recordInfos;
    }

    public void setRecordInfos(List<TransactionInfo> recordInfos) {
        this.recordInfos = recordInfos;
    }
}

