package cn.obcc.utils.base;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class MapUtils {

    public static <T> boolean isNullOrEmpty(Map<String, T> map, String key) {
        if (map == null) {
            return true;
        }
        if (!map.containsKey(key)) {
            return true;
        }
        if (map.get(key) == null) {
            return true;
        }
        return false;
    }

    public static <T> boolean isNullOrEmptyOrNoExist(Map<String, List<T>> map, String key) {
        if (map == null) {
            return true;
        }
        if (!map.containsKey(key)) {
            return true;
        }
        if (map.get(key) == null) {
            return true;
        }
        List list = map.get(key);
        if (list.size() < 0) {
            return true;
        }

        return false;
    }

    public static Map<String, String> mapObj2mapStr(Map<String, Object> params) {
        Map<String, String> map = new HashMap<>();
        for (String k : params.keySet()) {
            map.put(k, params.get(k) + "");
        }
        return map;
    }

    public static boolean containAndEq(Map map, String key, Object value) {
        if (map == null) {
            return false;
        }
        if (!map.containsKey(key)) {
            return false;
        }
        if (value instanceof String) {
            return value.equals(map.get(key));
        }
        if (map.get(key) == value) {
            return true;
        }

        return false;

    }

    public static void putIsNotNull(Map map, String key, Object value) {
        if (value != null) {
            map.put(key, value);
        }
    }

    public static void putIfNullDefault(Map map, String key, Object value, Object defaultValue) {
        if (value != null) {
            map.put(key, value);
        } else {
            map.put(key, defaultValue);
        }
    }

    public static String getIfNullDefault(Map map, String key, String defaultValue) {
        if (map == null) {
           // return null;
            return defaultValue;
        }
        Object value = map.get(key);
        if (value != null) {
            return String.valueOf(value);
        } else {
            return defaultValue;
        }
    }

    public static <T> T getObjIfNullDefault(Map map, String key, T defaultValue) {
        if (map == null) {
            //return null;
            return defaultValue;
        }
        Object value = map.get(key);
        if (value != null) {
            return (T) value;
        } else {
            return defaultValue;
        }
    }

    public static String get(Map map, String key) {
        if (map == null) {
            return null;
        }
        Object value = map.get(key);
        if (value != null) {
            return String.valueOf(value);
        } else {
            return null;
        }
    }

    public static Map clone(Map source) {
        return copy(source, null, false, false);
    }

    public static Map fetchFromRequestParameterMap(Map source, boolean only, boolean force) {
        return dealDotJson(copy(source, null, only, force));
    }

    /**
     * @param map1
     * @return
     * @author:彭仁夔 于2014年11月3日上午8:31:21创建
     */
    public static Map dealDotJson(Map map1) {

        Map map = new HashMap();
        boolean loop = false;
        for (Object s : map1.keySet()) {
            if (s instanceof String) {
                buildDotMap((String) s, map1.get(s), map);
            } else {
                Object v = map1.get(s);
                if (v instanceof String && v != null) {
                    map.put(s, ((String) v).trim());
                } else {
                    map.put(s, v);
                }

            }

        }
        return map;
    }

    /**
     * @param s
     * @param v
     * @param map
     * @author:彭仁夔 于2014年11月3日上午8:46:56创建,，trim string
     */
    public static void buildDotMap(String s, Object v, Map map) {
        if (StringUtils.isNullOrEmpty(s)) {
            return;
        }
        String[] strs = s.split(".");
        if (strs.length > 1) {
            int subpos = s.indexOf(".");
            String sub = s.substring(subpos);

            Map cmap = new HashMap();
            map.put(strs[0], cmap);

            if (sub.split("[.]").length > 1) {
                buildDotMap(sub, v, cmap);
            } else {
                if (v instanceof String && v != null) {
                    cmap.put(sub, ((String) v).trim());
                } else {
                    cmap.put(sub, v);
                }
            }
        } else {
            if (v instanceof String && v != null) {
                map.put(s, ((String) v).trim());
            } else {
                map.put(s, v);
            }
        }

    }

    // /**
    // * 去除id为null的关联对象，条件id的名称统一为id,
    // *
    // * @param map
    // * @return
    // * @author:彭仁夔 于2014年11月4日下午4:33:39创建
    // */
    // public static Map removeEmptyObj(Map map) {
    // Map retMap = new HashMap();
    //
    // for (Object obj : map.keySet()) {
    // Object val = map.get(obj);
    // if (val instanceof List) {
    // for (Object ov : (List) val) {
    // if (ov instanceof Map) {
    // Map vMap = (Map) val;
    //
    // if (vMap.containsKey("id") && vMap.get("id") == null) {
    //
    // } else {
    // retMap.put(obj, removeEmptyObj(vMap));
    // }
    //
    // } else {
    //
    // }
    // }
    //
    // } else if (val instanceof Map) {
    // Map vMap = (Map) val;
    // if (vMap.containsKey("id") && vMap.get("id") == null) {
    // } else {
    // retMap.put(obj, removeEmptyObj(vMap));
    // }
    //
    // } else {
    // retMap.put(obj, map.get(obj));
    // }
    //
    // }
    // return retMap;
    // }
    public static Map copy(Map source, Map dest, boolean only, boolean force) {

        if (dest == null) {
            dest = new HashMap<Object, Object>();
        }
        for (Object obj : source.keySet()) {
            if (only == true) {
                Object object = source.get(obj);
                if (object.getClass().isArray()) {
                    Object[] objs = (Object[]) object;
                    if (objs == null || objs.length == 0) {
                        continue;
                    }
                    Object o = objs[0];
                    if (objs.length == 1 || force == true) {
                        dest.put(obj, o);
                    } else {
                        dest.put(obj, source.get(obj));

                    }

                }
            } else {
                dest.put(obj, source.get(obj));
            }
        }

        return dest;
    }

    public static void remove(String names, Map map) {

        if (StringUtils.isNullOrEmpty(names) || map == null) {
            return;
        }

        String[] ns = names.split(",");
        if (ns != null) {
            for (String os : ns) {
                String s = os.trim();
                if (map.containsKey(s)) {
                    map.remove(s);
                }
            }
        }
    }

    /**
     * 将一个 Map 对象转化为一个 JavaBean
     *
     * @param type 要转化的类型
     * @param map  包含属性值的 map
     * @return 转化出来的 JavaBean 对象
     * @throws IntrospectionException    如果分析类属性失败
     * @throws IllegalAccessException    如果实例化 JavaBean 失败
     * @throws InstantiationException    如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    public static <T> T map2Obj(Class<T> type, Map map) {
        BeanInfo beanInfo = null; // 获取类属性
        try {
            beanInfo = Introspector.getBeanInfo(type);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        T obj = null; // 创建 JavaBean 对象
        try {
            obj = type.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();

            if (map.containsKey(propertyName)) {
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                try {
                    Object value = map.get(propertyName);
                    if (value instanceof String) {
                        // String 进行转换
                        Class<?> t = descriptor.getPropertyType();
                        // BeanUtil.isSimpleType(property)
                        if (String.class.isAssignableFrom(t)) {

                        } else if (Integer.class.isAssignableFrom(t)) {
                            value = Integer.parseInt(value + "");
                        } else if (Boolean.class.isAssignableFrom(t)) {
                            value = Boolean.parseBoolean(value + "");
                        } else if (Date.class.isAssignableFrom(t)) {
                            value = DateUtils.parseDateFormat(value + "");
                        }
                    }

                    Object[] args = new Object[1];
                    args[0] = value;

                    try {
                        descriptor.getWriteMethod().invoke(obj, args);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {

                }
            }

        }
        return obj;
    }

    public static <T> Map<String, Object> map2Map(Class<T> type, Map<String, ?> map, String... names) {

        Map<String, Object> retMap = new HashMap<>();

        BeanInfo beanInfo = null; // 获取类属性
        T obj = null; // 创建 JavaBean 对象
        try {
            beanInfo = Introspector.getBeanInfo(type);
            obj = type.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        try {
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (names != null && names.length > 0) {
                    if (map.containsKey(propertyName) && StringUtils.contain(names, propertyName)) {
                        // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                        Object value = map.get(propertyName);
                        if (value instanceof String) {
                            if (StringUtils.isNullOrEmpty((String) value)) {
                                retMap.put(propertyName, value);
                                continue;
                            }
                            // String 进行转换
                            Class<?> t = descriptor.getPropertyType();
                            // BeanUtil.isSimpleType(property)
                            if (String.class.isAssignableFrom(t)) {

                            } else if (Integer.class.isAssignableFrom(t)) {
                                value = Integer.parseInt(value + "");
                            } else if (Boolean.class.isAssignableFrom(t)) {
                                value = Boolean.parseBoolean(value + "");
                            } else if (Date.class.isAssignableFrom(t)) {
                                value = DateUtils.parseDateFormat(value + "");
                            }
                        }
                        retMap.put(propertyName, value);
                    }
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return retMap;
    }

    /**
     * 将一个 JavaBean 对象转化为一个 Map
     *
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的 Map 对象
     * @throws IntrospectionException    如果分析类属性失败
     * @throws IllegalAccessException    如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    public static Map obj2Map(Object bean)
            throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class type = bean.getClass();
        Map returnMap = new HashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);

        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }

    public static Map obj2Map(Object bean, String... excludename) {
        Class type = bean.getClass();
        Map returnMap = new HashMap();
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(type);
        } catch (IntrospectionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class") || !StringUtils.contain(excludename, propertyName)) {
                Method readMethod = descriptor.getReadMethod();
                Object result = null;
                try {
                    result = readMethod.invoke(bean, new Object[0]);
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
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }

    public static List<Map> ListObjToListMap(List list) {
        if (list == null) {
            return null;
        }
        List<Map> retList = new ArrayList<>();
        for (Object o : list) {
            try {
                retList.add(obj2Map(o));
            } catch (IntrospectionException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }

        return retList;

    }

    public static List<Map> listTree2ListMapTree(List list, String childrenName) {
        if (list == null) {
            return null;
        }
        List<Map> retList = new ArrayList<>();
        for (Object o : list) {
            retList.add(treeObj2TreeMap(o, childrenName));
        }
        return retList;
    }

    public static <T> Map treeObj2TreeMap(T node, String childrenName) {

        Map map = new HashMap<>();

        map = obj2Map(node, childrenName);

        List<Object> children = (List) BeanUtils.readFieldValue(node, childrenName);

        if (children != null) {
            List<Map> childrenList = new ArrayList<>();
            for (Object child : children) {
                Map childMap = treeObj2TreeMap(child, childrenName);
                childrenList.add(childMap);
            }
            map.put(childrenName, childrenList);
        }

        return map;

    }

    public static boolean containAndNotNull(Map<String, ?> map, String key) {
        if (!map.containsKey(key)) {
            return false;
        }
        Object v = map.get(key);
        if (v == null) {
            return false;
        }
        if (v instanceof String) {
            if (StringUtils.isNullOrEmpty((String) v)) {
                return false;
            }
        }
        return true;
    }
}
