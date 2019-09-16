package cn.obcc.driver.vo.params;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
@Data
public class TxParams implements Serializable {

    private String sourceAddress;
    private String secret;
    private String destinationAddress;
    private String nonce;
    private String amount;
    private String memos;
    private Long gasPrice; // 25Gwei
    private Long gasLimit; // 对应jingtum的gasFee

    /**
     * 私钥--->密钥
     */
    private String cipher;

}
