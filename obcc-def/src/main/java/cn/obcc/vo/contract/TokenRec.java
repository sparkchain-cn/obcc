package cn.obcc.vo.contract;

import lombok.Data;

/**
 * @author mgicode
 * @version 1.0
 * @ClassName TokenRec
 * @desc
 * @data 2019/9/2 17:11
 **/
@Data
public class TokenRec {
    private String method;
    private String destAddr;
    private String  amount;
    private  String memo;
}
