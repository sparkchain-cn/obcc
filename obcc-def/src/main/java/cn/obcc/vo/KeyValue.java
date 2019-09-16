package cn.obcc.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class KeyValue<T>  implements Serializable{

    private String key;
    private T val;

    public KeyValue(String key, T val) {
        super();
        this.key = key;
        this.val = val;
    }

    public KeyValue() {

    }

}
