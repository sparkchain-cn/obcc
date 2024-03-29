package cn.obcc.driver.tech;

import cn.obcc.driver.IDriverHandler;
import cn.obcc.vo.driver.ChainPipe;

/**
 * @author mgicode
 * @desc ISpeedAdjuster
 * @email 546711211@qq.com
 * @date 2019年8月22日 上午10:43:28
 */
public interface ISpeedAdjuster<T> extends IDriverHandler<T> {
    /**
     * 开启一个线程
     *
     * @throws Exception
     */
    public void start() throws Exception;

    public void offer(ChainPipe obj) throws Exception;

    public void close() throws Exception;

}
