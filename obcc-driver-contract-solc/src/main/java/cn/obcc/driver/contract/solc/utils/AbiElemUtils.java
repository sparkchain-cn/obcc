package cn.obcc.driver.contract.solc.utils;

import cn.obcc.utils.base.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName AbiUtils
 * @desc TODO
 * @date 2019/9/18 0018  7:53
 **/
public class AbiElemUtils {
    public static final Logger logger = LoggerFactory.getLogger(AbiElemUtils.class);

    public static boolean exist(@NonNull String abi, @NonNull String elName) {
        return getTopElem(abi, elName, null) == null ? false : true;
    }

    /**
     * <pre>
     *  {
     *   "payable": false,
     *   "inputs": [],
     *   "stateMutability": "nonpayable",
     *    "type": "constructor"
     *   }
     * </pre>
     * 解析函数的构造函数对象
     *
     * @param abi
     * @return
     */
    public static Map<String, Object> getConstructor(@NonNull String abi) {
        return getTopElem(abi, null, "constructor");
    }


    /**
     * <pre>
     *  {
     *   "constant": false,
     *   "inputs": [
     *     {
     *       "name": "_to",
     *       "type": "address"
     *     },
     *     {
     *       "name": "_value",
     *       "type": "uint256"
     *     },
     *     {
     *       "name": "memos",
     *       "type": "string"
     *     }
     *   ],
     *   "name": "transfer",
     *   "outputs": [
     *     {
     *       "name": "success",
     *       "type": "bool"
     *     }
     *   ],
     *   "payable": false,
     *   "stateMutability": "nonpayable",
     *   "type": "function"
     * }
     * </pre>
     *
     * @param abi
     * @param fnName
     * @return
     */
    public static Map<String, Object> getFunc(@NonNull String abi, @NonNull String fnName) {
        return getTopElem(abi, fnName, "function");
    }

    /**
     * @param abi
     * @param elemName
     * @param type     function、event、constructor
     * @return
     */
    public static Map<String, Object> getTopElem(@NonNull String abi, String elemName, String type) {

        if (StringUtils.isNullOrEmpty(elemName) && StringUtils.isNullOrEmpty(type)) {
            logger.error("get abi top Elem must provide elemName or type.");
            return null;
        }
        JSONArray array = null;
        try {
            array = JSON.parseArray(abi);
        } catch (Exception e) {
            logger.error("abi  format error. must be json,your abi is' {} '.", abi);
            return null;
        }
        if (array == null) {
            return null;
        }
        if (!StringUtils.isNullOrEmpty(elemName) && !StringUtils.isNullOrEmpty(type)) {
            for (Object object : array) {
                Map<String, Object> el = (Map<String, Object>) object;
                if (elemName.equals(el.get("name")) && type.equals(el.get("type"))) {
                    return el;
                }
            }
        } else if (!StringUtils.isNullOrEmpty(elemName)) {
            for (Object object : array) {
                Map<String, Object> el = (Map<String, Object>) object;
                if (elemName.equals(el.get("name"))) {
                    return el;
                }
            }
        } else if (!StringUtils.isNullOrEmpty(type)) {
            for (Object object : array) {
                Map<String, Object> el = (Map<String, Object>) object;
                if (type.equals(el.get("type"))) {
                    return el;
                }
            }
        }
        return null;
    }


    public static List<Map<String, Object>> getTopElemList(@NonNull String abi, @NonNull String type) {

        if (StringUtils.isNullOrEmpty(type)) {
            logger.error("get abi top Elem must provide elemName or type.");
            return null;
        }
        JSONArray array = null;
        try {
            array = JSON.parseArray(abi);
        } catch (Exception e) {
            logger.error("abi  format error. must be json,your abi is' {} '.", abi);
            return null;
        }
        if (array == null) {
            return null;
        }
        List<Map<String, Object>> retList = new ArrayList<>();
        for (Object object : array) {
            Map<String, Object> el = (Map<String, Object>) object;
            if (type.equals(el.get("type"))) {
                retList.add(el);
            }
        }
        return retList;
    }

}
