package cn.obcc.db.sqlite;

import cn.obcc.db.sqlite.helper.IResultSetExtractor;
import cn.obcc.db.sqlite.helper.SqlHandler;
import cn.obcc.db.sqlite.helper.TableHandler;
import cn.obcc.db.sqlite.mapper.RowMapper;
import cn.obcc.db.sqlite.utils.ConnUtils;
import cn.obcc.db.sqlite.utils.SqlUtils;
import cn.obcc.db.sqlite.vo.Column;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
public class SqliteHelper {

    final static Logger logger = LoggerFactory.getLogger(SqliteHelper.class);

    //private Connection connection;
    private String dbFilePath;

    private TableHandler tableHandler;
    private SqlHandler sqlHandler;

    /**
     * 构造函数
     *
     * @param dbFilePath sqlite db 文件路径
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public SqliteHelper(String dbFilePath) throws ClassNotFoundException, SQLException {
        this.dbFilePath = dbFilePath;
        //connection = getConnection(dbFilePath);
        this.sqlHandler = new SqlHandler(this);
        this.tableHandler = new TableHandler(this);
    }

    public Connection openConn() throws SQLException, ClassNotFoundException {
        return ConnUtils.getConnection(this.dbFilePath);
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


    /**
     * 插入
     *
     * @param tableName
     * @param dataBase
     * @return
     */
    public int insert(String tableName, Object dataBase) {
        return this.sqlHandler.insert(tableName, dataBase);
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
    public <T> T query(String sql, IResultSetExtractor<T> rse)
            throws SQLException, ClassNotFoundException {
        return this.sqlHandler.query(sql, rse);
    }

    /**
     * @param tableName
     * @param rm
     * @param <T>
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public <T> List<T> queryAll(String tableName, RowMapper<T> rm)
            throws SQLException, ClassNotFoundException {
        String sql = String.format(" select * from %s", tableName);
        return query(sql, rm);
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
        return this.sqlHandler.query(sql, rm);
    }

    /**
     * 执行数据库更新sql语句
     *
     * @param sql
     * @return 更新行数
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public int update(String sql)
            throws UnsupportedEncodingException, SQLException, ClassNotFoundException {
        return this.sqlHandler.update(sql);
    }

    /**
     * 执行多个sql更新语句
     *
     * @param sqls
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void update(String... sqls) throws SQLException, ClassNotFoundException {
        update(Arrays.asList(sqls));
    }

    /**
     * 执行数据库更新 sql List
     *
     * @param sqls sql列表
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void update(List<String> sqls) throws SQLException, ClassNotFoundException {
        this.sqlHandler.update(sqls);
    }


    /**
     * 根據sql创建表
     *
     * @param tableName 表名
     * @param vos       {@link }
     * @return 创建成功与否
     */
    public synchronized boolean createTable(String tableName, Column... vos) {
        return tableHandler.createTable(tableName, vos);
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
        tableHandler.addColumn(tableName, vo);
    }
    /**
     * 更新表名称
     * ALTER TABLE 旧表名 RENAME TO 新表名
     *
     * @param oldTableName
     * @param newTableName
     */
    public synchronized void tableRename(String oldTableName, String newTableName) {
        tableHandler.tableRename(oldTableName, newTableName);
    }

    /**
     * 删除表
     * DROP TABLE 表名;
     *
     * @param tableName
     */
    public synchronized void deleteTable(String tableName) {
        tableHandler.deleteTable(tableName);
    }

    public boolean isTableExist(String tableName) {
        return tableHandler.isTableExist(tableName);
    }

}
