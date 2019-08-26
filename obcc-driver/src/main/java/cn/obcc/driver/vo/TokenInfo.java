package cn.obcc.driver.vo;

import java.math.BigInteger;

public class TokenInfo {

    private String contract;
    private String code;
    private String name;
    private BigInteger supply;
    private int precisions;

    private String contractAbi;
    private String contractAddress;
    private String state;
    private String bizId;
    private String chainCode;

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

    public BigInteger getSupply() {
        return supply;
    }

    public void setSupply(BigInteger supply) {
        this.supply = supply;
    }

    public int getPrecisions() {
        return precisions;
    }

    public void setPrecisions(int precisions) {
        this.precisions = precisions;
    }

    public String getContractAbi() {
        return contractAbi;
    }

    public void setContractAbi(String contractAbi) {
        this.contractAbi = contractAbi;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getChainCode() {
        return chainCode;
    }

    public void setChainCode(String chainCode) {
        this.chainCode = chainCode;
    }
}
