package cn.obcc.driver.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 回调区块信息
 */
public class BlockInfo {
    private List<String> transactions;// 该账本里的交易列表
    private String blockHash;
    private String blockNumber;
    private Long tradeTime;
    private List<BlockTxInfo> txInfos;

    public List<String> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<String> transactions) {
        this.transactions = transactions;
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

    public Long getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Long tradeTime) {
        this.tradeTime = tradeTime;
    }

}
