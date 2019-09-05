package cn.obcc.driver.utils;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import cn.obcc.utils.HexUtils;
import cn.obcc.utils.base.StringUtils;


public class JunctionUtils {

    public static BigInteger toBigInteger(Long num, Long _default) {
        if (num != null) {
            return BigInteger.valueOf(num);
        }
        return BigInteger.valueOf(_default);

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


    public static String formatDot(Double v, Integer dotSize) {

        DecimalFormat decimalFormat = new DecimalFormat(
                "###################.######");
        if (dotSize != null) {
            if (dotSize == 5) {
                decimalFormat = new DecimalFormat("###################.#####");
            } else if (dotSize == 4) {
                decimalFormat = new DecimalFormat("###################.####");
            }
        }
        String bal = decimalFormat.format(v);
        return bal;
    }

    public static String hexData(String data) {

        if (StringUtils.isNotNullOrEmpty(data)) {
            data = HexUtils.str2Hex(data);
        } else {
            data = "";
        }
        return data;
    }


    /**
     * abi中指定方法的参数的个数
     *
     * @param abi
     * @param name
     * @return
     */
    public static int abiFuncInSize(String abi, String name) {
        if (StringUtils.isNullOrEmpty(abi) || StringUtils.isNullOrEmpty(name)) {
            return -1;
        }
        try {
            // "name": "transfer",
            JSONArray array = JSON.parseArray(abi);

            for (Object o : array) {
                Map<String, Object> func = (Map<String, Object>) o;

                if (name.equals(func.get("name"))) {
                    Collection ct = (Collection<?>) func.get("inputs");
                    return ct.size();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;

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


}


