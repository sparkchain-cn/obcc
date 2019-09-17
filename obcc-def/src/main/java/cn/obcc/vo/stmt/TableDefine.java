package cn.obcc.vo.stmt;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Data
public class TableDefine {

    private String name;
    private List<Column> columns = new ArrayList<Column>();

    public String toJson() {
        return JSON.toJSONString(this);
    }

    public void parseData(Object data) {

    }
}
