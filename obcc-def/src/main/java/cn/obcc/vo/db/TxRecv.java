package cn.obcc.vo.db;

import lombok.Data;

import java.io.Serializable;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName TxRecv
 * @desc TODO
 * @date 2019/9/17 0017  11:13
 **/
@Data
public class TxRecv implements Serializable {
    private String hash;
    private String gasPrice;
    private String gasLimit;
    private String nonce;
    private String errorCode;
    private String errorMsg;

}
