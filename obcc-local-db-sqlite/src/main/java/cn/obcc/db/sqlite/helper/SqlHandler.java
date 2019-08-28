package cn.obcc.db.sqlite.helper;

import cn.obcc.db.sqlite.SqliteHelper;
import cn.obcc.db.sqlite.mapper.RowMapper;
import cn.obcc.db.sqlite.utils.SqlUtils;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName SqlHandler
 * @desc TODO
 * @date 2019/8/27 0027  23:20
 **/
public class SqlHandler {


    SqliteHelper helper;

    public SqlHandler(SqliteHelper helper) {
        this.helper = helper;
    }

    /**
     * 插入
     *
     * @param tableName
     * @param dataBase
     * @return
     */
    public int insert(String tableName, Object dataBase) {

        return update(SqlUtils.insert(tableName, dataBase));

    }


    /**
     * 执行sql查询
     *
     * @param sql sql select 语句
     * @param rse 结果集处理类对象
     * @return 查询结果
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public <T> T query(String sql, IResultSetExtractor<T> rse) {
        ResultSet resultSet = null;
        Connection connection = null;
        try {
            connection = helper.openConn();
            resultSet = connection.createStatement().executeQuery(sql);
            T rs = rse.extractData(resultSet);
            return rs;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            helper.close(resultSet);
            helper.close(connection);
        }
    }


    /**
     * 执行select查询，返回结果列表
     *
     * @param sql sql select 语句
     * @param rm  结果集的行数据处理类对象
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public <T> List<T> query(String sql, RowMapper<T> rm)
            throws SQLException, ClassNotFoundException {
        List<T> rsList = new ArrayList<T>();
        ResultSet resultSet = null;
        Connection connection = null;
        try {
            connection = helper.openConn();
            ;
            PreparedStatement statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                rsList.add(rm.mapRow(resultSet, resultSet.getRow()));
            }
        } finally {
            helper.close(resultSet);
            helper.close(connection);

        }
        return rsList;
    }

    /**
     * 执行数据库更新sql语句
     *
     * @param sql
     * @return 更新行数
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public int update(String sql) {
        Connection connection = null;
        try {
            connection = helper.openConn();
            int c = connection.createStatement().executeUpdate(new String(sql.getBytes(), "utf8"));
            return c;
        } catch (UnsupportedEncodingException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return -1;
        } finally {
            helper.close(connection);
        }

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
