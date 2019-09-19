package cn.obcc.db.utils;

/**
 *
 */


import cn.obcc.utils.base.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author:pengrk
 * @email:sjkjs155@126.com
 */
public class JdbcUtils {

    public static final Logger logger = LoggerFactory.getLogger(JdbcUtils.class);
    private static Map<String, String> tableNameMap = new HashMap<String, String>();
    private static Map<String, String> idNameMap = new HashMap<String, String>();


    public static Map<String, ColumnNameReflect> distillClz(Class entityClass) {
        Map<String, ColumnNameReflect> result = new LinkedHashMap<>();

        Map<String, ReadWriteMethod> map = BeanUtils.getPropMethods(entityClass);

        for (Class superClass = entityClass; superClass != Object.class; superClass = superClass
                .getSuperclass()) {
            Field[] fields = superClass.getDeclaredFields();
            for (Field f : fields) {
                if (f.getAnnotation(Transient.class) != null) {
                    continue;
                }
                ColumnNameReflect crf = new ColumnNameReflect();
                crf.setColumnName(StringUtils.camelToUnderline(f.getName()).toLowerCase());
                crf.setField(f);
                // 判断是否配置了字段的注释
                boolean hasColumn = f.isAnnotationPresent(Column.class);
                // 得到字段的注释类型
                Column column = f.getAnnotation(Column.class);

                if (column != null) {
                    crf.setColumn(column);
                }
                if (column != null && StringUtils.isNotNullOrEmpty(column.name())) {
                    crf.setColumnName(column.name());
                }

                ReadWriteMethod rwm = map.get(f.getName());
                if (rwm == null) {
                    logger.error("没有找到字段的" + entityClass.getName() + "." + f.getName() + "的set,get Method");
                    // throw new RuntimeException("没有找到字段的" + entityClass.getName() + "." + f.getName() + "的set,get Method");
                }

                crf.setReadMethod(rwm.getReadMethod());
                crf.setWriteMethod(rwm.getWriteMethod());
                result.put(f.getName(), crf);
                logger.debug("parse entity: fileName:{}" + crf.getColumnName());
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
        String clsName = clz.getName();
        if (!tableNameMap.containsKey(clsName)) {
            Table table = clz.getAnnotation(Table.class);
            String retName = (table == null) ?
                    StringUtils.camelToUnderline(clz.getSimpleName()).toLowerCase()
                    : table.name();
            tableNameMap.put(clsName, retName);
        }
        return tableNameMap.get(clsName);
    }


    /**
     * 查找主键名称
     *
     * @param clz
     * @return
     */
    public static String findIdNameForClz(Class clz) {
        String clsName = clz.getName();
        String sign = clsName + ".id";
        if (!idNameMap.containsKey(sign)) {
            String result = null;
            Field field = BeanUtils.getDeclaredFieldForAnnotation(clz, Id.class);
            if (field == null) {
                return null;
            }
            if (field.isAnnotationPresent(Column.class)) {
                result = field.getAnnotation(Column.class).name();
            } else {
                result = StringUtils.camelToUnderline(field.getName()).toLowerCase();
            }
            //  return result;
            if (result == null) {
                logger.error("class {} 不存在 @Id的注解。");
                return null;
            }
            idNameMap.put(sign, result);
        }

        return idNameMap.get(sign);
    }


    /**
     //     * 将对象分解出需要的配置信息，并存入MAP里。 键里面存的是字段的名称，值里存的是字段的值。
     //     *
     //     * @param object
     //     *            和数据库表对应的model
     //     * @return
     //     */
//    public static Map<String, Object> distill(Object object) {
//        Map<String, Object> result = new HashMap<String, Object>();
//        Method[] methods = object.getClass().getMethods();
//        for (int i = 0; i < methods.length; i++) {
//            final Method method = methods[i];
//            // 判断不需要处理的方法
//            boolean isTransient = method.isAnnotationPresent(Transient.class);
//            // 过滤掉标识为Transient的访问器方法
//            if (!method.getName().startsWith("get")
//                    || method.getName().equals("getClass") || isTransient) {
//                continue;
//            }
//            // 得到字段的注释类型
//            Column column = method.getAnnotation(Column.class);
//            Object columnValue = null;
//            try {
//                columnValue = method.invoke(object);
//            } catch (IllegalArgumentException e) {
//                throw new RuntimeException(e);
//            } catch (IllegalAccessException e) {
//                throw new RuntimeException(e);
//            } catch (InvocationTargetException e) {
//                throw new RuntimeException(e);
//            }
//            String columnName = method.getName().substring(3).toUpperCase();
//            // 判断是否配置了字段的注释
//            boolean hasColumn = method.isAnnotationPresent(Column.class);
//            if (hasColumn) {
//                columnName = column.name().toUpperCase();
//            }
//            result.put(columnName, columnValue);
//        }
//        return result;
//    }
//
//
//    /**
//     * 将对象分解出需要的配置信息，并存入MAP里。 键里面存的是字段的名称，值里存的是设置器的方法名称。
//     *
//     * @param object
//     *            和数据库表对应的实体
//     * @return
//     */
//    public static Map<String, Method> distillSetter(Object object) {
//        Map<String, Method> result = new HashMap<String, Method>();
//        Method[] methods = object.getClass().getMethods();
//        for (int i = 0; i < methods.length; i++) {
//            final Method method = methods[i];
//            // 判断不需要处理的方法
//
//            boolean isTransient = false;
//            //在mappindId上可以加上@MappingId和@Transient
//            if (method.isAnnotationPresent(MappingId.class)) {
//
//                isTransient = false;
//            } else {
//                isTransient = method.isAnnotationPresent(Transient.class);
//                isTransient = isTransient
//                        || method.isAnnotationPresent(MappingEntity.class);
//            }
//
//            // 过滤掉标识为Transient的访问器方法
//            if (!method.getName().startsWith("get")
//                    || method.getName().equals("getClass") || isTransient) {
//                continue;
//            }
//            // 判断是否配置了字段的注释
//            boolean hasColumn = method.isAnnotationPresent(Column.class);
//            // 得到字段的注释类型
//            Column column = method.getAnnotation(Column.class);
//            String setterMethodName = "set" + method.getName().substring(3);
//            Method getterMethod = null;
//            Method setterMethod = null;
//            try {
//                getterMethod = object.getClass().getMethod(method.getName());
//                setterMethod = object.getClass().getMethod(setterMethodName,
//                        getterMethod.getReturnType());
//            } catch (SecurityException e) {
//                throw new RuntimeException(e);
//            } catch (NoSuchMethodException e) {
//                throw new RuntimeException(e);
//            }
//            String columnName = method.getName().substring(3).toUpperCase();
//            if (hasColumn) {
//                columnName = column.name().toUpperCase();
//            }
//            result.put(columnName, setterMethod);
//        }
//        return result;
//    }
//


//    /**
//     * 查找主键的方法名
//     *
//     * @param clz
//     * @return
//     */
//    public static String findIdMethodFfromClz(Class clz) {
//        String result = null;
//        Method[] methods = clz.getMethods();
//        for (int i = 0; i < methods.length; i++) {
//            if (methods[i].isAnnotationPresent(Id.class)) {
//                if (!methods[i].getName().startsWith("get")) {
//                    logger.error("ID必须配置在get方法上面");
//                }
//                result = methods[i].getName();
//                break;
//            }
//        }
//        return result;
//    }

//    /**
//     * 查找对象配置的表名
//     *
//     * @param object
//     * @return
//     */
//    public static boolean isMethod(Object object, String methodName) {
//        try {
//            object.getClass().getMethod(methodName);
//        } catch (SecurityException e) {
//            return false;
//        } catch (NoSuchMethodException e) {
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * 判断对象是不是实体BEAN 判断实体bean的依据： 1.不是String. 2.不是基本类型. 3.不是Date.
//     */
//    public static boolean isEntry(Object object) {
//        boolean result = true;
//        try {
//            if (object != null
//                    || ((Class) object.getClass().getField("TYPE").get(null))
//                    .isPrimitive() || object instanceof String
//                    || object instanceof Date) {
//                result = false;
//            }
//        } catch (Exception e) {
//            return false;
//        }
//        return result;
//    }


//
//    /**
//     * 查找主键名称
//     *
//     * @param clz
//     * @return
//     */
//    public static String findIdName(Class<?> clz) {
//        String result = null;
//        try {
//            Method method = clz.getClass().getMethod(findIdMethodFfromClz(clz));
//            if (method.isAnnotationPresent(Column.class)) {
//                result = method.getAnnotation(Column.class).name();
//            } else {
//                result = findIdMethodFfromClz(clz).substring(3);
//            }
//        } catch (SecurityException e) {
//            throw new RuntimeException(e);
//        } catch (NoSuchMethodException e) {
//            throw new RuntimeException(e);
//        }
//        return result;
//    }


//    /**
//     * 查找主键的方法名
//     *
//     * @param object
//     * @return
//     */
//    public static String findIdMethod(Object object) throws NoSuchFieldException {
//        String clsName = object.getClass().getName();
//        if (!idNameMap.containsKey(clsName)) {
//            String result = null;
//
//
//            Method[] methods = object.getClass().getMethods();
//            for (int i = 0; i < methods.length; i++) {
//                if (methods[i].isAnnotationPresent(Id.class)) {
//                    if (!methods[i].getName().startsWith("get")) {
//                        logger.error("ID必须配置在get方法上面");
//                    }
//                    result = methods[i].getName();
//                    break;
//                }
//            }
//            idNameMap.put(clsName, result);
//        }
//
//        return idNameMap.get(clsName);
//
//    }

//    /**
//     * 查找对象配置的表名
//     *
//     * @param object
//     * @return
//     */
//    public static String findTabelName(Object object) {
//        String clsName = object.getClass().getName();
//        if (!tableNameMap.containsKey(clsName)) {
//            Table tableName = object.getClass().getAnnotation(Table.class);
//            String retName = (tableName == null) ? object.getClass().getSimpleName()
//                    .toUpperCase() : tableName.name().toUpperCase();
//            tableNameMap.put(clsName, retName);
//        }
//
//
//        return tableNameMap.get(clsName);
//    }


//    /**
//     * 取得指定类中的标明了LogicDelete的属性
//     *
//     * @return
//     */
//    public static PropertyDescriptor getLogicDeleteProperty(Class<?> entityClass) {
//        try {
//            BeanInfo beanInfo = Introspector.getBeanInfo(entityClass);
//            PropertyDescriptor propertyDescriptors[] = beanInfo
//                    .getPropertyDescriptors();
//            for (int i = 0; i < propertyDescriptors.length; i++) {
//                PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
//                Class<?> type = propertyDescriptor.getPropertyType();
//                Method method = propertyDescriptor.getReadMethod();
//                if (method.isAnnotationPresent(LogicalDelete.class)) {
//                    return propertyDescriptor;
//                }
//            }
//
//        } catch (IntrospectionException e) {
//            e.printStackTrace();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static PropertyDescriptor getIdProperty(Class<?> entityClass) {
//        try {
//            BeanInfo beanInfo = Introspector.getBeanInfo(entityClass);
//            PropertyDescriptor propertyDescriptors[] = beanInfo
//                    .getPropertyDescriptors();
//            for (int i = 0; i < propertyDescriptors.length; i++) {
//                PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
//                Class<?> type = propertyDescriptor.getPropertyType();
//                Method method = propertyDescriptor.getReadMethod();
//                if (method != null) {
//                    if (method.isAnnotationPresent(Id.class)) {
//                        return propertyDescriptor;
//                    }
//                }
//            }
//
//        } catch (IntrospectionException e) {
//            e.printStackTrace();
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static String getTableName(Class<?> entityClass) {
//        if (entityClass.isAnnotationPresent(Table.class)) {
//            Table table = entityClass.getAnnotation(Table.class);
//            return table.name();
//        }
//        return null;
//
//    }

}
