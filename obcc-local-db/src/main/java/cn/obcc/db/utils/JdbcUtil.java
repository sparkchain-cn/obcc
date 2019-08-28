package cn.obcc.db.utils;

/**
 *
 */


import cn.obcc.def.annotation.MappingEntity;
import cn.obcc.def.annotation.MappingId;
import org.apache.log4j.Logger;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author:pengrk
 * @email:sjkjs155@126.com
 */
public class JdbcUtil {

    private static final Logger logger = Logger.getLogger(JdbcUtil.class);

    /**
     * 将对象分解出需要的配置信息，并存入MAP里。 键里面存的是字段的名称，值里存的是字段的值。
     *
     * @param object
     *            和数据库表对应的model
     * @return
     */
    public static Map<String, Object> distill(Object object) {
        Map<String, Object> result = new HashMap<String, Object>();
        Method[] methods = object.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            final Method method = methods[i];
            // 判断不需要处理的方法
            boolean isTransient = method.isAnnotationPresent(Transient.class);
            // 过滤掉标识为Transient的访问器方法
            if (!method.getName().startsWith("get")
                    || method.getName().equals("getClass") || isTransient) {
                continue;
            }
            // 得到字段的注释类型
            Column column = method.getAnnotation(Column.class);
            Object columnValue = null;
            try {
                columnValue = method.invoke(object);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            String columnName = method.getName().substring(3).toUpperCase();
            // 判断是否配置了字段的注释
            boolean hasColumn = method.isAnnotationPresent(Column.class);
            if (hasColumn) {
                columnName = column.name().toUpperCase();
            }
            result.put(columnName, columnValue);
        }
        return result;
    }

    /**
     * 将对象分解出需要的配置信息，并存入MAP里。 键里面存的是字段的名称，值里存的是设置器的方法名称。
     *
     * @param object
     *            和数据库表对应的实体
     * @return
     */
    public static Map<String, Method> distillSetter(Object object) {
        Map<String, Method> result = new HashMap<String, Method>();
        Method[] methods = object.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            final Method method = methods[i];
            // 判断不需要处理的方法

            boolean isTransient = false;
            //在mappindId上可以加上@MappingId和@Transient
            if (method.isAnnotationPresent(MappingId.class)) {

                isTransient = false;
            } else {
                isTransient = method.isAnnotationPresent(Transient.class);
                isTransient = isTransient
                        || method.isAnnotationPresent(MappingEntity.class);
            }

            // 过滤掉标识为Transient的访问器方法
            if (!method.getName().startsWith("get")
                    || method.getName().equals("getClass") || isTransient) {
                continue;
            }
            // 判断是否配置了字段的注释
            boolean hasColumn = method.isAnnotationPresent(Column.class);
            // 得到字段的注释类型
            Column column = method.getAnnotation(Column.class);
            String setterMethodName = "set" + method.getName().substring(3);
            Method getterMethod = null;
            Method setterMethod = null;
            try {
                getterMethod = object.getClass().getMethod(method.getName());
                setterMethod = object.getClass().getMethod(setterMethodName,
                        getterMethod.getReturnType());
            } catch (SecurityException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            String columnName = method.getName().substring(3).toUpperCase();
            if (hasColumn) {
                columnName = column.name().toUpperCase();
            }
            result.put(columnName, setterMethod);
        }
        return result;
    }

    /**
     * 查找主键名称
     *
     * @param clz
     * @return
     */
    public static String findIdName(Class<?> clz) {
        String result = null;
        try {
            Method method = clz.getClass().getMethod(findIdMethodFfromClz(clz));
            if (method.isAnnotationPresent(Column.class)) {
                result = method.getAnnotation(Column.class).name();
            } else {
                result = findIdMethodFfromClz(clz).substring(3);
            }
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 查找主键的方法名
     *
     * @param clz
     * @return
     */
    public static String findIdMethodFfromClz(Class clz) {
        String result = null;
        Method[] methods = clz.getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].isAnnotationPresent(Id.class)) {
                if (!methods[i].getName().startsWith("get")) {
                    logger.error("ID必须配置在get方法上面");
                }
                result = methods[i].getName();
                break;
            }
        }
        return result;
    }

    /**
     * 查找对象配置的表名
     *
     * @param clz
     * @return
     */
    public static String findTabelNameFromClz(Class<?> clz) {
        Table tableName = clz.getAnnotation(Table.class);
        return (tableName == null) ? clz.getSimpleName()
                .toUpperCase() : tableName.name().toUpperCase();
    }

    /**
     * 查找对象配置的表名
     *
     * @param object
     * @return
     */
    public static boolean isMethod(Object object, String methodName) {
        try {
            object.getClass().getMethod(methodName);
        } catch (SecurityException e) {
            return false;
        } catch (NoSuchMethodException e) {
            return false;
        }
        return true;
    }

    /**
     * 判断对象是不是实体BEAN 判断实体bean的依据： 1.不是String. 2.不是基本类型. 3.不是Date.
     */
    public static boolean isEntry(Object object) {
        boolean result = true;
        try {
            if (object != null
                    || ((Class) object.getClass().getField("TYPE").get(null))
                    .isPrimitive() || object instanceof String
                    || object instanceof Date) {
                result = false;
            }
        } catch (Exception e) {
            return false;
        }
        return result;
    }


    /**
     * 查找主键名称
     *
     * @param object
     * @return
     */
    public static String findIdNameForClz(Object object) {
        String result = null;
        try {
            Method method = object.getClass().getMethod(findIdMethod(object));
            if (method.isAnnotationPresent(Column.class)) {
                result = method.getAnnotation(Column.class).name();
            } else {
                result = findIdMethod(object).substring(3);
            }
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 查找主键的方法名
     *
     * @param object
     * @return
     */
    public static String findIdMethod(Object object) {
        String result = null;
        Method[] methods = object.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].isAnnotationPresent(Id.class)) {
                if (!methods[i].getName().startsWith("get")) {
                    logger.error("ID必须配置在get方法上面");
                }
                result = methods[i].getName();
                break;
            }
        }
        return result;
    }

    /**
     * 查找对象配置的表名
     *
     * @param object
     * @return
     */
    public static String findTabelName(Object object) {
        Table tableName = object.getClass().getAnnotation(Table.class);
        return (tableName == null) ? object.getClass().getSimpleName()
                .toUpperCase() : tableName.name().toUpperCase();
    }


}
