package cn.obcc.db.sqlite.mapper;

import cn.obcc.db.sqlite.SqliteFactroy;
import cn.obcc.db.sqlite.mapper.RowMapper;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

/**
 * BaseMapper
 *
 * @author ecasona
 * @version 1.4
 * @date 2019/8/21 14:26
 * @details
 */
public abstract class BaseMapper<T> implements RowMapper<T> {

    /**
     * 表名
     *
     * @return
     */
    public abstract String getTableName();

    @Override
    public T mapRow(ResultSet rs, int index) throws SQLException {
        return mapperRow(rs, (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    public T mapperRow(ResultSet rs, Class<T> clazz) throws SQLException {

        if (rs == null) {
            return null;
        }

        ResultSetMetaData rsmd = rs.getMetaData();
        int colCount = rsmd.getColumnCount();
        Field[] fields = clazz.getDeclaredFields();
        Object obj = null;
        //构造业务对象实体
        try {
            obj = clazz.newInstance();
            //将每一个字段取出进行赋值
            for (int i = 1; i <= colCount; i++) {
                Object value = rs.getObject(i);
                //寻找该列对应的对象属性
                for (Field f : fields) {
                    //如果匹配进行赋值
                    if (f.getName().equalsIgnoreCase(rsmd.getColumnName(i))) {
                        boolean flag = f.isAccessible();
                        f.setAccessible(true);
                        f.set(obj, value);
                        f.setAccessible(flag);
                        break;
                    }
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (T) obj;
    }

    public T getById(String id) {
        try {
            List<T> list = SqliteFactroy.getInstance()
                    .query(String.format(" select * from %s where id = '%s' ", getTableName(), id), this);

            if (list.size() > 0) {
                return list.get(0);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteById(String id) {
        try {
            return SqliteFactroy.getInstance()
                    .update(String.format(" delete from %s where id = '%s' ", getTableName(), id)) != -1;
        } catch (UnsupportedEncodingException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
