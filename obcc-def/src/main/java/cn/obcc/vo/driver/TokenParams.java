package cn.obcc.vo.driver;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
@Data
public class TokenParams implements Serializable {

    private String contract;
    private String code;
    private String name;
    private Long supply;
    private int precisions = 18;


}
