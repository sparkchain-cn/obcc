package cn.obcc.driver;

import cn.obcc.driver.module.fn.IStateListener;

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
    public void register(String bizId, IStateListener fn) throws Exception;

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
    public IStateListener getStateListener(String bizId);

    /**
     * @param bizId
     * @throws Exception
     */
    public void unRegister(String bizId) throws Exception;
}
