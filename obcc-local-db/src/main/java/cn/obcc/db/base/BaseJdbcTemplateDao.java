package cn.obcc.db.base;

import cn.obcc.config.ObccConfig;
import cn.obcc.db.mapper.RowMapper;
import cn.obcc.db.utils.BeanUtils;
import cn.obcc.db.utils.ClassUtils;
import cn.obcc.db.utils.ColumnNameReflect;
import cn.obcc.db.utils.JdbcUtils;
import cn.obcc.utils.base.StringUtils;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author pengrk
 * @email:sjkjs155@126.com
 * @wetsite:www.mgicode.com
 * @license:GPL
 */
public abstract class BaseJdbcTemplateDao<T, PK> implements JdbcDao<T, PK>, java.io.Serializable {

    public static final Logger logger = LoggerFactory.getLogger(BaseJdbcTemplateDao.class);

    protected IJdbcTemplate jdbcTemplate;
    protected Class<T> entityClass;
    private T entity;

    private ObccConfig config;

    //fieldName->ColumnNameReflect
    protected final Map<String, ColumnNameReflect> entityClzDistill;


    public BaseJdbcTemplateDao() {
        initEntity();
        entityClzDistill = JdbcUtils.distillClz(entityClass);
    }

    @Override
    public void init(ObccConfig config) throws Exception {
        this.config = config;
        this.jdbcTemplate = newJdbcTemplate();
        jdbcTemplate.init(config);
    }

    private IJdbcTemplate newJdbcTemplate() {
        String clzName = config.getJdbcTemplateName();
        try {
            return (IJdbcTemplate) getClass().getClassLoader().loadClass(clzName).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            logger.error("不能加载或实例化dbcTemplate:" + clzName + ",请引用对应的JAR,并设定好对应的dbcTemplate类名。");
        }

        return null;

    }


    private void initEntity() {
        entityClass = ClassUtils.getSuperClassGenricType(getClass());

    }


