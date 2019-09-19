package cn.obcc.vo.db;

import cn.obcc.vo.Entity;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.List;

/**
 * 回调区块信息
 */
//@Table(name = "block_info")
@Data
public class BlockInfo extends Entity {
    @Id
    private long id;
    @Transient
    private List<String> transactions;// 该账本里的交易列表
    private String blockHash;
    private String blockNumber;
    private Long tradeTime;
    @Transient
    private List<BlockTxInfo> txInfos;


}
