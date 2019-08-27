package cn.obcc.db;

import cn.obcc.config.ObccConfig;
import cn.obcc.vo.Entity;
import cn.obcc.vo.Page;

import java.util.List;
import java.util.Map;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName ILocalDb
 * @desc TODO
 * @date 2019/8/27 0027  18:01
 **/
public interface ILocalDb {

    public void init(ObccConfig config) throws Exception;

    public void createTable(String db, String table) throws Exception;


    public void insert(String db, String table, Entity entity) throws Exception;


    public void update(String db, String table, Map<String, Object> modefyData, Class<? extends Entity> clz) throws Exception;


    public <T> T query(String db, String sql, Map<String, Object> params, Class<T> clz) throws Exception;


    public <T> List<T> queryForList(String db, String sql, Map<String, Object> params, Class<T> clz) throws Exception;


    public <T> Page<T> queryForPage(String db, String sql, Map<String, Object> params, Class<T> clz) throws Exception;


    public <T> T queryForValue(String db, String sql, Map<String, Object> params, Class<T> clz) throws Exception;


}
