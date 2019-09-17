package cn.obcc.vo.driver;

import cn.obcc.exception.enums.EChainTxType;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.vo.BcMemo;
import cn.obcc.vo.Entity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "block_tx_info")
@Data
public class BlockTxInfo extends Entity {
    @Id
    private long id;

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

    @Transient
    private BcMemo memosObj;
    private String details;
    private String contractAddress;
    private String method;
    private String methodParams;//toJson

    private int shardingFlag;

    //在回调的处理函数中找缓存，并更新到字段上来
    private String recId;
    private String bizId;

    public boolean isContract() {
        return StringUtils.isNotNullOrEmpty(contractAddress);
    }

    public boolean isSingle() {
        if (isContract()) {
            return true;
        }
        if (memosObj == null) {
            return true;
        }
        if (memosObj.getSum() == null && memosObj.getSeq() == null) {
            return true;
        }
        if (memosObj.getSum().longValue() == 1) {
            return true;
        }
        return false;
    }

    public boolean isLast() {
        //合约只有一条
        if (isContract()) {
            return true;
        }
        //非合约，查memo,看看是否最后一条
        if (memosObj == null) {
            return false;
        }

        if (memosObj.getSum() == null && memosObj.getSeq() == null) {
            return true;
        }
        return memosObj.getSeq().longValue() == memosObj.getSum().longValue();
    }
}

