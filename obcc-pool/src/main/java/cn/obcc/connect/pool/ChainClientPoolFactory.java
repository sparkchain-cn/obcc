package cn.obcc.connect.pool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.obcc.config.ObccConfig;
import cn.obcc.vo.pool.ConnPoolConfig;
import cn.obcc.connect.builder.ChainClientBuilder;
import cn.obcc.connect.pool.core.ChainPool;

public class ChainClientPoolFactory {

	public static final Logger logger = LoggerFactory.getLogger(ChainClientPoolFactory.class);
	public static Map<String, ChainPool> chainPoolMap = new HashMap<>();

	public static <T> ChainPool<T> create(ConnPoolConfig config, Class<? extends ChainClientBuilder<T>> clz)
			throws Exception {

		ChainPool<T> chainPool = new ChainPool<T>();
		chainPool.config = config;
		chainPool.clientBuilder = clz;
		chainPool.init();

		return chainPool;
	}


	public static <T> T getClient(ObccConfig config, Class<? extends ChainClientBuilder<T>> clz) throws Exception {
		String name = config.getClientId() + "-" + config.getChain().getName();
		if (!getChainPoolMap().containsKey(name)) {
			ConnPoolConfig poolConfig = config.getPoolConfig();
			ChainPool chainPool = create(poolConfig, clz);
			getChainPoolMap().put(name, chainPool);
		}

		ChainPool<T> chainPool = (ChainPool<T>) getChainPoolMap().get(name);

		String uuid = UUID.randomUUID() + "";
		// config.getUuids().add(uuid);
		return chainPool.getAndMark(uuid);
	}

	public static <T> T getClient(String name, String uuid, Class<? extends ChainClientBuilder<T>> clz)
			throws Exception {
		ChainPool<T> chainPool = (ChainPool<T>) getChainPoolMap().get(name);
		return chainPool.get(uuid);
	}

	/**
	 * 释放节点对象
	 *
	 * @param uuids
	 */
	public static void release(List<String> uuids) {
		chainPoolMap.forEach((key, value) -> {
			for (String uuid : uuids) {
				value.release(uuid);
			}
		});
	}

	/**
	 * 获取链连接节点额外配置
	 *
	 * @param chain
	 * @param uuid
	 * @return
	 */
	public static Map<String, Object> getChainNodeExtras(String chain, String uuid) {
		ChainPool<?> pool = chainPoolMap.get(chain);
		if (pool != null) {
			return pool.getChainNodeExtras(uuid);
		}
		return null;
	}

	public static Map<String, ChainPool> getChainPoolMap() {
		return chainPoolMap;
	}

	public static void setChainPoolMap(Map<String, ChainPool> chainPoolMap) {
		ChainClientPoolFactory.chainPoolMap = chainPoolMap;
	}

}
