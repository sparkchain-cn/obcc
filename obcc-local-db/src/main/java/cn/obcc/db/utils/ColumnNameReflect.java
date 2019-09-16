package cn.obcc.db.utils;

import lombok.Data;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName ColumnNameReflect
 * @desc TODO
 * @date 2019/9/16 0016  18:18
 **/
@Data
public class ColumnNameReflect {

    private String columnName;
    private Field field;
    private Method readMethod;
    private Method writeMethod;
    private Column column;

}
