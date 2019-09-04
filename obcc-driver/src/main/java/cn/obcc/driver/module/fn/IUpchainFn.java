package cn.obcc.driver.module.fn;

import cn.obcc.exception.enums.ETransferStatus;
import cn.obcc.exception.enums.EUpchainType;

import java.util.Map;

/**
 * @author mgicode
 * @version 1.0
 * @ClassName IUpchainFn
 * @desc
 * @data 2019/9/4 9:08
 **/
public interface IUpchainFn<T> {
    public void exec(String bizId, String hash, EUpchainType upchainType, ETransferStatus state, T resp) throws Exception;

}
