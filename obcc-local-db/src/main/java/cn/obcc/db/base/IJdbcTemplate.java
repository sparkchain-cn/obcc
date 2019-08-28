package cn.obcc.db.base;

import cn.obcc.config.ObccConfig;
import cn.obcc.db.mapper.RowMapper;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName I
 * @desc TODO
 * @date 2019/8/28 0028  15:40
 **/
public interface IJdbcTemplate {
    public void init(ObccConfig config) throws Exception;

    /**
     * 执行select查询，返回结果列表
     *
     * @param sql sql select 语句
     * @param rm  结果集的行数据处理类对象
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public <T> List<T> query(String sql, RowMapper<T> rm);

    public <T> List<T> query(String sql, Object[] params, RowMapper<T> rm);

    public String queryForSingle(String sql);

    /**
     * 执行数据库更新sql语句
     *
     * @param sql
     * @return 更新行数
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public int update(String sql, Object[] params);

    public int update(String sql);
}
