package cn.obcc.utils;

import cn.obcc.utils.base.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName ConvertUtils
 * @desc TODO
 * @date 2019/9/10 0010  9:21
 **/
public class ConvertUtils {

    public static boolean containsHexPrefix(String input) {
        return StringUtils.isNotNullOrEmpty(input) && input.length() > 1
                && input.charAt(0) == '0' && input.charAt(1) == 'x';
    }

    public static String cleanHexPrefix(String input) {
        if (containsHexPrefix(input)) {
            return input.substring(2);
        } else {
            return input;
        }
    }

    public static BigDecimal toBig(BigDecimal number, int factor) {
        return number.divide(BigDecimal.TEN.pow(factor));
    }

    public static BigDecimal toBig(String number, int factor) {
        return toBig(new BigDecimal(number), factor);
    }

    public static String toBigStr(String number, int factor) {
        return toBig(number, factor).toPlainString();
    }

    public static String toBigStr(BigInteger number, int factor) {
        return toBig(new BigDecimal(number), factor).toPlainString();
    }


    public static BigDecimal toSmall(BigDecimal number, int factor) {
        return number.multiply(BigDecimal.TEN.pow(factor));
    }

    public static BigDecimal toSmall(String number, int factor) {
        return toSmall(new BigDecimal(number), factor);
    }

    public static String toSmallStr(String number, int factor) {
        return toSmall(number, factor).toPlainString();
    }


    public static String rvZeroAndDot(String s) {
        if (StringUtils.isNullOrEmpty(s)) {
            return null;
        }
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }
    public static String hexAddrress(String addr) {
        if (addr == null) {
            return null;
        }
        if (!addr.startsWith("0x")) {
            addr = "0x" + addr;
        }
        return addr;
    }

}
