package cn.obcc.connect.pool.fn;

/**
 * @author mgicode
 * @version 1.0
 * @ClassName ClientAdviceFn
 * @desc
 * @data 2019/9/5 8:43
 **/
public interface ClientAdviceFn {

    /**
     * 监控请求的数量
     *
     * @throws Exception
     */
    public default void requestCountInc() {
    }

    /**
     * 反映应用与链节点之间的网络连接情况
     *
     * @throws Exception
     */
    public default void ioErrorCountInc() {

    }

    /**
     * 反映链节点的稳定情况，如井通节点 no skywell sync
     * <br>节点没有维护好，经常报这个错误，说明节点不稳定     *
     *
     * @throws Exception
     */
    public default void bizErrorCountInc() {

    }


}