    @Override
    public void add(@NonNull T object) {
        String tableName = tableName();
        Collection<ColumnNameReflect> columnNames = entityClzDistill.values();
        StringBuffer clnsb = new StringBuffer();
        List<Object> paramList = new ArrayList<Object>();
        StringBuffer interrogations = new StringBuffer();
        int index = 0;

        for (ColumnNameReflect nameReflect : columnNames) {
            if (nameReflect == null) {
                continue;
            }
            clnsb.append(nameReflect).append(",");
            try {
                Object v = nameReflect.getReadMethod().invoke(object);
                paramList.add(v);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            interrogations.append(" ? ").append(",");
            index++;

        }
        Object[] params = new Object[paramList.size()];

        for (int i = 0; i < paramList.size(); i++) {
            params[i] = paramList.get(i);
        }

        clnsb.deleteCharAt(clnsb.length() - 1);
        interrogations.deleteCharAt(interrogations.length() - 1);

        final String sql = "INSERT INTO " + tableName + " ( " + clnsb
                + " ) VALUES ( " + interrogations + " ) ";

        logger.debug("sql:" + sql);
        getJdbcTemplate().update(sql, params);
    }


    @Override
    public void update(@NonNull T object) {
        String tableName = tableName();

        Collection<ColumnNameReflect> columnNames = entityClzDistill.values();
        List<Object> paramList = new ArrayList<Object>();
        StringBuffer paramSql = new StringBuffer();
        Object id = null;
        for (ColumnNameReflect nameReflect : columnNames) {
            try {
                Object value = nameReflect.getReadMethod().invoke(object);
                //解析Id
                if (primaryKeyName().equalsIgnoreCase(nameReflect.getColumnName())) {
                    id = value;
                    continue;
                }
                //null值不update
                if (value == null) {
                    continue;
                }
                paramList.add(value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            paramSql.append(nameReflect.getColumnName()).append(" = ").append(" ? ").append(" , ");
        }

        Object[] params = new Object[paramList.size() + 1];
        //更新值
        for (int i = 0; i < paramList.size(); i++) {
            params[i] = paramList.get(i);
        }
        //Id条件
        params[paramList.size()] = id;

        paramSql.deleteCharAt(paramSql.length() - 1);

        final StringBuffer sql = new StringBuffer().append("UPDATE ").append(
                tableName).append(" SET ").append(paramSql).append(" WHERE ")
                .append(primaryKeyName()).append("= ?");

        getJdbcTemplate().update(sql.toString(), params);
    }


    @Override
    public void update(@NonNull PK id, Map<String, Object> values) {
        String tableName = tableName();

        Collection<ColumnNameReflect> columnNames = entityClzDistill.values();
        List<Object> paramList = new ArrayList<Object>();
        StringBuffer paramSql = new StringBuffer();
        // Object id = null;
        for (String key : values.keySet()) {
            ColumnNameReflect reflect = entityClzDistill.get(key);
            if (reflect == null) {
                logger.error("class {} field {} 不存在。", entityClass.getName(), key);
                continue;
            }
            paramList.add(values.get(key));
            paramSql.append(reflect.getColumnName()).append(" = ").append(" ? ").append(" , ");
        }

        Object[] params = new Object[paramList.size() + 1];
        //更新值
        for (
                int i = 0; i < paramList.size(); i++) {
            params[i] = paramList.get(i);
        }
        //Id条件
        params[paramList.size()] = id;

        paramSql.deleteCharAt(paramSql.length() - 1);

        StringBuffer sql = new StringBuffer().append("UPDATE ").append(
                tableName).append(" SET ").append(paramSql).append(" WHERE ")
                .append(primaryKeyName()).append("= ?");

        getJdbcTemplate().update(sql.toString(), params);
    }


    @Override
    public List<T> query(String conditionSql, Object[] params) {
        return getJdbcTemplate().query(
                "SELECT * FROM " + tableName() + " WHERE  " + conditionSql,
                params, rowMapper);
    }

    @Override
    public List<T> queryBySql(String sql, Object[] params) {
        return getJdbcTemplate().query(sql, params, rowMapper);
    }

    @Override
    public List<T> findByProp(String name, Object value) {
        List<T> list = query("" + name + "= ? ", new Object[]{value});
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list;
    }

    @Override
    public T getById(PK id) {
        List<T> list = query("" + primaryKeyName() + "= ? ", new Object[]{id});
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public T getByProp(String name, Object value) {
        List<T> list = query("" + name + "= ? ", new Object[]{value});
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public T get(String conditionSql, Object[] params) {
        List<T> list = query(conditionSql, params);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public String getValue(String sql) {
        return getJdbcTemplate().getValue(sql);
    }

    @Override
    public String getCatIds(String sql, Object[] params) {
        final StringBuffer sBuffer = new StringBuffer();
        RowMapper rMapper = new RowMapper() {
            @Override
            public Object mapRow(ResultSet rs, int index) throws SQLException {
                return String.valueOf(rs.getObject(1));
            }
        };
        List<Object> list = getJdbcTemplate().query(sql, params, rMapper);

        if ((list != null) && (list.size() > 0)) {
            for (int i = 0; i < list.size(); i++) {
                sBuffer.append(String.valueOf(list.get(i)));
                if (i != list.size() - 1) {
                    sBuffer.append(",");
                }
            }
            return sBuffer.toString();
        }

        return null;

    }

    @Override
    public void delete(String conditionSql, Object[] params) {
        getJdbcTemplate().update(
                "delete from " + tableName() + " where  " + conditionSql,
                params);
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<T> queryBy(String propertyName, Object value) {
        return getJdbcTemplate().query(
                "SELECT * FROM " + tableName() + " WHERE " + propertyName
                        + " = ?", new Object[]{value}, rowMapper);
    }

    @Override
    public List<T> queryAll() {
        return getJdbcTemplate().query("SELECT * FROM " + tableName(),
                rowMapper);
    }

    public boolean exist(String tableName) {
        String sql = String.format("SELECT count(*) FROM sqlite_master WHERE type='table' AND name= '%s'", tableName);
        String count = getValue(sql);
        if (StringUtils.isNotNullOrEmpty(count) && Integer.parseInt(count) > 0) {
            return true;
        }
        return false;

    }

    @Override
    public void deleteById(@NonNull PK id) {
        getJdbcTemplate().update(
                "DELETE FROM " + tableName() + " WHERE " + primaryKeyName()
                        + "= ?", new Object[]{id});
    }

    @Override
    public void deleteAll() {
        getJdbcTemplate().update("DELETE FROM " + tableName());
    }

    /**
     * 字段映射，子类直接使用
     */
    public RowMapper rowMapper = new RowMapper() {
        @Override
        public Object mapRow(ResultSet rs, int index) throws SQLException {
            T newEntity = (T) BeanUtils.newInstance(entityClass);
            for (ColumnNameReflect reflect : entityClzDistill.values()) {
                Method method = reflect.getWriteMethod();
                String clnName = reflect.getColumnName();
                try {
                    String type = method.getParameterTypes()[0]
                            .getSimpleName();
                    if ("String".equals(type)) {
                        method.invoke(newEntity, rs.getString(clnName));
                    } else if ("int".equals(type) || "Integer".equals(type)) {
                        method.invoke(newEntity, rs.getInt(clnName));
                    } else if ("long".equals(type) || "Long".equals(type)) {
                        method.invoke(newEntity, rs.getLong(clnName));
                    } else if ("float".equals(type) || "Float".equals(type)) {
                        method.invoke(newEntity, rs.getFloat(clnName));
                    } else if ("double".equals(type) || "Double".equals(type)) {
                        method.invoke(newEntity, rs.getDouble(clnName));
                    } else if ("Boolean".equals(type) || "boolean".equals(type)) {
                        method.invoke(newEntity, rs.getBoolean(clnName));
                    } else if ("BigDecimal".equals(type) || "decimal".equals(type)) {
                        method.invoke(newEntity, rs.getBigDecimal(clnName));
                    } else if ("Date".equals(type)) {
                        method.invoke(newEntity, rs.getTimestamp(clnName));
                    } else if (method.getParameterTypes()[0].isEnum()) {
                        method.invoke(newEntity, BeanUtils.getEnum(rs.getString(clnName), (Class<? extends Enum>) method.getParameterTypes()[0]));
                    } else {
                        method.invoke(newEntity, rs.getObject(clnName));
                    }
                } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException("字段映射时出现异常", e);
                }
            }
            return newEntity;
        }
    };

    @Override
    public void createTable() {
        ////todo:
        if (!exist(tableName())) {
            String sql = getCreateSql();
            getJdbcTemplate().update(sql);

        }

    }

    @Override
    public String primaryKeyName() {
        return JdbcUtils.findIdNameForClz(entityClass);
    }

    @Override
    public String tableName() {
        return JdbcUtils.findTabelNameFromClz(entityClass);
    }

    @Override
    public IJdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    ////todo:
    public abstract String getCreateSql();

    //        try {
//            synchronized (this) {
//                if (entity == null) {
//                    entity = entityClass.newInstance();
//                }
//            }
//
//        } catch (InstantiationException e) {
//            throw new RuntimeException(e);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
}
