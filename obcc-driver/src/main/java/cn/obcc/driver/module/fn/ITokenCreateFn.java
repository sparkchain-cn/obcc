package cn.obcc.driver.module.fn;

import cn.obcc.exception.enums.StateEnum;

import java.util.Map;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName ITokenCreateFn
 * @desc TODO
 * @date 2019/8/24 0024  16:59
 **/
public interface ITokenCreateFn {
    public void exec(String bizId, String hash, int state, Map<String, String> resp) throws Exception;

}
