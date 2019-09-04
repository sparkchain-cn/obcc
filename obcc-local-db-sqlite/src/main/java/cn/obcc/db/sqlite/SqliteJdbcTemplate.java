package cn.obcc.db.sqlite;

import cn.obcc.config.ObccConfig;
import cn.obcc.db.base.IJdbcTemplate;

import cn.obcc.db.mapper.RowMapper;
import cn.obcc.db.sqlite.helper.DbOperator;
import cn.obcc.db.sqlite.helper.SqlHandler;
import cn.obcc.db.sqlite.helper.TableHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName SqliteLocalDb
 * @desc TODO
 * @date 2019/8/28 0028  10:06
 **/
public class SqliteJdbcTemplate implements IJdbcTemplate {
    final static Logger logger = LoggerFactory.getLogger(DbOperator.class);

    protected ObccConfig config;
    protected DbOperator dbOperator;// = new SqliteHelper(dbName);
    protected SqlHandler sqlHandler;
    protected TableHandler tableHandler;

    //@Override
    public void init(ObccConfig config) throws Exception {
        this.config = config;
        createDb(config.getDbConn());
        dbOperator = new DbOperator(config.getDbConn());
        // sqlHandler=new TableHandler(dbOperator);
        // sqlHandler = new SqlHandler(dbOperator);
        // tableHandler = new TableHandler(dbOperator);
    }


    private void createDb(String dbName) throws IOException {
        File file = new File(dbName);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        logger.debug("sqlite 数据器文件:" + file.getAbsoluteFile());
    }

    @Override
    public <T> List<T> query(String sql, RowMapper<T> rm) {
        try {
            return dbOperator.query(sql, rm);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> List<T> query(String sql, Object[] params, RowMapper<T> rm) {
        try {
            return dbOperator.query(sql, params, rm);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String queryForSingle(String sql) {
        String s = null;
        try {
            s = dbOperator.queryForSingle(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return s;
    }

    @Override
    public int update(String sql, Object[] params) {
        return dbOperator.update(sql, params);
    }

    @Override
    public int update(String sql) {
        return dbOperator.update(sql);
    }

}
