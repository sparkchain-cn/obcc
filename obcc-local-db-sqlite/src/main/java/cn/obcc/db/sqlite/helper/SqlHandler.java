package cn.obcc.db.sqlite.helper;

import cn.obcc.db.mapper.RowMapper;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName SqlHandler
 * @desc TODO
 * @date 2019/8/27 0027  23:20
 **/
public class SqlHandler {


    DbOperator helper;

    public SqlHandler(DbOperator helper) {
        this.helper = helper;
    }


    public <T> List<T> query(String sql, RowMapper<T> rm)
            throws SQLException, ClassNotFoundException {
        return helper.query(sql, rm);
    }


    public int update(String sql) {
        return helper.update(sql);
    }

    public int insert(String tableName, String sql) {
        return update(sql);
    }

    /**
     * 执行数据库更新 sql List
     *
     * @param sqls sql列表
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void update(List<String> sqls) {
        Connection connection = null;
        try {
            connection = helper.openConn();
            for (String sql : sqls) {
                connection.createStatement().executeUpdate(new String(sql.getBytes(), "utf8"));
            }
        } catch (UnsupportedEncodingException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            helper.close(connection);
        }
    }

}
