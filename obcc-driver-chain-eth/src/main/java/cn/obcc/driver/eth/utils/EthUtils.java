package cn.obcc.driver.eth.utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import cn.obcc.exception.enums.ETransferStatus;
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


//    public static boolean isNgDigit(BigInteger value) {
//        if (value == null) {
//            return false;
//        }
//        if (value.longValue() > 0)
//            return true;
//        return false;
//    }
//
//    public static boolean isOriginToken(String token) {
//        return EthConstants.ETH_TOKEN.equalsIgnoreCase(token);
//    }
//
//    public static BigInteger toValueInMin(BigDecimal value, Integer decimal) {
//        return value.multiply(new BigDecimal(Math.pow(10, decimal.doubleValue()) + "")).toBigInteger();
//    }
//
//    public static String parseHexValue(String value, int decimal) {
//        if (StringUtils.isNullOrEmpty(value)) {
//            return value;
//        } else {
//            BigInteger bigInteger = Numeric.toBigInt(value);
//            return parseValue(bigInteger, decimal);
//        }
//    }
//
//    private static String parseValue(BigInteger value, int decimal) {
//        String amount = fromValueInMin(value, decimal).stripTrailingZeros().toPlainString();
//        return amount;
//    }
//
//	public static BigDecimal fromValueInMin(BigInteger valueInMin, Integer decimal) {
//		BigDecimal valueInMinInDecimal = new BigDecimal(valueInMin);
//		return valueInMinInDecimal.divide(new BigDecimal(Math.pow(10, decimal.doubleValue()) + ""));
//	}


}
