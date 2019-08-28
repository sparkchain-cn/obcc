package cn.obcc.db.sqlite.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName ConnUtils
 * @desc TODO
 * @date 2019/8/27 0027  23:26
 **/
public class ConnUtils {
    /**
     * 获取数据库连接
     *
     * @param dbFilePath db文件路径
     * @return 数据库连接
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection getConnection(String dbFilePath) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
        return conn;
    }
}
