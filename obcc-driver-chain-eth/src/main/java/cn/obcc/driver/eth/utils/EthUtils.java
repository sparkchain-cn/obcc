package cn.obcc.driver.eth.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
