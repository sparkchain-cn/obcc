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
public interface IHashStateStrategy<D> extends IStragegy<D> {

    public void setHashState(String hash, BizState status) throws Exception;

    public BizState getHashState(String hash) throws Exception;

    public boolean existHash(String hash) throws Exception;

    public void delHashState(String hash) throws Exception;


}
