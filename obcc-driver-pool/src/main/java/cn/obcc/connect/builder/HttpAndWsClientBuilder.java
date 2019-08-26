package cn.obcc.connect.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.obcc.config.conn.pool.ChainNodeWeight;
import cn.obcc.connect.pool.core.ClientWrapper;
import cn.obcc.connect.utils.WeightUtils;

public abstract class HttpAndWsClientBuilder<T> implements ChainClientBuilder<T> {

    final static Logger logger = LoggerFactory.getLogger(HttpAndWsClientBuilder.class);
    protected String url;
    protected ChainNodeWeight nodeWeight;
    protected ClientWrapper<T> clientWrapper;

    //type:1:http,type:2 :ws
    protected Integer type = 0;


    public void init(String url, ClientWrapper clientWrapper, ChainNodeWeight weight) {
        setUrl(url);
        this.nodeWeight = weight;
        this.clientWrapper = clientWrapper;

    }

    public void setUrl(String url) {
        this.url = url;
        if (WeightUtils.isHttp(url)) {
            this.type = 1;
        } else {
            this.type = 2;
        }
    }

    @Override
    public abstract T newNativeClient(String url) throws Exception;

    @Override
    public boolean isDead(T client) {
        if (this.type == 1) {
            return isHttpDead(client);
        } else {
            return isWsDead(client);
        }
    }


    public abstract boolean isWsDead(T client);

    public boolean isHttpDead(T client) {
        return false;
    }

    @Override
    public boolean isOpen(T client) {
        if (this.type == 1) {
            return isHttpOpen(client);
        } else {
            return isWsOpen(client);
        }
    }

    public abstract boolean isWsOpen(T client);

    public boolean isHttpOpen(T client) {
        return true;
    }

    @Override
    public abstract void close(T client) throws Exception;


    @Override
    public boolean open(T client) throws Exception {
        return false;
    }


    public void requestCountInc() {
        clientWrapper.requestCountInc();
    }

    public void ioErrorCountInc() {
        clientWrapper.ioExceptionCountInc();
    }

    public void bizErrorCountInc() {
        clientWrapper.bizExceptionCountInc();
    }

}
