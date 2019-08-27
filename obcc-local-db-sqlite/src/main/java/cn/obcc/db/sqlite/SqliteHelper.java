package cn.obcc.db.sqlite;

import cn.obcc.db.sqlite.mapper.ResultSetExtractor;
import cn.obcc.db.sqlite.mapper.RowMapper;
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

    private Connection connection;
    private String dbFilePath;

    /**
     * 构造函数
     *
     * @param dbFilePath sqlite db 文件路径
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public SqliteHelper(String dbFilePath) throws ClassNotFoundException, SQLException {
        this.dbFilePath = dbFilePath;
        connection = getConnection(dbFilePath);
    }

    /**
     * 获取数据库连接
     *
     * @param dbFilePath db文件路径
     * @return 数据库连接
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public Connection getConnection(String dbFilePath) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
        return conn;
    }

    /**
     * 根據sql  创建表
     *
     * @param tableName 表名
     * @param vos       {@link }
     * @return 创建成功与否
     */
    public synchronized boolean createTable(String tableName, Column... vos) {
        try {
            update(String.format(" drop table if exists %s; ", tableName));
            String sql = SqlUtils.createTaleSql(tableName, vos);
            update(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
            connection = getConnection(dbFilePath);
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
                        update(String.format(Locale.CHINESE, "ALTER TABLE %s ADD COLUMN %s %s default %s",
                                tableName, vo.getColumnName(), vo.getColumnType(), vo.getDefaultValue()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(String.format(Locale.CHINESE, "表：%s 中 %s 列添加失败==%s",
                    tableName, vo.getColumnName(), e.getMessage()));
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 更新表名称
     * ALTER TABLE 旧表名 RENAME TO 新表名
     *
     * @param oldTableName
     * @param newTableName
     */
    public synchronized void tableRename(String oldTableName, String newTableName) {
        try {
            update(String.format(Locale.CHINESE, "ALTER TABLE %s RENAME TO %s",
                    oldTableName, newTableName));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(String.format(Locale.CHINESE, "表：%s 更新名称为 %s 失败==%s",
                    oldTableName, newTableName, e.getMessage()));
        }
    }

    /**
     * 删除表
     * DROP TABLE 表名;
     *
     * @param tableName
     */
    public synchronized void deleteTable(String tableName) {
        try {
            update(String.format(Locale.CHINESE, "DROP TABLE %s", tableName));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(String.format(Locale.CHINESE, "表：%s 删除失败==%s", tableName, e.getMessage()));
        }
    }

    public boolean isTableExist(String tableName) {
        boolean isTableExist = true;
        ResultSet c = null;
        Connection connection = null;
        try {
            connection = getConnection(dbFilePath);
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
            try {
                if (c != null) {
                    c.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return isTableExist;

    }

    /**
     * 插入
     *
     * @param tableName
     * @param dataBase
     * @return
     */
    public int insert(String tableName, Object dataBase) {
        try {
            return update(SqlUtils.insert(tableName, dataBase));
        } catch (SQLException | ClassNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return -1;
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
    public <T> T query(String sql, ResultSetExtractor<T> rse) throws SQLException, ClassNotFoundException {
        ResultSet resultSet = null;
        Connection connection = null;
        try {
            connection = getConnection(dbFilePath);
            resultSet = connection.createStatement().executeQuery(sql);
            T rs = rse.extractData(resultSet);
            return rs;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public <T> List<T> queryAll(String tableName, RowMapper<T> rm) throws SQLException, ClassNotFoundException {
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
    public <T> List<T> query(String sql, RowMapper<T> rm) throws SQLException, ClassNotFoundException {
        List<T> rsList = new ArrayList<T>();
        ResultSet resultSet = null;
        Connection connection = null;
        try {
            connection = getConnection(dbFilePath);
            PreparedStatement statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                rsList.add(rm.mapRow(resultSet, resultSet.getRow()));
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (connection != null) {
                connection.close();
            }
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
    public int update(String sql) throws UnsupportedEncodingException, SQLException, ClassNotFoundException {
        Connection connection = null;
        try {
            connection = getConnection(dbFilePath);
            int c = connection.createStatement().executeUpdate(new String(sql.getBytes(), "utf8"));
            return c;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

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
        Connection connection = null;
        try {
            connection = getConnection(dbFilePath);
            for (String sql : sqls) {
                connection.createStatement().executeUpdate(new String(sql.getBytes(), "utf8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

}
