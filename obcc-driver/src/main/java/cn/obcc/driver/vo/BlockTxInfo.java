package cn.obcc.driver.vo;

import cn.obcc.config.ReqConfig;
import cn.obcc.driver.module.fn.ITokenOperateFn;
import cn.obcc.exception.enums.EChainTxType;
import cn.obcc.vo.RetData;
import cn.obcc.vo.driver.TokenInfo;

import java.math.BigInteger;

public class BlockTxInfo {

	private EChainTxType txType;
	private String chainCode;
	private String blockHash;
	private String blockNumber;
	private Long  blockTime;


	private String hash;
	private String state;


	private String srcAddr;
	private Long nonce;

	private String destAddr;
	private String gasPrice;
	private String gasLimit;
	private String gasUsed;
	private String token;
	private String amount;
	private String memos;

	private String details;
	private String contractAddress;
     private String method;
     private String methodParams;//toJson

	private int shardingFlag;


	public EChainTxType getTxType() {
		return txType;
	}

	public void setTxType(EChainTxType txType) {
		this.txType = txType;
	}

	public String getChainCode() {
		return chainCode;
	}

	public void setChainCode(String chainCode) {
		this.chainCode = chainCode;
	}

	public String getBlockHash() {
		return blockHash;
	}

	public void setBlockHash(String blockHash) {
		this.blockHash = blockHash;
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

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSrcAddr() {
		return srcAddr;
	}

	public void setSrcAddr(String srcAddr) {
		this.srcAddr = srcAddr;
	}

	public Long getNonce() {
		return nonce;
	}

	public void setNonce(Long nonce) {
		this.nonce = nonce;
	}

	public String getDestAddr() {
		return destAddr;
	}

	public void setDestAddr(String destAddr) {
		this.destAddr = destAddr;
	}

	public String getGasPrice() {
		return gasPrice;
	}

	public void setGasPrice(String gasPrice) {
		this.gasPrice = gasPrice;
	}

	public String getGasLimit() {
		return gasLimit;
	}

	public void setGasLimit(String gasLimit) {
		this.gasLimit = gasLimit;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAmount() {
		return amount;
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

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getContractAddress() {
		return contractAddress;
	}

	public void setContractAddress(String contractAddress) {
		this.contractAddress = contractAddress;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getMethodParams() {
		return methodParams;
	}

	public void setMethodParams(String methodParams) {
		this.methodParams = methodParams;
	}

	public int getShardingFlag() {
		return shardingFlag;
	}

	public void setShardingFlag(int shardingFlag) {
		this.shardingFlag = shardingFlag;
	}

	public String getGasUsed() {
		return gasUsed;
	}

	public void setGasUsed(String gasUsed) {
		this.gasUsed = gasUsed;
	}
}

