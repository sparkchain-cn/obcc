package cn.obcc.vo.db;

import lombok.Data;

import java.io.Serializable;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName TxConSensus
 * @desc TODO
 * @date 2019/9/17 0017  11:16
 **/

@Data
public class TxConSensus implements Serializable {

    private String hash;
    private String blockNumber;    //多次相关
    private String gasUsed;
    private String chainTxState;

}
