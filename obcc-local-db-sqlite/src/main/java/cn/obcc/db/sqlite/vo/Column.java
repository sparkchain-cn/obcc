package cn.obcc.db.sqlite.vo;

/**
 * ColumnVo
 *
 * @author ecasona
 * @version 1.4
 * @date 2019/8/20 10:03
 * @details
 */
public class Column {

    /**
     * 列名
     */
    private String columnName;
    /**
     * 列类型
     */
    private String columnType;
    /**
     * 列默认值
     */
    private Object defaultValue;

    public Column() {
    }

    public Column(String columnName, String columnType, Object defaultField) {
        this.columnName = columnName;
        this.columnType = columnType;
        this.defaultValue = defaultField;
    }


    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

}
