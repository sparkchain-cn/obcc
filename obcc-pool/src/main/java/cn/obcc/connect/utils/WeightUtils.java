package cn.obcc.connect.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import cn.obcc.connect.pool.core.ChainNode;

public class WeightUtils {

    static Random random = new Random();

    public static <T> Integer calChainNodeNo(List<ChainNode<T>> chainNodes) {

        // 每个chainNode权重不同，总的份数
        ArrayList<Integer> nodeNoList = new ArrayList<>();
        // 建立每个节点在总份数中占有份数
        for (int nodeNo = 0; nodeNo < chainNodes.size(); nodeNo++) {
            ChainNode<T> node = chainNodes.get(nodeNo);
            for (int j = 0; j < node.getWeight(); j++) {
                nodeNoList.add(nodeNo);
            }
        }

        Collections.shuffle(nodeNoList);

        int weight = random.nextInt(nodeNoList.size());
        Integer num = nodeNoList.get(weight);
        return num;

    }

    public static void decWeight(ChainNode cn) {
        //todo:NodeWeight 中配置
        cn.getNodeWeight().setWeight(Math.round((float) (cn.getNodeWeight().getWeight() * 0.5)));

    }


    public static void incWeight(ChainNode cn) {
        //todo:NodeWeight 中配置
        cn.getNodeWeight().setWeight(Math.round((float) (cn.getNodeWeight().getWeight() * 2)));

    }


    public static boolean isHttp(String url) {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return true;
        }
        return false;
    }

    public static boolean isWs(String url) {
        if (url.startsWith("ws://") || url.startsWith("wss://")) {
            return true;
        }
        return false;
    }
}
