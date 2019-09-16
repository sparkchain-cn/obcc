package cn.obcc.vo.driver;

import cn.obcc.vo.BcMemo;
import cn.obcc.vo.Entity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName AccountInfo
 * @desc TODO
 * @date 2019/8/27 0027  16:06
 **/

//@Table(name = "record_info")
@Data
public class RecordInfo extends Entity {
    @Id
    private long id;
    //重发不在driver,driver只有网络IOException才重试
    private String bizId;
    private String hashs;
    private String userName;

    private String storeType;//memo,IPFS,fileSys

    private String msgType;//msg,file

    private String lastHashs;

    private int state;//

    //每个bizId在一条链上
    private String chainCode;

    //每个bizId多条记录的blockNumber以，分隔
    private String blockNumbers;

    private String data;//原始的memo


}
