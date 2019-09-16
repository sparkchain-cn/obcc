package cn.obcc.vo.driver;

import cn.obcc.vo.Entity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;

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

  
}
