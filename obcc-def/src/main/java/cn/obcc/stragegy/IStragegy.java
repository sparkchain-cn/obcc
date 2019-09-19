package cn.obcc.stragegy;

import cn.obcc.config.ObccConfig;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName IStragegy
 * @desc TODO
 * @date 2019/9/19 0019  8:52
 **/
public interface IStragegy<D> {

    public void init(D driver) throws Exception;

    public default void onAfterInit() throws Exception {
    }

    public default void destory() throws Exception {
    }

}
