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

    //如果该什是ChainDriver的话，那么一定要注意，防止循环调用
    protected D driver;

    @Override
    public void init(D driver) throws Exception {
        this.driver = driver;
    }

    @Override
    public void onAfterInit() throws Exception {

    }

}
