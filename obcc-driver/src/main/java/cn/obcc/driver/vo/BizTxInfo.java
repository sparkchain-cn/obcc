package cn.obcc.driver.vo;

import cn.obcc.driver.utils.JunctionUtils;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.vo.driver.BlockTxInfo;
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

