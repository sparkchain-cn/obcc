package cn.obcc.vo.stmt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Column {
    private String name;
    private String dataType;
    private String memo;

}
