package cn.obcc.connect.pool.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.obcc.config.conn.pool.ChainNodeWeight;
import cn.obcc.config.conn.pool.ConnPoolConfig;
import cn.obcc.connect.builder.ChainClientBuilder;
import cn.obcc.connect.utils.WeightUtils;

public class ChainPool<T> {

    public static final Logger logger = LoggerFactory.getLogger(ChainPool.class);

    public ConnPoolConfig config;

    public Class<? extends ChainClientBuilder<T>> clientBuilder;

    List<ClientWrapper<T>> pool = Collections.synchronizedList(new ArrayList<>());

    private Semaphore access = null;

    List<ChainNode<T>> chainNodes = Collections.synchronizedList(new ArrayList<>());

    Thread checkTread = null;

    public ChainPool() {
    }

    public void init() throws Exception {

        if (config.getNodeWeights() == null) {
            logger.error("没有配置节点及节点的权重数据，请把控制台中进行配置。");
            return;
        }
        for (ChainNodeWeight nw : (List<ChainNodeWeight>) config.getNodeWeights()) {
            this.chainNodes.add(new ChainNode<>(nw, clientBuilder));
            logger.debug("根据配置的区块链的地址创建连接池的节点：" + nw.getUrl() + "," + nw.getWeight());
        }

        int asize = Math.round(config.getPoolSize() * config.getEnterRate());
        access = new Semaphore(asize);

        initNodes();
        // 生成
        this.chainNodes.forEach((v) -> {
            try {
                v.init();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    /**
     * 处理节点相关
     */
    private void initNodes() {
        // 打乱顺序
        Collections.shuffle(this.chainNodes);
        int nodeSize = chainNodes.size();
        // 初始化每个服务实例中的所需要连接数大小
        int weight = 0;
        for (ChainNode<T> node : chainNodes) {
            node.needCount = 0;
            node.maxCount = 0;
            weight += node.getWeight();
        }
        logger.debug("初始化时总权重为：" + weight);

        // 计算最少连接
        for (int i = 0; i < config.getPoolSize(); i++) {
            int j = i % nodeSize;
            chainNodes.get(j).needCount++;
        }
        // 计算最大连接
        for (ChainNode<T> node : chainNodes) {
            node.maxCount = Math.round(config.getMaxPoolSize() * node.getWeight() / weight);
            logger.debug("url-weight=" + node.getUrl() + "-" + node.getWeight() + " need=" + node.needCount
                    + " max=" + node.maxCount);
        }
    }

    //	/**
//	 * 根据业务ID获取节点连接
//	 *
//	 * @param uuid
//	 */
    public T get(String uuid) {
        for (ChainNode<T> node : chainNodes) {
            for (ClientWrapper<T> w : node.getClientWrapperList()) {
                if (uuid.equals(w.getUuid())) {
                    return w.getClient();
                }
            }
        }
        return null;
    }

//    @Deprecated
//    public T get(String uuid) throws Exception {
//        return this.getNonOccupancyWrapper(uuid).getClient();
//    }

    public T getAndMark(String uuid) throws Exception {
        return this.getNonOccupancyWrapper(uuid).getClient();
    }

    /**
     * 非占用式获取,链<br>
     * 要么采用http，要么采用jsonrpc,不需要进行占用和释放
     *
     * @param uuid
     * @return
     */
    public ClientWrapper<T> getNonOccupancyWrapper(String uuid) throws Exception {
        // 重试三次
        for (int i = 0; i < 3; i++) {
            // 按比重找到节点
            Integer num = WeightUtils.calChainNodeNo(this.chainNodes);
            ChainNode<T> si = this.chainNodes.get(num);
            if (si == null) {
                continue;
            }
            System.out.println(si.getUrl() + " weigth:" + si.getNodeWeight().getWeight());
            if (si.getClientWrapperList() == null || si.getClientWrapperList().size() < 1) {
                logger.error(si.getUrl() + " clientWrapperList的size为0，说明可初始化时其没有初始化成功任何的Client.");
                ClientWrapper<T> wrapper = si.getOptimalWrapper(uuid);
                if (wrapper != null) {
                    WeightUtils.incWeight(si);
                    System.out.println(wrapper.getName() + ":  requestCount:" + wrapper.getRequestCount() + ",IoExceptionCount:"
                            + wrapper.getIoExceptionCount() + ",BizExceptionCount:" + wrapper.getBizExceptionCount() + "," + si.getNodeWeight().getWeight());
                    return wrapper;
                }
                WeightUtils.decWeight(si);
                continue;

            }
            logger.info(si.getUrl() + "-" + si.getWeight() + " |--| " + si.getActiveCount() + " -- ");

            ClientWrapper<T> wrapper = si.getOptimalWrapper(uuid);
            if (wrapper == null) {
                WeightUtils.decWeight(si);
                continue;
            }

            System.out.println(wrapper.getName() + ":  requestCount:" + wrapper.getRequestCount() + ",IoExceptionCount:"
                    + wrapper.getIoExceptionCount() + ",BizExceptionCount:" + wrapper.getBizExceptionCount() + "," + si.getNodeWeight().getWeight());
            return wrapper;
        }
        throw new RuntimeException("can not find any  ChainNode. ");
    }

    public ClientWrapper<T> getWrapper(String uuid) {
        try {
            if (access.tryAcquire(3, TimeUnit.SECONDS)) {
                synchronized (this) {
                    // 重试三次
                    for (int i = 0; i < 3; i++) {
                        ArrayList<Integer> urllist = new ArrayList<>();
                        for (int k = 0; k < chainNodes.size(); k++) {
                            ChainNode<T> node = chainNodes.get(k);
                            for (int j = 0; j < node.getWeight(); j++) {
                                urllist.add(k);
                            }
                        }
                        int random = new Random().nextInt(urllist.size());
                        // 随机负载均衡
                        ChainNode<T> maxInstance = null;
                        ChainNode<T> si = this.chainNodes.get(urllist.get(random));
                        String info = "";// si.getUrl() +"-"+ si.getWeight() +" |--| "+ si.getActiveCount() + " -- " ;
//						System.out.print(info);
                        if (si.getAvailCount() > 0) {
                            maxInstance = si;
                        }
                        if (maxInstance == null) {
                            if (i == 2) {
                                throw new RuntimeException(
                                        info + "找不到有空闲连接的服务实例，" + "出现这种情况说明初始化的连接有很多已经失效，可用的连接数少于指定的连接池数！");
                            }
                        } else {
//							System.out.println("maxInstance==null : " +(maxInstance==null) +"  -- maxIdeNum:"+ maxIdeNum);
                            ClientWrapper<T> wrapper = maxInstance.getWrapper(uuid);
                            if (wrapper != null) {
                                return wrapper;
                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(" can not get available client");
        }
        throw new RuntimeException(" all client is too busy");
    }

    /**
     * 客户端调用完成后，必须手动调用此方法，将TTransport恢复为可用状态
     */
    public void release(ClientWrapper<T> wrapper) {
        boolean released = false;
        synchronized (this) {
            wrapper.setIsBusy(false);
            wrapper.setUuid(null);
            released = true;
        }
        if (released) {
            access.release();
        }
    }

    /**
     * 根据当前节点连接释放
     *
     * @param t
     */
    public void release(T t) {
        for (ChainNode<T> node : chainNodes) {
            for (ClientWrapper<T> w : node.getClientWrapperList()) {
                if (w.getClient().equals(t)) {
                    release(w);
                }
            }
        }
    }

    /**
     * 根据业务ID释放节点连接
     *
     * @param uuid
     */
    public void release(String uuid) {
        for (ChainNode<T> node : chainNodes) {
            for (ClientWrapper<T> w : node.getClientWrapperList()) {
                if ((uuid != null) && (w != null) && uuid.equals(w.getUuid())) {
                    release(w);
                    break;
                }
            }
        }
    }

    /**
     * 获取链连接节点额外配置
     *
     * @param t
     * @return
     */
    public Map<String, Object> getChainNodeExtras(T t) {
        for (ChainNode<T> node : chainNodes) {
            for (ClientWrapper<T> w : node.getClientWrapperList()) {
                if (w.getClient().equals(t)) {
                    return w.getNodeWeight().getExtras();
                }
            }
        }
        return null;
    }

    /**
     * 获取链连接节点额外配置
     *
     * @param uuid
     * @return
     */
    public Map<String, Object> getChainNodeExtras(String uuid) {
        for (ChainNode<T> node : chainNodes) {
            for (ClientWrapper<T> w : node.getClientWrapperList()) {
                if (uuid.equals(w.getUuid())) {
                    return w.getNodeWeight().getExtras();
                }
            }
        }
        return null;
    }

    public void destory() {
        this.chainNodes.forEach((v) -> {
            try {
                v.destory();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        config.setAllowCheck(false);
        checkTread = null;
        System.out.print("连接池被销毁！");
    }

    public int getActiveCount() {
        int result = 0;
        for (ChainNode<T> si : this.chainNodes) {
            result += si.getActiveCount();
        }
        return result;
    }

    public int getBusyCount() {
        int result = 0;
        for (ChainNode<T> si : this.chainNodes) {
            result += si.getBusyCount();
        }
        return result;

    }

    public int getAllCount() {
        int result = 0;
        for (ChainNode<T> si : this.chainNodes) {
            result += si.getClientWrapperList().size();
        }
        return result;

    }

    public int getDeadCount() {

        int result = 0;
        for (ChainNode<T> si : this.chainNodes) {
            result += si.getDeadCount();
        }
        return result;

    }

    public String getWrapperInfo(T client) {
        for (int i = 0; i < config.getPoolSize(); i++) {
            if (pool.get(i).getClient() == client) {
                return pool.get(i).toString();
            }
        }
        return "";
    }

    public void updateDel(ChainNodeWeight p) throws Exception {

        ChainNode<T> select = null;
        for (ChainNode<T> si : this.chainNodes) {
            if (isEq(si, p)) {
                // 注册中心的健康检查与实际的连接可能对不上
                if (!si.isUp()) {
                    si.destory();
                    select = si;
                }
                break;
            }
        }

        if (select != null) {
            this.chainNodes.remove(select);
            this.rebuild();
        }
    }

    public void updateAdd(ChainNodeWeight node) throws Exception {
        for (ChainNode<T> si : this.chainNodes) {
            // 同一个ip:host发布一个服务，相等说明存在
            if (isEq(si, node)) {
                System.out.println(node.getUrl() + "已经存在");
                return;
            }
        }

        // 不存在
        ChainNode<T> si = new ChainNode<T>(node, clientBuilder);
        this.chainNodes.add(si);
        this.rebuild();

    }

    public boolean isEq(ChainNode<T> p, ChainNodeWeight si) {
        return si.getUrl().equalsIgnoreCase(p.getUrl());

    }

    public void rebuild() throws Exception {
        synchronized (this) {
            initNodes();
            // 生成
            this.chainNodes.forEach((v) -> {
                try {
                    v.rebuild();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
        }

    }

    public void destoryAndRebuild() {
        this.destory();
        try {
            this.rebuild();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查空闲连接
     */
    // private void check() {
    // Thread.start((Runnable) () -> {
    // HeartBeat heartBeat = new HeartBeat(this);
    // while (allowCheck) {
    // heartBeat.checkTask();
    // }
    // });
    //
    // }

}
