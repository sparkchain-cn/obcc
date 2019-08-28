package cn.obcc.db.sqlite.helper;

import cn.obcc.db.sqlite.vo.Column;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Locale;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName TableUtils
 * @desc TODO
 * @date 2019/8/27 0027  23:16
 **/
public class TableHandler {
    final static Logger logger = LoggerFactory.getLogger(TableHandler.class);
    DbOperator helper;

    public TableHandler(DbOperator helper) {
        this.helper = helper;
    }

    /**
     * 根據sql  创建表
     *
     * @param tableName 表名
     * @return 创建成功与否
     */
    public synchronized boolean createTable(String tableName, String sql) {
        helper.update(String.format(" drop table if exists %s; ", tableName));
        //String sql = SqlUtils.createTaleSql(tableName, vos);
        helper.update(sql);
        return true;
    }


    /**
     * 表中添加列
     * ALTER TABLE 表名 ADD COLUMN 列名 数据类型
     *
     * @param tableName 表名
     * @param vo        {@link }
     *                  columnName   列名
     *                  columnType   列类型
     *                  defaultField 列默认值
     */
    public synchronized void addColumn(String tableName, Column vo) {
        ResultSet resultSet = null;
        Statement statement = null;
        Connection connection = null;
        try {
            connection = helper.openConn();
            statement = connection.createStatement();
            if (null != statement) {
                //查询第一条记录
                resultSet = statement
                        .executeQuery(String.format("SELECT * FROM %s LIMIT 1 ", tableName));
                boolean flag = true;
                if (resultSet != null) {
                    //遍历检索是否已经存在该列
                    ResultSetMetaData rsmd = resultSet.getMetaData();
                    for (int i = 0; i < rsmd.getColumnCount(); i++) {
                        if (vo.getColumnName().equalsIgnoreCase(rsmd.getColumnName(i))) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        //插入列
                        helper.update(String.format(Locale.CHINESE, "ALTER TABLE %s ADD COLUMN %s %s default %s",
                                tableName, vo.getColumnName(), vo.getColumnType(), vo.getDefaultValue()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(String.format(Locale.CHINESE, "表：%s 中 %s 列添加失败==%s",
                    tableName, vo.getColumnName(), e.getMessage()));
        } finally {
            helper.close(resultSet);
            helper.close(statement);
            helper.close(connection);


        }
    }


    /**
     * 更新表名称
     * ALTER TABLE 旧表名 RENAME TO 新表名
     *
     * @param oldName
     * @param newName
     */
    public synchronized void rename(String oldName, String newName) {
        try {
            helper.update(String.format(Locale.CHINESE, "ALTER TABLE %s RENAME TO %s",
                    oldName, newName));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(String.format(Locale.CHINESE, "表：%s 更新名称为 %s 失败==%s",
                    oldName, newName, e.getMessage()));
        }
    }

    /**
     * 删除表
     * DROP TABLE 表名;
     *
     * @param tableName
     */
    public synchronized void delete(String tableName) {
        try {
            helper.update(String.format(Locale.CHINESE, "DROP TABLE %s", tableName));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(String.format(Locale.CHINESE, "表：%s 删除失败==%s", tableName, e.getMessage()));
        }
    }

    public boolean exist(String tableName) {
        boolean isTableExist = true;
        ResultSet c = null;
        Connection connection = null;
        try {
            connection = helper.openConn();
            c = connection.prepareStatement(String.format("SELECT count(*) FROM sqlite_master WHERE type='table' AND name= '%s'", tableName)).executeQuery();
            if (c.getInt(1) == 0) {
                isTableExist = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {

            helper.close(c);
            helper.close(connection);


        }
        return isTableExist;

    }

}
