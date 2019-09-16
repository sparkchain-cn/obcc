package cn.obcc.driver.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName SrcAccount
 * @desc TODO
 * @date 2019/8/24 0024  17:25
 **/
@Data
public class SrcAccount implements Serializable {

    private String srcAddr;
    private String secret;

    private String nonce;
    private String memos;

    private Long gasPrice; // 25Gwei
    private Long gasLimit; // 对应jingtum的gasFee


}
