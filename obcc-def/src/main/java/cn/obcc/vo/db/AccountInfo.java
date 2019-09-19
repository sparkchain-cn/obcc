package cn.obcc.vo.db;


import cn.obcc.vo.Entity;
import lombok.Data;

import javax.persistence.Id;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName AccountInfo
 * @desc TODO
 * @date 2019/8/27 0027  16:06
 **/

//@Table(name = "account_info")
@Data
public class AccountInfo extends Entity {

    @Id
    private long id;

    private String bizId;

    private String userName;
    private String password;
    private String address;
    private String secret;
    private String publicKey;

    //激活不在这里做
    private int state;//0:创建，1：已激活

}
