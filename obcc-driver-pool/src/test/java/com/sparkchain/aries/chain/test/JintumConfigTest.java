package com.sparkchain.aries.chain.test;

import java.util.ArrayList;
import java.util.List;

import cn.obcc.config.conn.pool.ChainNodeWeight;
import cn.obcc.config.conn.pool.ConnPoolConfig;

public class JintumConfigTest {

    public static ConnPoolConfig getPoolConfig(String chainCode) throws Exception {
        ConnPoolConfig poolConfig = new ConnPoolConfig();
        String poolSize = 5 + "";
        String maximumPoolSize = 30 + "";

        poolConfig.setPoolSize(Integer.parseInt(poolSize));
        poolConfig.setMaxPoolSize(Integer.parseInt(maximumPoolSize));
        poolConfig.setNodeWeights(getNodesAndWeight());

        return poolConfig;
    }

    // [  {"url":"wss://c03.jingtum.com:5020","weight":8}, {"url":"wss://c01.jingtum.com:5020","weight":6} ]
    private static List<ChainNodeWeight> getNodesAndWeight() {
        List<ChainNodeWeight> nodes = new ArrayList<ChainNodeWeight>();
        ChainNodeWeight node = new ChainNodeWeight("wss://c03.jingtum.com:5020", 1000);
        nodes.add(node);
        node = new ChainNodeWeight("wss://c01.jingtum.com:5020", 1000);
        nodes.add(node);
        node = new ChainNodeWeight("wss://c02.jingtum.com:5020", 1000);
        nodes.add(node);
        node = new ChainNodeWeight("wss://c06.jingtum.com:5020", 1000);
        nodes.add(node);
        node = new ChainNodeWeight("wss://c04.jingtum.com:5020", 1000);
        nodes.add(node);
        return nodes;

    }
}
