package cn.obcc.vo.driver;

import cn.obcc.vo.Entity;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.List;

/**
 * 回调区块信息
 */
@Table(name = "block_info")
public class BlockInfo extends Entity {
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
    @Column(name = "trade_time")
    public Long getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Long tradeTime) {
        this.tradeTime = tradeTime;
    }

}
