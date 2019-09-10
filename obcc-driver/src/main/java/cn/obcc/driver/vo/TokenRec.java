package cn.obcc.driver.vo;

/**
 * @author mgicode
 * @version 1.0
 * @ClassName TokenRec
 * @desc
 * @data 2019/9/2 17:11
 **/
public class TokenRec {
    private String method;
    private String destAddr;
    private String  amount;
    private  String memo;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getDestAddr() {
        return destAddr;
    }

    public void setDestAddr(String destAddr) {
        this.destAddr = destAddr;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
