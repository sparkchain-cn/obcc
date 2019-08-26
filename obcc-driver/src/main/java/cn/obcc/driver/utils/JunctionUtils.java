package cn.obcc.driver.utils;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import cn.obcc.config.conn.pool.ChainNodeWeight;
import cn.obcc.config.conn.pool.ConnPoolConfig;
import cn.obcc.driver.vo.BcMemo;
import cn.obcc.utils.HexStringUtils;
import cn.obcc.utils.base.MapUtils;
import cn.obcc.utils.base.StringUtils;


public class JunctionUtils {

    public static String joinErrorMap(Map<String, String> errorMap) {
        StringBuilder sb = new StringBuilder();
        for (String s : errorMap.values()) {
            sb.append(s);
            sb.append("/n");
        }

        return sb.toString();
    }

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

    public static String calFreezed(String chianCode, Integer currencyCount) {
        return (20 + currencyCount * 5) + "";
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
            data = HexStringUtils.str2HexStr(data);
        } else {
            data = "";
        }
        return data;
    }

    public static String getBizId(Map<String, Object> otherParams) {
        String bizId = MapUtils.getIfNullDefault(otherParams, "bizId", "");
        return bizId;
    }

    public static String getAbi(Map<String, Object> otherParams) {
        String abi = MapUtils.getIfNullDefault(otherParams, "abi", "");
        return abi;
    }

    public static String getContractAddress(Map<String, Object> otherParams) {
        String contractAddress = MapUtils.getIfNullDefault(otherParams,
                "contractAddress", "");
        return contractAddress;
    }

    public static String parseMemos(Map<String, Object> otherParams) {
        String memo = MapUtils.getIfNullDefault(otherParams, "memo", "");
        String bizId = MapUtils.getIfNullDefault(otherParams, "format", "");
        String appid = MapUtils.getIfNullDefault(otherParams, "md5", "");
        String memos = SpcMemoUtils.encode(appid, bizId, memo);
        return memos;
    }

    public static BcMemo parseMemoObj(Map<String, Object> otherParams) {
        String memo = MapUtils.getIfNullDefault(otherParams, "memo", "");
        String bizId = MapUtils.getIfNullDefault(otherParams, "bizId", "");
        String appid = MapUtils.getIfNullDefault(otherParams, "appid", "");
        return SpcMemoUtils.buildMemo(appid, bizId, memo);
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

    public static void sleep(long l) {
        try {
            Thread.sleep(l);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static ConnPoolConfig getPoolConfig(Map<String, String> chainConfigMap) {
        String poolSize = chainConfigMap.get("corePoolSize");
        String maximumPoolSize = chainConfigMap.get("maximumPoolSize");
        String remoteNodeWeights = chainConfigMap.get("remoteNodeWeights");
        List<ChainNodeWeight> nodes = new ArrayList<ChainNodeWeight>();
        if (!StringUtils.isNullOrEmpty(remoteNodeWeights)) {
            JSONArray array = JSONArray.parseArray(remoteNodeWeights);
            for (int i = 0; i < array.size(); i++) {
                ChainNodeWeight node = array.getObject(i, ChainNodeWeight.class);
                nodes.add(node);
            }
        }

        ConnPoolConfig poolConfig = new ConnPoolConfig();
        if (nodes.isEmpty() || StringUtils.isNullOrEmpty(remoteNodeWeights)) {
            throw new NullPointerException("无法获取到远程连接地址配置");
        }
        if (!StringUtils.isNullOrEmpty(poolSize)) {
            poolConfig.setPoolSize(Integer.parseInt(poolSize));
        }
        if (!StringUtils.isNullOrEmpty(maximumPoolSize)) {
            poolConfig.setMaxPoolSize(Integer.parseInt(maximumPoolSize));
        }
        poolConfig.setNodeWeights(nodes);

        return poolConfig;
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


