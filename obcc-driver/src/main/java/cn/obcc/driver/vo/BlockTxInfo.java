package cn.obcc.driver.vo;

public class BlockTxInfo {

	private String hash;
	private String srcAddr;
	private String destAddr;
	private String blockHash;
	private String blockNumber;
	private String memos;
	private String chainCode;
	private String token;
	private String contractAddress;
	private String gasFee;
	private String amount;
	private String sparkHash;
	private String state;
	private Long tradeTime;
	private Long nonce;
	private String txType;
	private boolean normal;
	private String gasPrice;
	private String gasLimit;
	private String details;
	private String appid;
	private int shardingFlag;
	private Long srcFlowId;
	private Long destFlowId;

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getSrcAddr() {
		return srcAddr;
	}

	public void setSrcAddr(String srcAddr) {
		this.srcAddr = srcAddr;
	}

	public String getDestAddr() {
		return destAddr;
	}

	public void setDestAddr(String destAddr) {
		this.destAddr = destAddr;
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

	public String getMemos() {
		return memos;
	}

	public void setMemos(String memos) {
		this.memos = memos;
	}

	public String getChainCode() {
		return chainCode;
	}

	public void setChainCode(String chainCode) {
		this.chainCode = chainCode;
	}

	public String getContractAddress() {
		return contractAddress;
	}

	public void setContractAddress(String contractAddress) {
		this.contractAddress = contractAddress;
	}

	public String getGasFee() {
		return gasFee;
	}

	public void setGasFee(String gasFee) {
		this.gasFee = gasFee;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getSparkHash() {
		return sparkHash;
	}

	public void setSparkHash(String sparkHash) {
		this.sparkHash = sparkHash;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTxType() {
		return txType;
	}

	public void setTxType(String txType) {
		this.txType = txType;
	}

	public boolean isNormal() {
		return normal;
	}

	public void setNormal(boolean normal) {
		this.normal = normal;
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

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public Long getSrcFlowId() {
		return srcFlowId;
	}

	public void setSrcFlowId(Long srcFlowId) {
		this.srcFlowId = srcFlowId;
	}

	public Long getDestFlowId() {
		return destFlowId;
	}

	public void setDestFlowId(Long destFlowId) {
		this.destFlowId = destFlowId;
	}

	public int getShardingFlag() {
		return shardingFlag;
	}

	public void setShardingFlag(int shardingFlag) {
		this.shardingFlag = shardingFlag;
	}
}

