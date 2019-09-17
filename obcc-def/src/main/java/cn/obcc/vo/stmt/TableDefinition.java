package cn.obcc.vo.stmt;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class TableDefinition {

    private String name;
    private List<Column> columns = new ArrayList<Column>();

    public String toJson() {
        return JSON.toJSONString(this);
    }

//    public void parseData(Object data) {
//
//    }
}
