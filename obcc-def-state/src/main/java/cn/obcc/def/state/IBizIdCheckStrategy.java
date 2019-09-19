package cn.obcc.def.state;

import cn.obcc.stragegy.IStragegy;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName IBizIdCheckStrategy
 * @desc TODO
 * @date 2019/9/19 0019  13:35
 **/
public interface IBizIdCheckStrategy<D> extends IStragegy<D> {

    public void setBizId(String bizId) throws Exception;

    public boolean existBizId(String bizId) throws Exception;


}

