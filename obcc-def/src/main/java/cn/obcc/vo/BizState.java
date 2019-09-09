package cn.obcc.vo;

import cn.obcc.exception.enums.ETransferStatus;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName BizState
 * @desc TODO
 * @date 2019/9/8 0008  10:02
 **/
public class BizState {

    private String bizId;
    private String hash;
    private boolean single = true;
    private ETransferStatus status;

    public BizState(String bizId, String hash, ETransferStatus status) {
        this.bizId = bizId;
        this.hash = hash;
        this.status = status;
        if (this.hash!=null&&this.hash.contains(",")) {
            this.single = false;
        }
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public ETransferStatus getStatus() {
        return status;
    }

    public void setStatus(ETransferStatus status) {
        this.status = status;
    }

    public boolean isSingle() {
        return single;
    }
}
