package cn.obcc.driver.vo;

import cn.obcc.driver.utils.JunctionUtils;
import cn.obcc.utils.base.StringUtils;

public class TransactionInfo {

    private String hash;

    private String sourceAddress;
    private String destinationAddress;
    private String contractAddress;

    // chaincode;
    private String chain;
    //private String token;
    private String amount;
    private String memos;
    private String gasFee;

    // 链上的状态
    private String state;

    private String blockNumber;
    private Long blockTime;
    private Long tradeTime;

    private Long nonce;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
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

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getChain() {
        return chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

//	public String getToken() {
//		return token;
//	}
//
//	public void setToken(String token) {
//		this.token = token;
//	}

    public String getAmount() {
        return rvZeroAndDot(amount);
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMemos() {
        return memos;
    }

    public void setMemos(String memos) {
        this.memos = memos;
    }

    public String getGasFee() {
        return JunctionUtils.rvZeroAndDot(gasFee);
    }

    public void setGasFee(String gasFee) {
        this.gasFee = gasFee;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public Long getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(Long blockTime) {
        this.blockTime = blockTime;
    }

    public Long getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Long tradeTime) {
        this.tradeTime = tradeTime;
    }

    public Long getNonce() {
        return nonce;
    }

    public void setNonce(Long nonce) {
        this.nonce = nonce;
    }

    String rvZeroAndDot(String s) {
        if (StringUtils.isNullOrEmpty(s)) {
            return null;
        }
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");// 去掉多余的0
            s = s.replaceAll("[.]$", "");// 如最后一位是.则去掉
        }
        return s;
    }
}
