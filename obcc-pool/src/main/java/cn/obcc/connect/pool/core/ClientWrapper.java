package cn.obcc.connect.pool.core;

import java.util.Date;
import java.util.Random;

import cn.obcc.vo.pool.ChainNodeWeight;
import cn.obcc.connect.builder.ChainClientBuilder;
import cn.obcc.connect.pool.fn.ClientAdviceFn;

public class ClientWrapper<T> {

    Random random = new Random();
    private String name;
    private long starttime;
    private long endttime;

    private long ioExceptionCount = 0;

    private long bizExceptionCount = 0;

    private long requestCount = 0;


    private T client;
    /**
     * 服务端Server主机名或IP
     */
//	private String url;
    private ChainNodeWeight nodeWeight;

    private String uuid;
    /**
     * 是否正忙
     */
    private boolean isBusy = false;

    private boolean isOpen = false;

    public ChainClientBuilder<T> clientBuilder;

    public ChainNode<T> chainNode;

    /**
     * 最后使用时间
     */
    private Date lastUseTime;


    ClientAdviceFn fn = new ClientAdviceFn() {
        @Override
        public void ioErrorCountInc() {
            ClientWrapper.this.ioExceptionCount = ClientWrapper.this.ioExceptionCount + 1;
            ClientWrapper.this.chainNode.ioExceptionCountInc();
        }

        @Override
        public void requestCountInc() {
            ClientWrapper.this.requestCount = ClientWrapper.this.requestCount + 1;
            ClientWrapper.this.chainNode.requestCountInc();
        }

        @Override
        public void bizErrorCountInc() {
            ClientWrapper.this.bizExceptionCount = ClientWrapper.this.bizExceptionCount + 1;
            ClientWrapper.this.chainNode.bizExceptionCountInc();
        }
    };

    public ClientWrapper(ChainNodeWeight nodeWeight, ChainClientBuilder<T> clientBuilder, ChainNode chainNode) throws Exception {
        this.lastUseTime = new Date();
        this.nodeWeight = nodeWeight;
        this.clientBuilder = clientBuilder;
        this.chainNode = chainNode;
        this.name = this.chainNode.getUrl() + ":" + this.lastUseTime.getTime() + ":" + random.nextInt(100000000);
        // this.clientBuilder.init(nodeWeight.getUrl(), this, nodeWeight);
        this.clientBuilder.init(nodeWeight.getUrl(), fn);
        //   this.client = clientBuilder.newNativeClient(nodeWeight.getUrl());
        this.client = newNativeClient();
        if (client != null) {
            isOpen = true;
        }

    }

    public T newNativeClient() throws Exception {
        this.client = clientBuilder.newNativeClient();
        return client;
    }

    //
    public boolean isDead() {
        return clientBuilder.isDead(client);
    }

    //
    public boolean isOpen() {
        return clientBuilder.isOpen(client);
    }

    public boolean open() throws Exception {
        boolean flag = clientBuilder.open(client);
        this.starttime = System.currentTimeMillis();
        return flag;
    }

    public void close() throws Exception {
        clientBuilder.close(client);
        this.endttime = System.currentTimeMillis();
    }

    public boolean isBusy() {
        return isBusy;
    }

    public void setIsBusy(boolean isBusy) {
        this.isBusy = isBusy;
    }

    public T getClient() {
        return client;
    }

    public void setClient(T transport) {
        this.client = transport;
    }

    /**
     * 当前transport是否可用
     *
     * @return
     */
    public boolean isAvailable() {
        return !isBusy && !isDead() && isOpen();
    }

    public Date getLastUseTime() {
        return lastUseTime;
    }

    public void setLastUseTime(Date lastUseTime) {
        this.lastUseTime = lastUseTime;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public ChainNodeWeight getNodeWeight() {
        return nodeWeight;
    }

    public void setNodeWeight(ChainNodeWeight nodeWeight) {
        this.nodeWeight = nodeWeight;
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

    public String getName() {
        return name;
    }
}
