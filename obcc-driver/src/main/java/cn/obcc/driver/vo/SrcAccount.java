package cn.obcc.driver.vo;

import java.io.Serializable;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName SrcAccount
 * @desc TODO
 * @date 2019/8/24 0024  17:25
 **/
public class SrcAccount implements Serializable {

    private String srcAddr;
    private String secret;

    private String nonce;
    private String memos;

    private Long gasPrice; // 25Gwei
    private Long gasLimit; // 对应jingtum的gasFee


    //  private String amount;


    public String getSrcAddr() {
        return srcAddr;
    }

    public void setSrcAddr(String srcAddr) {
        this.srcAddr = srcAddr;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getMemos() {
        return memos;
    }

    public void setMemos(String memos) {
        this.memos = memos;
    }

    public Long getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(Long gasPrice) {
        this.gasPrice = gasPrice;
    }

    public Long getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(Long gasLimit) {
        this.gasLimit = gasLimit;
    }
}
