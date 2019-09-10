package cn.obcc.driver.vo;

import cn.obcc.vo.KeyValue;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mgicode
 * @version 1.0
 * @ClassName ContractExecRec
 * @desc
 * @data 2019/9/2 17:02
 **/
public class ContractRec {

    private String method;

    private List<KeyValue> params = new ArrayList<>();


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<KeyValue> getParams() {
        return params;
    }

    public void setParams(List<KeyValue> params) {
        this.params = params;
    }
}
