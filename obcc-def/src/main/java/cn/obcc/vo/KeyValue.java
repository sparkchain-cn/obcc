package cn.obcc.vo;

import java.io.Serializable;

public class KeyValue<T>  implements Serializable{

    private String key;
    private T val;

    public KeyValue(String key, T val) {
        super();
        this.key = key;
        this.val = val;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getVal() {
        return val;
    }

    public void setVal(T val) {
        this.val = val;
    }
}
