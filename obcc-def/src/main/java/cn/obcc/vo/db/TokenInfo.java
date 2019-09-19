package cn.obcc.vo.db;

import cn.obcc.vo.Entity;
import lombok.Data;

import javax.persistence.Id;

//@Table(name = "token_info")
@Data
public class TokenInfo extends Entity {
    @Id
    private long id;
    private String bizId;//unique
    private String hash;
    private String contract;
    private String code;
    private String name;
    private Long supply;
    private int precisions;

    private String contractAbi;
    private String contractAddress;
    private int state;
    //该类名的类必须实现ITokenParse接口
    private String parseClsName;
    //转币的方法名
    private String transferName = "transfer";
    //private String chainCode;

}
