package cn.obcc.connect.builder;

import cn.obcc.connect.pool.fn.ClientAdviceFn;
import cn.obcc.connect.utils.WeightUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseHttpAndWsClientBuilder<T> implements ChainClientBuilder<T> {

    final static Logger logger = LoggerFactory.getLogger(BaseHttpAndWsClientBuilder.class);
    protected String url;
    ClientAdviceFn callback;

    //type:1:http,type:2 :ws
    protected Integer type = 0;


    @Override
    public ChainClientBuilder init(String url, ClientAdviceFn fn) {
        setUrl(url);
        this.callback = fn;
        return this;
    }

    @Override
    public ClientAdviceFn getCallback() {
        return callback;
    }

    public void setCallback(ClientAdviceFn callback) {
        this.callback = callback;
    }

    private void setUrl(String url) {
        this.url = url;
        if (WeightUtils.isHttp(url)) {
            this.type = 1;
        } else {
            this.type = 2;
        }
    }

    @Override
    public boolean isDead(T client) {
        if (this.type == 1) {
            return isHttpDead(client);
        } else {
            return isWsDead(client);
        }
    }

    @Override
    public boolean isOpen(T client) {
        if (this.type == 1) {
            return isHttpOpen(client);
        } else {
            return isWsOpen(client);
        }
    }

    @Override
    public abstract void close(T client) throws Exception;


    @Override
    public boolean open(T client) throws Exception {
        return false;
    }


    private boolean isHttpDead(T client) {
        //每次都链接，不需要判断，
        return false;
    }

    private boolean isHttpOpen(T client) {
        //每次都链接，不需要判断，
        return true;
    }

    protected abstract boolean isWsOpen(T client);

    protected abstract boolean isWsDead(T client);

}
