package cn.obcc.vo.driver;

import cn.obcc.vo.db.BlockTxInfo;
import lombok.Data;

import java.util.List;
@Data
public class BizTxInfo {

    private String bizId;
    private String hashs;
    private String sourceAddress;
    private String destinationAddress;
    //合并的memos，合并其data部分，形成数据
    private String memos;

    private String state;

    private List<BlockTxInfo> recordInfos;


}

