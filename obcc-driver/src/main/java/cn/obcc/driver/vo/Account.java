package cn.obcc.driver.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Account {

    private String address;
    private String secret;
    private String publicKey;

}
