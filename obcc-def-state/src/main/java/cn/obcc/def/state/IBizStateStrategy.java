package cn.obcc.def.state;

import cn.obcc.stragegy.IStragegy;
import cn.obcc.vo.BizState;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName INonceStrategy
 * @desc TODO
 * @date 2019/8/26 0026  8:47
 **/
public interface IBizStateStrategy<D> extends IStragegy<D> {


    public void setBizState(String bizId, BizState bizState) throws Exception;

    public BizState getBizState(String bizId) throws Exception;

    public boolean existBizState(String bizId) throws Exception;

    public void delBizState(String bizId) throws Exception;



}
