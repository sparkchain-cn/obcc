//package com.sparkchain.chain.jingtum;
//
//import com.blink.jtblc.client.Remote;
//import ChainPoolFactory;
//import ClientBuilder;
//import HttpAndWsClientBuilder;
//import ChainPool;
//import PoolConfig;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.net.URI;
//import java.util.UUID;
//
//public class JingtumBuilder extends HttpAndWsClientBuilder<Remote> implements ClientBuilder<Remote> {
//    static final Logger logger = LoggerFactory.getLogger(JingtumBuilder.class);
//  public   JintumWebSocketclient webSocketclient = null;
//  public   JingtumConnection connection = null;
//
//    @Override
//    public Remote newNativeClient(String url) throws Exception {
//
//        webSocketclient = new JintumWebSocketclient(URI.create(url));
//        webSocketclient.setClientBuilder(this);
//        webSocketclient.connect();
//
//        connection = new JingtumConnection(webSocketclient);
//        connection.setClientBuilder(this);
//        connection.setUrl(url);
//
//        waitStatus();
//        logger.debug(" JingtumBuilder new client:"+connection.getUrl());
//        return new Remote(connection);
//    }
//
//
//    // NOT_YET_CONNECTED, CONNECTING, OPEN, CLOSING, CLOSED
//    @Override
//    public boolean isWsDead(Remote client) {
//        return client.getConn().getState().equalsIgnoreCase("CLOSING")
//                || client.getConn().getState().equalsIgnoreCase("CLOSED");
//
//    }
//
//    @Override
//    public boolean isWsOpen(Remote client) {
//        return client.getConn().getState().equalsIgnoreCase("OPEN");
//    }
//
//    @Override
//    public void close(Remote client) {
//        client.getConn().close();
//    }
//
//    @Override
//    public boolean open(Remote client) throws Exception {
//        client.getConn().open();
//        return client.getConn().getState().equalsIgnoreCase("OPEN");
//    }
//
//
//    public static Remote getClient(String chain,PoolConfig poolConfig) throws Exception {
//        return (Remote) getClient(chain, poolConfig,JingtumBuilder.class);
//    }
//
//    public static <T> T getClient(String chain,PoolConfig poolConfig, Class<? extends ClientBuilder<T>> clz) throws Exception {
//        if (!ChainPoolFactory.getChainPoolMap().containsKey(chain)) {
//            //PoolConfig poolConfig = getPoolConfig(chain);
//            ChainPool chainPool = ChainPoolFactory.create(poolConfig, clz);
//            ChainPoolFactory.getChainPoolMap().put(chain, chainPool);
//        }
//
//        ChainPool<T> chainPool = (ChainPool<T>) ChainPoolFactory.getChainPoolMap().get(chain);
//        String uuid = UUID.randomUUID() + "";
//      //  config.getUuids().add(uuid);
//        return chainPool.getAndMark(uuid);
//    }
//
//    public static <T> T getClient(String chain, String uuid, Class<? extends ClientBuilder<T>> clz) throws Exception {
//        ChainPool<T> chainPool = (ChainPool<T>) ChainPoolFactory.getChainPoolMap().get(chain);
//        return chainPool.get(uuid);
//    }
//
//
//    private void waitStatus() throws Exception {
//        int time = 0;
//        while (!"open".equals(webSocketclient.getStatus())) {
//            Thread.sleep(100);
//            time += 100;
//            if (time > 10 * 1000) {
//                throw new RuntimeException("链接" + url + "服务器连接超时");
//            }
//        }
//
//    }
//
//
//}
//
