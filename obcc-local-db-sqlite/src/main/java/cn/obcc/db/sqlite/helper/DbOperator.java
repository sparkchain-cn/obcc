package cn.obcc.db.sqlite.helper;

import cn.obcc.db.mapper.RowMapper;
import cn.obcc.db.sqlite.utils.ConnUtils;
import cn.obcc.db.utils.TypeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SqliteHelper
 *
 * @author ecasona
 * @version 1.4
 * @date 2019/8/20 9:16
 * @details qlite帮助类，直接创建该类示例，并调用相应的借口即可对sqlite数据库进行操作
 * <p>
 * 本类基于 sqlite jdbc v56
 */
public class DbOperator {

    final static Logger logger = LoggerFactory.getLogger(DbOperator.class);

    //private Connection connection;
    private String dbFilePath;


    /**
     * 构造函数
     *
     * @param dbFilePath sqlite db 文件路径
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public DbOperator(String dbFilePath) throws ClassNotFoundException, SQLException {
        this.dbFilePath = dbFilePath;
    }

    public Connection openConn() throws SQLException, ClassNotFoundException {
        return ConnUtils.getConnection(this.dbFilePath);
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
            connection = this.openConn();
            ;
            PreparedStatement statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                rsList.add(rm.mapRow(resultSet, resultSet.getRow()));
            }
        } finally {
            this.close(resultSet);
            this.close(connection);

        }
        return rsList;
    }


    public <T> List<T> query(String sql, Object[] params, RowMapper<T> rm) throws SQLException, ClassNotFoundException {
        List<T> rsList = new ArrayList<T>();
        ResultSet resultSet = null;
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = this.openConn();
            ;
            stmt = connection.prepareStatement(sql);
            holdParams(stmt, params);
            resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                rsList.add(rm.mapRow(resultSet, resultSet.getRow()));
            }
        } finally {
            this.close(resultSet);
            this.close(stmt);
            this.close(connection);

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
            connection = this.openConn();
            int c = connection.createStatement().executeUpdate(new String(sql.getBytes(), "utf8"));
            return c;
        } catch (UnsupportedEncodingException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return -1;
        } finally {
            this.close(connection);
        }

    }


    private void holdParams(PreparedStatement stmt, Object[] params) throws SQLException {
        int i = 1;
        for (Object o : params) {

            if (TypeUtil.isString(o.getClass())) {
                stmt.setString(i, (String) o);
            } else if (TypeUtil.isLong(o.getClass())) {
                stmt.setLong(i, (Long) o);
            } else if (TypeUtil.isInteger(o.getClass())) {
                stmt.setInt(i, (int) o);
            } else if (TypeUtil.isBoolean(o.getClass())) {
                stmt.setBoolean(i, (boolean) o);
            } else if (TypeUtil.isDouble(o.getClass())) {
                stmt.setDouble(i, (double) o);
            } else if (TypeUtil.isFloat(o.getClass())) {
                stmt.setFloat(i, (float) o);
            } else if (o instanceof Date || (o instanceof java.sql.Date)) {
                stmt.setDate(i, (Date) o);
            } else if (TypeUtil.isShort(o.getClass())) {
                stmt.setShort(i, (short) o);
            } else if (o instanceof Timestamp) {
                stmt.setTimestamp(i, (Timestamp) o);
            } else {
                stmt.setString(i, (String) o);
            }
            i++;
            // stmt.setDate();
            // stmt.setInt();
            // stmt.setBoolean();
            //stmt.setShort();
            //stmt.setFloat();
            // stmt.setDouble();
            //stmt.setLong();
            //stmt.setTimestamp();
            //stmt.setString();
            //stmt.setString(1, username);
            //stmt.setString(2, address);
        }
    }

    public int update(String sql, Object[] params) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = this.openConn();
            stmt = connection.prepareStatement(sql);
            holdParams(stmt, params);
            int rows = stmt.executeUpdate(new String(sql.getBytes(), "utf8"));
            if (rows > 0) {
                System.out.println("operate successfully!");

            }

            return rows;
        } catch (SQLException | ClassNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return -1;
        } finally {
            this.close(stmt);
            this.close(connection);
        }

    }


    public void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void close(Statement rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
