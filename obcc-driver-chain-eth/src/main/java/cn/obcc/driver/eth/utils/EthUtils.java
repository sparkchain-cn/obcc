package cn.obcc.driver.eth.utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;
import org.web3j.utils.Numeric;

import cn.obcc.driver.eth.EthConstants;
import cn.obcc.driver.utils.JunctionUtils;
import cn.obcc.driver.vo.Account;
import cn.obcc.exception.ObccException;
import cn.obcc.exception.enums.EExceptionCode;
import cn.obcc.utils.base.StringUtils;

public class EthUtils {

	public static Logger logger = LoggerFactory.getLogger(EthUtils.class);

	public static Account createWallet() throws ObccException {
		try {
			ECKeyPair ecKeyPair = Keys.createEcKeyPair();
			String addr = Keys.getAddress(ecKeyPair);
			Account spcAccount = new Account();
			addr = JunctionUtils.hexAddrress(addr);
			spcAccount.setAddress(addr);

			String hexPrivateKey = String.format("%040x", ecKeyPair.getPrivateKey());
			spcAccount.setSecret(hexPrivateKey);
			String hexPublicKey = String.format("%040x", ecKeyPair.getPublicKey());
			spcAccount.setPublicKey(hexPublicKey);
			return spcAccount;
		} catch (InvalidAlgorithmParameterException e) {
			throw ObccException.create(EExceptionCode.CREATE_ACCOUNT_FAIL, "InvalidAlgorithmParameter." + e);
		} catch (NoSuchAlgorithmException e) {
			throw ObccException.create(EExceptionCode.CREATE_ACCOUNT_FAIL, "NoSuchAlgorithm." + e);
		} catch (NoSuchProviderException e) {
			throw ObccException.create(EExceptionCode.CREATE_ACCOUNT_FAIL, "NoSuchProvider" + e);
		}

	}

	public static Boolean isContractAddr(Web3j web3j, String addr) {
		String code = null;
		try {
			code = web3j.ethGetCode(addr, DefaultBlockParameterName.LATEST).send().getCode();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		if ("0x".equals(code)) {
			return false;
		}
		return true;
	}

	/**
	 * 获取普通交易的gas上限
	 *
	 * @param transaction 交易对象
	 * @return gas 上限
	 */
	public static BigInteger getGasLimit(Web3j web3j, Transaction transaction) throws ObccException {
		BigInteger gasLimit = BigInteger.ZERO;
		try {
			EthEstimateGas ethEstimateGas = web3j.ethEstimateGas(transaction).send();
			gasLimit = ethEstimateGas.getAmountUsed();
		} catch (IOException e) {
			throw ObccException.create(EExceptionCode.IO_EXCEPTION, "web3j mcEstimateGas error." + e);
		}
		return gasLimit;
	}

	public static BigInteger getGasPrice(Web3j web3j) throws ObccException {
		BigInteger gasPrice = BigInteger.ZERO;
		try {
			gasPrice = web3j.ethGasPrice().send().getGasPrice();

		} catch (IOException e) {
			throw ObccException.create(EExceptionCode.IO_EXCEPTION, "web3j mcGasPrice error." + e);
		}
		return gasPrice;
	}

	public static String signEthTxData(String privateKey, String to, BigInteger nonce, StaticGasProvider gasTwo,
			BigInteger value, String hexData) {
		// 把十进制的转换成ETH的Wei, 1ETH = 10^18 Wei
		System.out.println("nonce:" + nonce + ",GasPrice:" + gasTwo.getGasPrice() + ",GasLimit:" + gasTwo.getGasLimit()
				+ ",to:" + to + ",value:" + value + ",hexData:" + hexData);
		System.out.println("privateKey:" + privateKey);
		RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasTwo.getGasPrice(),
				gasTwo.getGasLimit(), to, value, hexData);
		Credentials credentials = Credentials.create(privateKey);
		// 使用TransactionEncoder对RawTransaction进行签名操作
		byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
		// 转换成0x开头的字符串
		return Numeric.toHexString(signedMessage);

	}

