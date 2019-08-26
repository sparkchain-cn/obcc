package cn.obcc.driver.eth.utils;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.web3j.protocol.Web3j;

import cn.obcc.exception.ObccException;
import cn.obcc.utils.base.StringUtils;

public class NonceUtils {
	public static Logger logger = LoggerFactory.getLogger(NonceUtils.class);

	// RedisEthService redisEthService;

	public String genAccountSeqKey(String chainCode, String account) {

		return "spc_eth_seq_" + chainCode + "_" + account;
	}

	public Long getNounceFormRedis(String chainCode, String account) {
		String key = genAccountSeqKey(chainCode, account);
		String value = null;// redisEthService.get(key);
		if (value == null)
			return null;
		return Long.parseLong(value);
	}

	public Long getNounceFormChain(Web3j web3j, String chainCode, String account) throws ObccException {
		BigInteger bigInteger = EthUtils.getTransactionNonce(web3j, account);
		if (bigInteger == null) {
			return null;
		}
		return bigInteger.longValue();
	}

	public Long storeNounceAndGetNow(Long nowSeq, String chainCode, String account) {

		String key = genAccountSeqKey(chainCode, account);
		String value = null;// redisEthService.get(key);

		// 取redis中nounce
		Long oldSeq = null;
		if (StringUtils.isNotNullOrEmpty(value)) {
			oldSeq = Long.parseLong(value);
		}

		if (nowSeq == null && oldSeq == null) {
			// 没有传入，redis中没有，那就返回null
			return null;
		} else if (nowSeq == null && oldSeq != null) {
			// 没有传入，redis中有，那么就是redis中大，取redis中当作当前的nounce，并计算下一个写到redis
			nowSeq = oldSeq;
		} else if (nowSeq != null && oldSeq == null) {
			nowSeq = nowSeq;
			// 传入，redis中没有，那么就是传入中大，取nowSeq中当作当前的nounce，并计算下一个写到redis
		} else if (nowSeq != null && oldSeq != null) {
			// 传入，redis中有，比大小，取大的中当作当前的nounce，并计算下一个写到redis
			if (oldSeq > nowSeq) {
				nowSeq = oldSeq;
			} else {
				nowSeq = nowSeq;
			}
		}

		logger.info(String.format("oldseq:" + oldSeq + " |seq:" + nowSeq + ":time:" + System.currentTimeMillis()));

		// 存入到redis是下一个seq
		// Long nextSeq = nowSeq + 1;
		Long nextSeq = nowSeq;
		// 20分钟进行重置，从链上取代表其
		//redisEthService.set(key, nextSeq, 20 * 60);

		return nowSeq;

	}

	// 以大的为准，redis和从链上取的nounce，谁的大取谁

	public BigInteger computeNounce(Web3j web3j, String chainCode, String fromAddress) throws ObccException {
		// ,eth不能减，所以只有先到链上取，然后重试加，这种方案不行，这样的命名没有先把redis中取，大于再减的效率高
		// 1 先看redis有没有？
		Long nowSeq = storeNounceAndGetNow(null, chainCode, fromAddress);
		if (nowSeq == null) {
			BigInteger bigInteger = EthUtils.getTransactionNonce(web3j, fromAddress);
			// 2 没有，那么从链上取
			nowSeq = bigInteger.longValue();
			// 3，保存其+1的数据到redis，让其它服务器取下一个
			storeNounceAndGetNow(nowSeq, chainCode, fromAddress);
		}

		return BigInteger.valueOf(nowSeq);
		// return nowSeq;
	}

}
