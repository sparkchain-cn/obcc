package cn.obcc.vo.driver;

import cn.obcc.exception.enums.EMsgType;
import cn.obcc.exception.enums.EStmtType;
import cn.obcc.exception.enums.EStoreType;
import cn.obcc.exception.enums.ETransferStatus;
import cn.obcc.vo.BcMemo;
import cn.obcc.vo.Entity;
import com.alibaba.fastjson.JSON;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName AccountInfo
 * @desc TODO
 * @date 2019/8/27 0027  16:06
 **/

//@Table(name = "record_info")
@Data
public class RecordInfo extends Entity {
    @Id
    private long id;
    //重发不在driver,driver只有网络IOException才重试
    private String bizId;
    //每个bizId在一条链上
    private String chainCode;

    private EStmtType stmtType;
    private EStoreType storeType = EStoreType.Memo;
    private EMsgType msgType = EMsgType.Msg;

    //对表记录更新时使用
    private String orignRecordId;
    private String orignBizId;
    private long updateOrder;//更新的第几次

    private String srcUser;
    private String srcAccount;
    private String token;
    private String amount;
    private String destUser;
    private String destAccount;
    private String contractAddr;
    private String memo;

    //流水条数
    private int txSize;
    private String hashs;

    @Transient
    private List<TxRecv> txRecvList = new ArrayList<>();
    private String gasPrice;
    private String gasLimit;
    //链接收时的区块高度
    private String recvBlockHeight;
    private String nonce;
    private String errorCode;
    private String errorMsg;
    //多条流水，合并上面的数据
    private String txRecvJson;

    @Transient
    private List<TxConSensus> txConSensusList = new ArrayList<>();
    //每个bizId多条记录的blockNumber以，分隔
    private String blockNumber;    //多次相关
    private String gasUsed;
    private String chainTxState;
    //多条流水时合并上面
    private String consensusJson;

    //transfer state
    private ETransferStatus state;



    public List<TxConSensus> getComputedConSensusList() {
        List<TxConSensus> list = JSON.parseArray(consensusJson, TxConSensus.class);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

}