	/**
	 * 获取账号交易次数 nonce
	 *
	 * @param address 钱包地址
	 * @return nonce
	 */
	public static BigInteger getTransactionNonce(Web3j web3j, String address) throws ObccException {
		BigInteger nonce = BigInteger.ZERO;
		try {
			EthGetTransactionCount ethGetTransactionCount = web3j
					.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).send();

			nonce = ethGetTransactionCount.getTransactionCount();
		} catch (IOException e) {
			throw ObccException.create(EExceptionCode.IO_EXCEPTION,
					"web3j mcGetTransactionCount io exception for " + address + e);
		}
		return nonce;
	}

	public static boolean isNgDigit(BigInteger value) {
		if (value == null) {
			return false;
		}
		if (value.longValue() > 0)
			return true;
		return false;
	}

	public static boolean checkAddrAndPrivate(String addr, String privateKey) {
		Credentials credentials = Credentials.create(privateKey);
		if (addr.equalsIgnoreCase(credentials.getAddress())) {
			return true;
		}
		return false;
	}

	public static StaticGasProvider createGasProvider(Long gasPrice, Long gasLimit) {
		if (gasLimit == null) {
			gasLimit = 0L;
		}
		if (gasPrice == null) {
			gasPrice = 0L;
		}

		BigInteger gasPriceBInt = BigInteger.valueOf(gasPrice);
		if (gasPrice <= 0) {
			gasPriceBInt = DefaultGasProvider.GAS_PRICE;

		}

		BigInteger gasLimitBInt = BigInteger.valueOf(gasLimit);
		if (gasLimit <= 0) {
			gasLimitBInt = DefaultGasProvider.GAS_LIMIT;

		}

		return new StaticGasProvider(gasPriceBInt, gasLimitBInt);

	}

	public static boolean isNouceLowError(String msg) {
		if (msg == null) {
			return false;
		}
		if (msg.contains(EthConstants.ERROR_MOAC_NTL)) {
			return true;
		}
		return false;

	}

	public static boolean isInNouceQueueError(String msg) {
		if (msg == null) {
			return false;
		}
		if (msg.contains(EthConstants.ERROR_MOAC_KT)) {
			return true;
		}
		return false;

	}

	public static boolean isUnderPriceError(String msg) {
		if (msg == null) {
			return false;
		}
		if (msg.contains(EthConstants.ERROR_MOAC_RTU)) {
			return true;
		}
		return false;

	}

	public static String calUsedGasFee(TransactionReceipt tr, BigInteger gasPrice) {

		if (tr == null) {
			return null;
		}
		BigInteger gasFee = tr.getGasUsed().multiply(gasPrice);

		String gasFeeStr = Convert.fromWei(new BigDecimal((gasFee)), Unit.ETHER).toPlainString();

		return gasFeeStr;
	}

	// todo:取不到就是当前的时间
	public static long getTradeTime(Web3j web3j, String blockHash) {
		try {
			BigInteger timeStamp = null;
			if (!StringUtils.isNullOrEmpty(blockHash)) {
				timeStamp = web3j.ethGetBlockByHash(blockHash, false).send().getBlock().getTimestamp();
			}
			return timeStamp == null ? (System.currentTimeMillis() / 1000) : timeStamp.longValue();
		} catch (IOException e) {
			logger.error(
					"chain3j io exception mcGetBlockByHash error for:" + blockHash + "," + StringUtils.exception(e));
		} catch (Exception ex) {
			logger.error(
					"chain3j io exception mcGetBlockByHash error for:" + blockHash + "," + StringUtils.exception(ex));
		}

		return (0L);
	}

	public static TransactionReceipt processResponse(Web3j chain3j, EthSendTransaction transactionResponse)
			throws ObccException {
		if (transactionResponse.hasError()) {
			throw ObccException.create(transactionResponse.getError().getCode() + "",
					transactionResponse.getError().getMessage(), null);
		} else {
			String transactionHash = transactionResponse.getTransactionHash();
			PollingTransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(chain3j,
					15000L, 40);
			try {
				return receiptProcessor.waitForTransactionReceipt(transactionHash);
			} catch (IOException e) {
				throw ObccException.create(EExceptionCode.IO_EXCEPTION,
						"io exception for wait TransactionReceipt" + e);
			} catch (TransactionException e) {
				throw ObccException.create(EExceptionCode.INVALID_KEYSPEC_EXCEPTION,
						" wait TransactionReceipt error.F" + e);
			}
		}
	}

	public static boolean isOriginToken(String token) {
		return EthConstants.ETH_TOKEN.equalsIgnoreCase(token);
	}

	public static BigInteger toValueInMin(BigDecimal value, Integer decimal) {
		return value.multiply(new BigDecimal(Math.pow(10, decimal.doubleValue()) + "")).toBigInteger();
	}

	public static BigDecimal fromValueInMin(BigInteger valueInMin, Integer decimal) {
		BigDecimal valueInMinInDecimal = new BigDecimal(valueInMin);
		return valueInMinInDecimal.divide(new BigDecimal(Math.pow(10, decimal.doubleValue()) + ""));
	}
//
//	public static String signTx(EthTxParams params, String privateKey) throws Exception {
//		return signTx(params.getChainId(), createRawTransaction(params), privateKey);
//	}
//
//	private static RawTransaction createRawTransaction(EthTxParams params) {
//		return RawTransaction.createTransaction(params.getNonceInt(), params.getGasPriceInt(), params.getGasLimitInt(),
//				params.getDestinationAddress(), params.getAmountInMin(), params.getInputData());
//	}

	public static Boolean isHex(String str) {
		boolean flag = false;
		for (int i = 0; i < str.length(); i++) {
			char cc = str.charAt(i);
			if (cc == '0' || cc == '1' || cc == '2' || cc == '3' || cc == '4' || cc == '5' || cc == '6' || cc == '7'
					|| cc == '8' || cc == '9' || cc == 'A' || cc == 'B' || cc == 'C' || cc == 'D' || cc == 'E'
					|| cc == 'F' || cc == 'a' || cc == 'b' || cc == 'c' || cc == 'c' || cc == 'd' || cc == 'e'
					|| cc == 'f') {
				flag = true;
			} else {
				return flag; // if there is one char is not hex char, then false.
			}
		}
		return flag;
	}

	public static boolean isContractAddress(Web3j chain3j, String to) {
		String code = null;
		try {
			code = chain3j.ethGetCode(to, DefaultBlockParameterName.LATEST).send().getCode();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		if ("0x".equals(code)) {
			return false;
		}
		return true;

	}

	public static String parseHexValue(String value, int decimal) {
		if (StringUtils.isNullOrEmpty(value)) {
			return value;
		} else {
			BigInteger bigInteger = Numeric.toBigInt(value);
			return parseValue(bigInteger, decimal);
		}
	}

	private static String parseValue(BigInteger value, int decimal) {
		String amount = fromValueInMin(value, decimal).stripTrailingZeros().toPlainString();
		return amount;
	}

	private static String signTx(Integer chainId, RawTransaction rawTransaction, String privateKey) throws Exception {
		Credentials credentials = Credentials.create(privateKey);
		byte[] signedMessage = null;
		signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId.byteValue(), credentials);
		// 转换成0x开头的字符串
		return Numeric.toHexString(signedMessage);
	}
}
