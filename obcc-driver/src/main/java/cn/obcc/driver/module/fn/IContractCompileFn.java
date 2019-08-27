package cn.obcc.driver.module.fn;

import cn.obcc.exception.enums.StateEnum;

import java.util.Map;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName ITransferInfoFn
 * @desc TODO
 * @date 2019/8/27 0027  15:09
 **/
public interface IContractCompileFn {

    /**
     *
     * @param bizId
     * @param hash
     * @param state
     * @param resp
     * @throws Exception
     */
    public void exec(String bizId, String hash, StateEnum state, Map<String, String> resp) throws Exception;

}
