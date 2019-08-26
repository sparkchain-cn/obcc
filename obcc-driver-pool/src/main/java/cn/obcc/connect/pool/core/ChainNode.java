package cn.obcc.connect.pool.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.obcc.config.conn.pool.ChainNodeWeight;
import cn.obcc.connect.builder.ChainClientBuilder;
import cn.obcc.utils.retry.RetryUtils;

public class ChainNode<T> {

    public static final Logger logger = LoggerFactory.getLogger(ChainNode.class);
    //	private String url;
//	private Integer weight;
    private ChainNodeWeight nodeWeight;
    private long ioExceptionCount = 0;

    private long bizExceptionCount = 0;

    private long requestCount = 0;


    Random random = new Random();
    // 调用方式：wss,http，同一台服务器两个都支持，当作两个ChainNode
    private String way;

    private List<ClientWrapper<T>> clientWrapperList = new ArrayList<>();


    private Class<? extends ChainClientBuilder<T>> builder;
    public int needCount = 0;
    public int maxCount = 0;

    public ChainNode(ChainNodeWeight nodeWeight, String way, Class<? extends ChainClientBuilder<T>> builder) {
        super();
        this.way = way;
        this.nodeWeight = nodeWeight;
        this.builder = builder;
    }

    public ChainNode(ChainNodeWeight nodeWeight, Class<? extends ChainClientBuilder<T>> builder) {
        super();
        this.nodeWeight = nodeWeight;
        this.builder = builder;
    }

    public void init() throws Exception {
        if (needCount == 0) {
            logger.debug(nodeWeight.getUrl() + ":needCount:0");
            return;
        }
        int nc = needCount - clientWrapperList.size();
        if (nc <= 0) {
            removeTo(needCount);
            return;
        }
        int ipc = needCount - getActiveCount();
        for (int i = 0; i < nc; i++) {
            if (i < ipc) {
                long start = System.currentTimeMillis();
                ClientWrapper<T> tw = RetryUtils.retry(3, 1000, () -> {
                    ClientWrapper<T> twInner = new ClientWrapper<>(nodeWeight, builder.newInstance(), this);
                    return twInner;
                });
                if (tw != null && tw.isOpen()) {
                    clientWrapperList.add(tw);
                }
                long end = System.currentTimeMillis();
                logger.debug("创建" + nodeWeight.getUrl() + " clientWrap时间:" + (end - start));

            }

        }

    }

    public void removeTo(int needCount) throws Exception {
        removeDead();
        if (clientWrapperList.size() == needCount)
            return;

        if (clientWrapperList.size() < needCount) {
            addTo(needCount);
            return;
        }
        int need = clientWrapperList.size() - needCount;
        if (clientWrapperList != null) {
            List<ClientWrapper<T>> list = new ArrayList<>();
            int j = 0;
            for (ClientWrapper<T> tw : clientWrapperList) {
                if (!tw.isBusy() && j <= need) {
                    tw.close();
                    list.add(tw);
                    j++;
                }
            }
            clientWrapperList.removeAll(list);

        }
    }

    public void addTo(int needCount) throws Exception {
        int need = needCount = clientWrapperList.size();
        for (int i = 0; i < need; i++) {
            ClientWrapper<T> tw = new ClientWrapper<>(nodeWeight, builder.newInstance(), this);
            if (tw.isOpen()) {
                clientWrapperList.add(tw);
            }
        }
    }

    public void destory() throws Exception {
        if (clientWrapperList != null) {
            for (ClientWrapper<T> tw : clientWrapperList) {
                tw.close();
            }
        }

    }

    public int getActiveCount() {
        int result = 0;
        for (ClientWrapper<T> tw : clientWrapperList) {
            if (!tw.isDead() && tw.isOpen()) {
                result += 1;
            }
        }
        return result;
    }

    public boolean isUp() throws Exception {

        int n = getActiveCount();
        if (n > 0) {
            return true;
        }
        if (clientWrapperList.size() > 0) {
            ClientWrapper<T> tw = clientWrapperList.get(0);
            try {
                tw.open();
                return true;
            } catch (Exception e) {
                tw.close();
                return false;
            }
        } else {
            ClientWrapper<T> tw = new ClientWrapper<>(nodeWeight, builder.newInstance(), this);
            if (tw == null || tw.isDead()) {
                return false;
            } else {
                if (tw.isOpen()) {
                    clientWrapperList.add(tw);
                    return true;
                }
            }
        }
        return false;
    }

