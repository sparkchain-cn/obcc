package cn.obcc.db.utils;

import lombok.NonNull;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.log4j.Logger;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author pengrk
 * @email:sjkjs155@126.com
 * @wetsite:www.mgicode.com
 * @license:GPL
 */

public class BeanUtils {
    private static final Logger logger = Logger.getLogger(BeanUtils.class);

    private static Map<String, Field> fieldMap = new HashMap<String, Field>();
    private static Map<String, Method> methodMap = new HashMap<String, Method>();

    private static Map<String, List<ReadWriteMethod>> clzPropDesc = new HashMap<String, List<ReadWriteMethod>>();

    /**
     * 指定对象，指定属性名，取得其属性值
     *
     * @param object    对象
     * @param fieldName 属性名
     * @return 属性值
     */
    public static Object getFieldValue(Object object, String fieldName) {
        if (object == null) {
            return null;
        }
        Object result = null;

        try {
            Field field = getDeclaredField(object.getClass(), fieldName);
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            // 直接通过其属性字段来读
            result = field.get(object);
            if (result == null) {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(
                        fieldName, object.getClass());
                Method method = propertyDescriptor.getReadMethod();
                result = method.invoke(object, null);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 根据对象，属性名来指定属性值
     *
     * @param object
     * @param fieldName
     * @param value
     * @throws NoSuchFieldException
     */
    public static void setFieldValue(Object object, String fieldName,
                                     Object value) throws NoSuchFieldException {
        Field field = getDeclaredField(object.getClass(), fieldName);
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            logger.error("不可能抛出的异常:{}", e);
            e.printStackTrace();

        }
    }

    /**
     * 循环向上转型,获取类的DeclaredField.
     */
    public static Field getDeclaredFieldForAnnotation(Class clazz, Class annClz) {
        for (Class superClass = clazz; superClass != Object.class; superClass = superClass
                .getSuperclass()) {

            Field[] fields = superClass.getDeclaredFields();
            for (Field f : fields) {
                if (f.getAnnotation(annClz) != null) {
                    return f;
                }
            }
        }
        logger.warn("类" + clazz.getName() + "不存在字段含有注解" + annClz.getName());
        //  throw new NoSuchFieldException("类" + clazz.getName() + "不存在字段含有注解"
        //     + annClz.getName());
        return null;
    }


    /**
     * 循环向上转型,获取类的DeclaredField.
     */
    public static Field getDeclaredField(Class clazz, String fieldName)
            throws NoSuchFieldException {
        String name = clazz.getName() + '.' + fieldName;
        if (fieldMap.containsKey(name)) {
            return fieldMap.get(name);
        }

        Field field = null;
        for (Class superClass = clazz; superClass != Object.class; superClass = superClass
                .getSuperclass()) {
            try {
                field = superClass.getDeclaredField(fieldName);
                if (field != null) {
                    break;
                }
            } catch (NoSuchFieldException e) {
                // Field不在当前类，继续向上转型
            }
        }

        if (field == null) {
            throw new NoSuchFieldException("没有此字段 " + clazz.getName() + '.'
                    + fieldName);
        }

        fieldMap.put(name, field);
        return field;
    }

    @SuppressWarnings("unchecked")
    public static Method getDeclaredMethod(Class clazz, String methodName,
                                           Class<?>... parameterTypes) throws NoSuchFieldException {
        String name = clazz.getName() + '.' + methodName;

        if (methodMap.containsKey(name)) {
            return methodMap.get(name);
        }

        Method field = null;
        for (Class superClass = clazz; superClass != Object.class; superClass = superClass
                .getSuperclass()) {
            try {
                field = superClass.getDeclaredMethod(methodName,
                        parameterTypes);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            if (field != null) {
                break;
            }
        }

        if (field == null) {
            throw new NoSuchFieldException("没有此方法 " + clazz.getName() + '.'
                    + methodName);
        }

        methodMap.put(name, field);
        return field;
    }


    /**
     * 调用指定对象的私有方法
     *
     * @param object
     * @param methodName
     * @param params
     * @return
     * @throws NoSuchMethodException
     */
    public static Object invokePrivateMethod(@NonNull Object object, @NonNull String methodName,
                                             Object... params) throws NoSuchMethodException {
        Class[] types = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            types[i] = params[i].getClass();
            if (logger.isDebugEnabled()) {
                logger.debug("参数类型：" + types[i].toString());
            }
        }

        Class clazz = object.getClass();

        Method method = null;
        try {
            method = clazz.getMethod(methodName, types);
        } catch (Exception e) {

        }
        if (method == null) {
            for (Class superClass = clazz; superClass != Object.class; superClass = superClass
                    .getSuperclass()) {
                if (logger.isDebugEnabled()) {
                    logger.debug(clazz.toString() + "," + methodName + "");
                }
                try {
                    method = superClass.getDeclaredMethod(methodName, types);
                    break;
                } catch (NoSuchMethodException e) {
                    // method=superClass.get.getDeclaredMethod(methodName);
                }
            }
        }
        if (method == null) {
            for (Method m : clazz.getMethods()) {
                if (m.getName().equals(methodName)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("通过getMethods的循环找到了的");
                    }
                    method = m;
                    break;
                }
            }
        }
        if (method == null) {
            throw new NoSuchMethodException(clazz.getSimpleName()
                    + " No Such Method:" + methodName);
        }
        boolean accessible = method.isAccessible();
        method.setAccessible(true);
        Object result = null;
        try {
            result = method.invoke(object, params);
        } catch (Exception e) {
            //  ReflectionUtils.handleReflectionException(e);
        }
        method.setAccessible(accessible);
        return result;
    }

    /**
     * 将list转换为数组
     *
     * @param list
     * @return
     */
    public static Object[] toArray(List list) {
        Object[] result = new Object[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }


    private static List<ReadWriteMethod> getPropertyDescriptors(Class<?> clz) {
        String clzname = clz.getName();
        if (!clzPropDesc.containsKey(clzname)) {
            for (Class superClass = clz; superClass != Object.class; superClass = superClass
                    .getSuperclass()) {
                BeanInfo beanInfo = null;
                try {
                    beanInfo = Introspector.getBeanInfo(superClass);
                } catch (IntrospectionException e) {
                    e.printStackTrace();
                }
                PropertyDescriptor propertyDescriptors[] = beanInfo
                        .getPropertyDescriptors();
                List<ReadWriteMethod> list = new ArrayList<ReadWriteMethod>();
                for (PropertyDescriptor pd : propertyDescriptors) {
                    list.add(new ReadWriteMethod(pd.getReadMethod(), pd
                            .getWriteMethod()));
                }
                clzPropDesc.put(clzname, list);
            }
        }
        return clzPropDesc.get(clzname);

    }


    public static Map<String, ReadWriteMethod> getPropMethods(Class<?> clz) {
        Map<String, ReadWriteMethod> map = new HashMap<>();

        for (Class superClass = clz; superClass != Object.class; superClass = superClass
                .getSuperclass()) {
            BeanInfo beanInfo = null;
            try {
                beanInfo = Introspector.getBeanInfo(superClass);
            } catch (IntrospectionException e) {
                e.printStackTrace();
            }
            PropertyDescriptor propertyDescriptors[] = beanInfo
                    .getPropertyDescriptors();

            for (PropertyDescriptor pd : propertyDescriptors) {
                map.put(pd.getName(), new ReadWriteMethod(pd.getReadMethod(), pd
                        .getWriteMethod()));
            }
        }

        return map;
    }

    /**
     * 复制一个对象
     *
     * @param clz 该对象的类型
     * @param o   对象
     * @return
     */
    public static Object clone(Class<?> clz, Object o) {

        Object object = null;
        try {
            object = clz.newInstance();
        } catch (InstantiationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        // BeanInfo beanInfo = null;
        // try {
        // beanInfo = Introspector.getBeanInfo(clz);
        // } catch (IntrospectionException e) {
        // e.printStackTrace();
        // }
        //
        // PropertyDescriptor propertyDescriptors[] = beanInfo
        // .getPropertyDescriptors();

        List<ReadWriteMethod> pdlist = getPropertyDescriptors(clz);

        // for (int i = 0; i < propertyDescriptors.length; i++) {
        // PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
        for (ReadWriteMethod readWriteMethod : pdlist) {
            Method method = readWriteMethod.getReadMethod();
            Method write = readWriteMethod.getWriteMethod();
            if (method != null && write != null) {
                try {
                    Object o1 = method.invoke(o);
                    if (o1 != null) {
                        write.invoke(object, o1);
                    }
                } catch (IllegalArgumentException e) {

                    e.printStackTrace();
                } catch (IllegalAccessException e) {

                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return object;
    }


    public static Enum getEnum(String name, Class<? extends Enum> clz) {
        return Enum.valueOf(clz, name);
    }

    public static Object read(Class mapping, ResultSet result) {
        Object persistentObject = newInstance(mapping);
        return read(result, persistentObject);
    }

    public static Object read(ResultSet result, Object persistentObject) {
        try {
            for (int i = 1, n = result.getMetaData().getColumnCount(); i <= n; i++) {
                String columnName = result.getMetaData().getColumnName(i);
                populateProperty(persistentObject, columnName,
                        result.getObject(columnName), false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return persistentObject;
    }

    public static void populateProperty(Object o, String attribute,
                                        Object value, boolean ignoreCase) {

        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(o.getClass());
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

        PropertyDescriptor propertyDescriptors[] = beanInfo
                .getPropertyDescriptors();

        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
            if ((ignoreCase && attribute.equals(propertyDescriptor.getName()))
                    || (!ignoreCase && attribute
                    .equalsIgnoreCase(propertyDescriptor.getName()))) {
                Class type = propertyDescriptor.getPropertyType();
                ConvertUtilsBean cub = BeanUtilsBean.getInstance()
                        .getConvertUtils();
                Converter converter = cub.lookup(type);
                if (converter != null) {
                    value = converter.convert(type, value);
                }
                try {
                    propertyDescriptor.getWriteMethod().invoke(o,
                            new Object[]{value});
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    public static String beanToString(Object obj) {
        String display = "";
        Method methods[] = obj.getClass().getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            String methodName = methods[i].getName();
            String prefix = methodName.substring(0, 3);
            String fieldName = methodName.substring(3);
            Class retType = methods[i].getReturnType();
            if (prefix.equals("get")) {
                Class classes[] = new Class[1];
                classes[0] = retType;
                try {
                    if (Modifier.isPublic(methods[i].getModifiers())) {
                        Object value = methods[i].invoke(obj, null);
                        display = display + fieldName + " = " + value + "\n";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return display;
    }

    public static Object newInstance(Class persistentObjectClass) {
        try {
            return persistentObjectClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Object newInstance(String className) {
        try {
            BeanUtils.class.getClassLoader().loadClass(className).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invoke(Object o, Method m) {
        try {
            return m.invoke(o);
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


}
