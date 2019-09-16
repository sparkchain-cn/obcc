package cn.obcc.vo.stmt;

import lombok.Data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Data
public class TableDefine {

    private String name;
    private List<Column> columns=new ArrayList<Column>();



    public void parseData(Object data) {

    }
}
