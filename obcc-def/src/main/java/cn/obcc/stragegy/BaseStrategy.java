package cn.obcc.stragegy;

import cn.obcc.config.ObccConfig;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName BaseStrategy
 * @desc TODO
 * @date 2019/9/19 0019  8:56
 **/
public class BaseStrategy<D> implements IStragegy<D> {
    protected D driver;

    @Override
    public void init(D driver) throws Exception {
        this.driver = driver;
    }


}
