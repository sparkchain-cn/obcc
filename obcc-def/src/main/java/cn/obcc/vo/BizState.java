package cn.obcc.vo;

import cn.obcc.exception.enums.ETransferStatus;
import lombok.Data;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName BizState
 * @desc TODO
 * @date 2019/9/8 0008  10:02
 **/
@Data
public class BizState {

    private String bizId;
    //hash或##srcAddr+##nonce
    private String hash;
    private String recId;

    //没有返回Hash
   // private String srcAddr;
   // private String none;

    private boolean single = true;
    private ETransferStatus status;

    public BizState() {
    }

    public BizState(String bizId, String hash, String recId, ETransferStatus status) {
        this.bizId = bizId;
        this.hash = hash;
        this.recId = recId;
        this.status = status;
        if (this.hash != null && this.hash.contains(",")) {
            this.single = false;
        }
    }

}
