package cn.obcc.driver.vo.params;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class TokenParams implements Serializable {

    private String contract;
    private String code;
    private String name;
    private BigInteger supply;
    private int precisions = 18;


    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getSupply() {
        return supply;
    }

    public void setSupply(BigInteger supply) {
        this.supply = supply;
    }

    public int getPrecisions() {
        return precisions;
    }

    public void setPrecisions(int precisions) {
        this.precisions = precisions;
    }


}
