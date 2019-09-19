package cn.obcc.vo.driver;

import cn.obcc.enums.EDbOperaType;
import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName TableInfo
 * @desc TODO
 * @date 2019/9/17 0017  8:35
 **/
@Data
public class TableInfo implements Serializable {

    @Id
    private long id;

    private String dbName;
    private String name;

    private EDbOperaType type;

    private String contents;
}
