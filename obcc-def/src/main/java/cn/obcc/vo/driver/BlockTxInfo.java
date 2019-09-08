package cn.obcc.vo.driver;

import cn.obcc.exception.enums.EChainTxType;
import cn.obcc.vo.BcMemo;
import cn.obcc.vo.Entity;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "block_tx_info")
public class BlockTxInfo extends Entity {

    private EChainTxType txType;
    private String chainCode;
    private String blockHash;
    private String blockNumber;
    private String blockTime;


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

    private BcMemo memosObj;
    private String details;
    private String contractAddress;
    private String method;
    private String methodParams;//toJson

    private int shardingFlag;

   // @Column(name = "tx_type")
    @Transient
    public EChainTxType getTxType() {
        return txType;
    }

    public void setTxType(EChainTxType txType) {
        this.txType = txType;
    }

    @Column(name = "chain_code")
    public String getChainCode() {
        return chainCode;
    }

    public void setChainCode(String chainCode) {
        this.chainCode = chainCode;
    }

    @Column(name = "block_hash")
    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    @Column(name = "block_number")
    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    @Column(name = "block_time")
    public String getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(String blockTime) {
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

    @Column(name = "src_addr")
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

    @Column(name = "dest_addr")
    public String getDestAddr() {
        return destAddr;
    }

    public void setDestAddr(String destAddr) {
        this.destAddr = destAddr;
    }

    @Column(name = "gas_price")
    public String getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice;
    }

    @Column(name = "gas_limit")
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

    @Column(name = "contract_address")
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

    @Column(name = "method_params")
    public String getMethodParams() {
        return methodParams;
    }

    public void setMethodParams(String methodParams) {
        this.methodParams = methodParams;
    }

    @Column(name = "sharding_flag")
    public int getShardingFlag() {
        return shardingFlag;
    }

    public void setShardingFlag(int shardingFlag) {
        this.shardingFlag = shardingFlag;
    }

    @Column(name = "gas_used")
    public String getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(String gasUsed) {
        this.gasUsed = gasUsed;
    }

    public void setMemosObj(BcMemo memosObj) {
        this.memosObj = memosObj;
    }

    @Transient
    public BcMemo getMemosObj() {
        return memosObj;
    }

}

