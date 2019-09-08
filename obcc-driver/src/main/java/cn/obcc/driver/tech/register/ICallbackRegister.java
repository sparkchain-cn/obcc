package cn.obcc.driver.tech.register;

import cn.obcc.driver.module.fn.IUpchainFn;

/**
 * @author mgicode
 * @version 1.0
 * @ClassName ICallbackRegister
 * @desc
 * @data 2019/9/4 9:46
 **/
public interface ICallbackRegister {
    /**
     * 上链时，把回调函数注册进入<br>
     * 区块流水回调味时回调该方法
     * 20分钟自动过期
     *
     * @param bizId
     * @param fn
     */
    public void register(String bizId, IUpchainFn fn) throws Exception;

    /**
     *
     * @param bizId
     * @return
     * @throws Exception
     */
    public boolean exist(String bizId) throws Exception;

    /**
     * 获取回调函数
     *
     * @param bizId
     * @return
     */
    public IUpchainFn getCallbackFn(String bizId);

    /**
     * @param bizId
     * @throws Exception
     */
    public void unRegister(String bizId) throws Exception;
}
