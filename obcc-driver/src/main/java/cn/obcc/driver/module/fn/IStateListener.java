package cn.obcc.driver.module.fn;

import cn.obcc.vo.BizState;

/**
 * @author mgicode
 * @version 1.0
 * @ClassName IUpchainFn
 * @desc
 * @data 2019/9/4 9:08
 **/
public interface IStateListener {
    public void exec(BizState bizState, Object resp) throws Exception;

}
