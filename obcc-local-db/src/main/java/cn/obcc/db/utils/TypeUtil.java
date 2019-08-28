package cn.obcc.db.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author pengrk
 * @email:sjkjs155@126.com
 * @wetsite:www.mgicode.com
 * @license:GPL
 */
public class TypeUtil {
    /**
     * 判断是否是值类型
     **/

    public static boolean isSingle(Class<?> clazz) {

        return isBoolean(clazz) || isNumber(clazz) || isString(clazz);

    }

    /**
     * 是否布尔值
     **/

    public static boolean isBoolean(Class<?> clazz) {

        return (clazz != null)

                && ((Boolean.TYPE.isAssignableFrom(clazz)) || (Boolean.class

                .isAssignableFrom(clazz)));

    }

    public static boolean isShort(Class<?> clazz) {
        return Byte.TYPE.isAssignableFrom(clazz)
                || Short.TYPE.isAssignableFrom(clazz);
    }

    public static boolean isInteger(Class<?> clazz) {
        return Integer.TYPE.isAssignableFrom(clazz);
        //|| Short.TYPE.isAssignableFrom(clazz);
    }

    public static boolean isLong(Class<?> clazz) {
        return Long.TYPE.isAssignableFrom(clazz);
        //|| Short.TYPE.isAssignableFrom(clazz);
    }

    public static boolean isFloat(Class<?> clazz) {
        return Float.TYPE.isAssignableFrom(clazz);
        //|| Short.TYPE.isAssignableFrom(clazz);
    }

    public static boolean isDouble(Class<?> clazz) {
        return Float.TYPE.isAssignableFrom(clazz);
        //|| Short.TYPE.isAssignableFrom(clazz);
    }

    /**
     * 是否数值
     **/

    public static boolean isNumber(Class<?> clazz) {

        return (clazz != null)

                && ((Byte.TYPE.isAssignableFrom(clazz))
                || (Short.TYPE.isAssignableFrom(clazz))

                || (Integer.TYPE.isAssignableFrom(clazz))

                || (Long.TYPE.isAssignableFrom(clazz))

                || (Float.TYPE.isAssignableFrom(clazz))

                || (Double.TYPE.isAssignableFrom(clazz)) || (Number.class

                .isAssignableFrom(clazz)));

    }


    /**
     * 判断是否是字符串
     **/

    public static boolean isString(Class<?> clazz) {

        return (clazz != null)

                && ((String.class.isAssignableFrom(clazz))

                || (Character.TYPE.isAssignableFrom(clazz)) || (Character.class

                .isAssignableFrom(clazz)));

    }

    /**
     * 判断是否是对象
     **/

    public static boolean isObject(Class<?> clazz) {

        return clazz != null && !isSingle(clazz) && !isArray(clazz)
                && !isCollection(clazz);

    }

    /**
     * 判断是否是数组
     **/

    public static boolean isArray(Class<?> clazz) {

        return clazz != null && clazz.isArray();

    }

    /**
     * 判断是否是集合
     **/

    public static boolean isCollection(Class<?> clazz) {

        return clazz != null && Collection.class.isAssignableFrom(clazz);

    }

    /**
     * 判断是否是列表
     *
     * @param clazz
     * @return
     */
    public static boolean isList(Class<?> clazz) {
        return clazz != null && List.class.isAssignableFrom(clazz);
    }

    /**
     * 判断是否是Map
     *
     * @param clazz
     * @return
     */
    public static boolean isMap(Class<?> clazz) {
        return clazz != null && Map.class.isAssignableFrom(clazz);
    }

}