    public int getBusyCount() {
        int result = 0;
        for (ClientWrapper<T> tw : clientWrapperList) {
            if (!tw.isDead() && tw.isBusy()) {
                result += 1;
            }
        }
        return result;
    }

    /**
     * 可用连接数
     *
     * @return
     */
    public int getAvailCount() {
        int result = 0;
        for (ClientWrapper<T> tw : clientWrapperList) {
            if (!tw.isDead() && !tw.isBusy()) {
                result += 1;
            }
        }
//		System.out.println(String.format("getAvailCount() 列表总数: %s , 返回数据: %s ", clientList.size(), result));
        return result;
    }

    /**
     * 已消亡连接数量
     *
     * @return
     */
    public int getDeadCount() {
        int result = 0;
        for (ClientWrapper<T> tw : clientWrapperList) {
            if (tw.isDead()) {
                result += 1;
            }
        }
        return result;
    }

    public ClientWrapper<T> getWrapper(String uuid) throws Exception {

        if (clientWrapperList == null || clientWrapperList.size() < 1) {
            logger.error(getUrl() + "clientWrapperList的size为0，说明可初始化时其没有初始化成功任何的Client.");
            return tryMaxClient(uuid);
        }

        for (ClientWrapper<T> tw : clientWrapperList) {
            if (tw.isAvailable()) {
                tw.setIsBusy(true);
                tw.setLastUseTime(new Date());
                tw.setUuid(uuid);
                return tw;
            }
        }
        // 尝试激活更多连接
        ClientWrapper<T> ret = tryMaxClient(uuid);
        return ret;
    }

    private ClientWrapper<T> tryMaxClient(String uuid) throws Exception {
        if (clientWrapperList.size() < maxCount) {
            try {
                ClientWrapper<T> tw = new ClientWrapper<>(nodeWeight, builder.newInstance(), this);
                tw.setIsBusy(true);
                tw.setLastUseTime(new Date());
                tw.setUuid(uuid);
                if (tw.isOpen()) {
                    clientWrapperList.add(tw);
                }
                return tw;
            }catch (Exception e){e.printStackTrace();}
            finally {
                return null;
            }
        }
        return null;
    }

    public ClientWrapper<T> getOptimalWrapper(String uuid) throws Exception {
        if (clientWrapperList == null || clientWrapperList.size() < 1) {
            logger.error(getUrl() + "clientWrapperList的size为0，说明可初始化时其没有初始化成功任何的Client.");
            return tryMaxClient(uuid);
        }

        int rand = random.nextInt(clientWrapperList.size());
        ClientWrapper<T> ret = clientWrapperList.get(rand);
        if (ret != null && ret.isOpen()) {
            return ret;
        }
        logger.debug("getOptimalWrapper:uuid:" + uuid + ",clientWrapperListSize:" + clientWrapperList.size() + ",maxCount" + maxCount);
        // 尝试激活更多连接
        ret = tryMaxClient(uuid);

        return ret;
    }

    /**
     * 重建连接
     *
     * @throws Exception
     */
    public void rebuild() throws Exception {
        this.removeDead();
        this.init();
    }

    public void removeDead() {

        List<ClientWrapper<T>> list = new ArrayList<>();

        for (ClientWrapper<T> tw : clientWrapperList) {
            if (tw.isDead()) {
                list.add(tw);
                try {
                    tw.close();
                } catch (Exception e) {
                }
            }
        }
        clientWrapperList.removeAll(list);

    }

    public String getUrl() {
        return nodeWeight.getUrl();
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public Integer getWeight() {
        return nodeWeight.getWeight();
    }

    public List<ClientWrapper<T>> getClientWrapperList() {
        return clientWrapperList;
    }

    public void setClientWrapperList(List<ClientWrapper<T>> clientList) {
        this.clientWrapperList = clientList;
    }


    public void ioExceptionCountInc() {
        this.ioExceptionCount = this.ioExceptionCount + 1;
    }

    public void requestCountInc() {
        this.requestCount = this.requestCount + 1;
    }

    public void bizExceptionCountInc() {
        this.bizExceptionCount = this.bizExceptionCount + 1;
    }

    public long getIoExceptionCount() {
        return ioExceptionCount;
    }

    public long getBizExceptionCount() {
        return bizExceptionCount;
    }

    public long getRequestCount() {
        return requestCount;
    }

    public ChainNodeWeight getNodeWeight() {
        return nodeWeight;
    }
}
