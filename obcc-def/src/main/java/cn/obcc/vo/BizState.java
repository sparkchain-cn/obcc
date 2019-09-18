package cn.obcc.vo;

import cn.obcc.exception.enums.ETransferState;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName BizState
 * @desc TODO
 * @date 2019/9/8 0008  10:02
 **/
@Data
@NoArgsConstructor
public class BizState {

    private String bizId;
    //hash或##srcAddr+##nonce？？
    private String hashes;
    private String recId;
    private ETransferState transferState;

    private int txSize = 1;
    private String chainCode;


    //区块回调回来时，该数字加1
    private int consensusSize = 0;

    public void incConsensusSize() {
        consensusSize++;
    }

    //没有返回Hash
    // private String srcAddr;
    // private String none;

}
