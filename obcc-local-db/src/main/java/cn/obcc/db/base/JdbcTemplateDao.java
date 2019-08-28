package cn.obcc.db.base;

import cn.obcc.config.ObccConfig;
import cn.obcc.db.mapper.RowMapper;
import cn.obcc.db.utils.BeanUtil;
import cn.obcc.db.utils.JdbcUtil;
import cn.obcc.utils.base.StringUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author pengrk
 * @email:sjkjs155@126.com
 * @wetsite:www.mgicode.com
 * @license:GPL
 */
public abstract class JdbcTemplateDao<T, PK> implements JdbcDao<T, PK>, java.io.Serializable {

    private static final Logger logger = Logger.getLogger(JdbcTemplateDao.class);

    protected IJdbcTemplate jdbcTemplate;
    protected Class<T> entityClass;
    private T entity;

    private ObccConfig config;

    @SuppressWarnings("unchecked")
    public JdbcTemplateDao() {
        initEntity();
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
        entityClass = BeanUtil.getSuperClassGenricType(getClass());
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

    public void add(T object) {
        // Assert.notNull(object, "对象不能为空");
        String tableName = JdbcUtil.findTabelName(object);
        Map<String, Object> map = JdbcUtil.distill(object);
        Set<String> columnNames = map.keySet();
        StringBuffer columnString = new StringBuffer();
        List<Object> paramList = new ArrayList<Object>();
        int index = 0;
        StringBuffer interrogations = new StringBuffer();
        for (String columnName : columnNames) {
            if (map.get(columnName) == null) {
                continue;
            }
            columnString.append(columnName).append(",");
            paramList.add(map.get(columnName));
            interrogations.append(" ? ").append(",");
            index++;
        }
        Object[] params = new Object[paramList.size()];
        for (int i = 0; i < paramList.size(); i++) {
            params[i] = paramList.get(i);
        }
        columnString.deleteCharAt(columnString.length() - 1);
        interrogations.deleteCharAt(interrogations.length() - 1);
        final String sql = "insert into " + tableName + "(" + columnString
                + " ) values(" + interrogations + ")";
        getJdbcTemplate().update(sql, params);
    }


    @Override
    public void update(T object) {
        //Assert.notNull(object, "对象不能为空");
        String tableName = JdbcUtil.findTabelName(object);
        Map<String, Object> map = JdbcUtil.distill(object);
        Set<String> columnNames = map.keySet();
        List<Object> paramList = new ArrayList<Object>();
        StringBuffer paramSql = new StringBuffer();
        Object id = null;
        for (String columnName : columnNames) {
            final Object value = map.get(columnName);
            if (primaryKeyName().equalsIgnoreCase(columnName)) {
                id = value;
                continue;
            }
            if (value == null) {
                continue;
            }
            paramSql.append(columnName).append(" = ").append("?").append(",");
            paramList.add(value);
        }
        Object[] params = new Object[paramList.size() + 1];
        for (int i = 0; i < paramList.size(); i++) {
            params[i] = paramList.get(i);
        }
        params[paramList.size()] = id;
        paramSql.deleteCharAt(paramSql.length() - 1);
        final StringBuffer sql = new StringBuffer().append("UPDATE ").append(
                tableName).append(" SET ").append(paramSql).append(" where ")
                .append(primaryKeyName()).append("=?");
        getJdbcTemplate().update(sql.toString(), params);
    }

    @SuppressWarnings("unchecked")
    public List<T> query(String conditionSql, Object[] params) {
        return getJdbcTemplate().query(
                "select * from " + tableName() + " where  " + conditionSql,
                params, rowMapper);
    }

    public T findOne(String conditionSql, Object[] params) {
        List<T> list = query(conditionSql, params);
        if (list == null) return null;
        return list.get(0);
    }

    @SuppressWarnings("unchecked")
    public List<T> queryBySql(String sql, Object[] params) {
        return getJdbcTemplate().query(sql, params, rowMapper);
    }


    public String queryForSingle(String sql) {
        return getJdbcTemplate().queryForSingle(sql);
    }

    public String queryIdsBySql(String sql, Object[] params) {
        final StringBuffer sBuffer = new StringBuffer();
        RowMapper rMapper = new RowMapper() {
            public Object mapRow(ResultSet rs, int Index) throws SQLException {
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

    public void delete(String conditionSql, Object[] params) {
        getJdbcTemplate().update(
                "delete from " + tableName() + " where  " + conditionSql,
                params);
    }

    public T queryById(String id) {
        // Assert.notNull(id, "ID不能为空");
        final List<T> object = queryBy(primaryKeyName(), id);
        return (object.size() == 0) ? null : (T) object.get(0);
    }

    @SuppressWarnings("unchecked")
    public List<T> queryBy(String propertyName, Object value) {
        return getJdbcTemplate().query(
                "select * from " + tableName() + " where " + propertyName
                        + " = ?", new Object[]{value}, rowMapper);
    }

    @SuppressWarnings("unchecked")
    public List<T> queryAll() {
        return getJdbcTemplate().query("select * from " + tableName(),
                rowMapper);
    }

    public boolean exist(String tableName) {
        String sql = String.format("SELECT count(*) FROM sqlite_master WHERE type='table' AND name= '%s'", tableName);
        String count = queryForSingle(sql);
        if (StringUtils.isNotNullOrEmpty(count) && Integer.parseInt(count) > 0) {
            return true;
        }
        return false;

    }

    public void deleteById(Long id) {
        //Assert.notNull(id, "ID不能为空");
        getJdbcTemplate().update(
                "delete from " + tableName() + " where " + primaryKeyName()
                        + "= ?", new Object[]{id});
    }

    public void deleteAll() {
        getJdbcTemplate().update("delete from " + tableName());
    }

    /**
     * 字段映射，子类直接使用
     */
    public RowMapper rowMapper = new RowMapper() {
        public Object mapRow(ResultSet rs, int Index) throws SQLException {
            T newEntity = null;
            try {
                newEntity = entityClass.newInstance();
            } catch (InstantiationException e1) {
                throw new RuntimeException("字段映射时出现异常", e1);
            } catch (IllegalAccessException e1) {
                throw new RuntimeException("字段映射时出现异常", e1);
            }
            Map<String, Method> map = JdbcUtil.distillSetter(newEntity);
            Set<String> columns = map.keySet();
            for (String column : columns) {
                Method method = map.get(column);
                try {
                    final String type = method.getParameterTypes()[0]
                            .getSimpleName();
                    if ("String".equals(type)) {
                        method.invoke(newEntity, rs.getString(column));
                    } else if ("int".equals(type) || "Integer".equals(type)) {
                        method.invoke(newEntity, rs.getInt(column));
                    } else if ("long".equals(type)) {
                        method.invoke(newEntity, rs.getLong(column));
                    } else if ("Date".equals(type)) {
                        method.invoke(newEntity, rs.getTimestamp(column));
                    } else {
                        method.invoke(newEntity, rs.getObject(column));
                    }
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("字段映射时出现异常", e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("字段映射时出现异常", e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException("字段映射时出现异常", e);
                } catch (Exception e) {
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

    public String primaryKeyName() {
        return JdbcUtil.findIdNameForClz(entityClass);
    }

    public String tableName() {
        return JdbcUtil.findTabelNameFromClz(entityClass);
    }

    @Override
    public IJdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    ////todo:
    public abstract String getCreateSql();
}
