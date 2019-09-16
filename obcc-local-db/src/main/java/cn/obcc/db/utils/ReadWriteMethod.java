package cn.obcc.db.utils;

import java.lang.reflect.Method;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName ReadWriteMethod
 * @desc TODO
 * @date 2019/9/16 0016  17:31
 **/
 class ReadWriteMethod {

    public Method readMethod;
    public Method writeMethod;

    public Method getReadMethod() {
        return readMethod;
    }

    public void setReadMethod(Method readMethod) {
        this.readMethod = readMethod;
    }

    public Method getWriteMethod() {
        return writeMethod;
    }

    public void setWriteMethod(Method writeMethod) {
        this.writeMethod = writeMethod;
    }

    public ReadWriteMethod(Method readMethod, Method writeMethod) {
        super();
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
    }

}