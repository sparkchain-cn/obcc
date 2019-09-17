package cn.obcc.db.base;

import cn.obcc.config.ObccConfig;
import cn.obcc.db.base.IJdbcTemplate;
import lombok.NonNull;

import java.util.List;
import java.util.Map;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName ILocalDb
 * @desc TODO
 * @date 2019/8/27 0027  18:01
 **/
public interface JdbcDao<T, PK> {

    public void init(ObccConfig config) throws Exception;

    public void createTable();

    /**
     * 保存对象
     *
     * @param entity
     */
    public void add(T entity);

    /**
     * 更新对象
     *
     * @param object
     */
    public void update(T object);

    public void update(@NonNull PK id, Map<String, Object> values);

    /**
     * 按照指定条件和参数进行查询操作
     *
     * @param conditionSql 条件sql语句 如 name=? && password=?
     * @param params       参数 如 new Object[]{"f","f"}
     * @return 存放对象的列表
     */
    public List<T> query(String conditionSql, Object[] params);


    public List<T> findByProp(String name, Object value);


    public T getById(PK id);

    public T get(String conditionSql, Object[] params);

    public T getByProp(String name, Object value);

    public String getValue(String sql, Object[] params);

    public List<String> getValues(String sql, Object[] params);

    /**
     * 使用sql语句进行查询
     *
     * @param sql    sql语句
     * @param params 参数 如 new Object[]{"f","f"}
     * @return 存放对象的列表
     */
    public List<T> queryBySql(String sql, Object[] params);


    /**
     * 使用sql语句进行查询,返回3,5,4这样形式的ids
     *
     * @param sql    sql语句
     * @param params 参数 如 new Object[]{"f","f"}
     * @return 存放对象的列表
     */
    public String getCatIds(String sql, Object[] params);

    /**
     * 按照指定条件和参数进行删除操作
     *
     * @param conditionSql 条件sql语句 如 name=? && password=?
     * @param params       参数 如 new Object[]{"f","f"}
     * @return 存放对象的列表
     */
    public void delete(String conditionSql, Object[] params);

    /**
     * 查询指定ID的对象
     *
     * @param id
     * @return
     */
//    public T queryById(String id);

    /**
     * 根据属性名和属性值查询对象.
     *
     * @return 符合条件的对象列表
     */
    public List<T> queryBy(String propertyName, Object value);

    /**
     * 得到所有的对象
     */
    public List<T> queryAll();

    /**
     * 删除指定ID的对象
     *
     * @param id
     */
    public void deleteById(PK id);

    /**
     * 删除所有的对象
     */
    public void deleteAll();

    /**
     * 得到主键的名称
     */
    public String primaryKeyName();

    /**
     * 得到表格的名称
     */
    public String tableName();


    /**
     * 取得基于Spring的 JDBC Template
     *
     * @return
     */
    public IJdbcTemplate getJdbcTemplate();

}
