package cn.obcc.connect.builder;

import cn.obcc.connect.pool.core.ChainPool;
import cn.obcc.connect.pool.fn.ClientAdviceFn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ChainClientBuilder<T> {


    public static final Logger logger = LoggerFactory.getLogger(ChainPool.class);

    /**
     * 初始化，把连接池相关属性传入ClientBuilder中<br>
     * 实现它即可以获得相关属性
     *
     * @param url
     * @param fn  //@param weight
     */
//    public default void init(String url, ClientWrapper clientWrapper, ChainNodeWeight weight) {
//    }
    public ChainClientBuilder<T> init(String url, ClientAdviceFn fn);

    /**
     * 创建区域链的客户端的对象，如ETH的web3j,Moac的chain3j
     * <p>
     * //@param url
     *
     * @return
     * @throws Exception
     */
    //public T newNativeClient(String url) throws Exception;
    public T newNativeClient() throws Exception;

    /**
     * 判断Client是否死掉
     *
     * @param client
     * @return
     */
    public boolean isDead(T client);

    /**
     * 判断客户端的连接是否打开
     *
     * @param client
     * @return
     */
    public boolean isOpen(T client);

    /**
     * 关闭客户端的连接
     *
     * @param client
     * @throws Exception
     */
    public void close(T client) throws Exception;

    /**
     * 打开客户端的连接
     *
     * @param client
     * @return
     * @throws Exception
     */
    public boolean open(T client) throws Exception;


    public ClientAdviceFn getCallback();
}
