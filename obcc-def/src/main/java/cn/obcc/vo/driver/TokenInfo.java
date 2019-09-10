package cn.obcc.vo.driver;

import cn.obcc.vo.Entity;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigInteger;

@Table(name = "token_info")
public class TokenInfo extends Entity {

    private String bizId;//unique
    private String hash;
    private String contract;
    private String code;
    private String name;
    private Long supply;
    private int precisions;

    private String contractAbi;
    private String contractAddress;
    private int state;

    public String getParseClsName() {
        return parseClsName;
    }

    public void setParseClsName(String parseClsName) {
        this.parseClsName = parseClsName;
    }

    private String parseClsName;
    //转币的方法名
    private String transferName = "transfer";
    //private String chainCode;

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSupply() {
        return supply;
    }

    public void setSupply(Long supply) {
        this.supply = supply;
    }

    public int getPrecisions() {
        return precisions;
    }

    public void setPrecisions(int precisions) {
        this.precisions = precisions;
    }

    @Column(name = "contract_abi")
    public String getContractAbi() {
        return contractAbi;
    }

    public void setContractAbi(String contractAbi) {
        this.contractAbi = contractAbi;
    }

    @Column(name = "contract_address")
    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Column(name = "biz_id")
    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getTransferName() {
        return transferName;
    }

    public void setTransferName(String transferName) {
        this.transferName = transferName;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

//    public String getChainCode() {
//        return chainCode;
//    }
//
//    public void setChainCode(String chainCode) {
//        this.chainCode = chainCode;
//    }
}
