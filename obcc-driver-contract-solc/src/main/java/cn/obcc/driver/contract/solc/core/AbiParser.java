package cn.obcc.driver.contract.solc.core;

import cn.obcc.utils.base.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AbiParser {
    //region example
    /* example
    [
{"constant":true,"inputs":[],"name":"name","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},
{"constant":true,"inputs":[],"name":"totalSupply","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},
{"constant":true,"inputs":[],"name":"decimals","outputs":[{"name":"","type":"uint8"}],"payable":false,"stateMutability":"view","type":"function"},
{"constant":false,"inputs":[{"name":"_to","type":"address"},{"name":"_value","type":"uint256"},{"name":"memos","type":"string"}],"name":"transfer","outputs":[{"name":"success","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},
{"constant":true,"inputs":[{"name":"_owner","type":"address"}],"name":"balanceOf","outputs":[{"name":"balance","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},
{"constant":true,"inputs":[],"name":"symbol","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},
{"constant":true,"inputs":[],"name":"ONE_TOKEN","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},
{"inputs":[{"name":"tokenCode","type":"string"},{"name":"tokenName","type":"string"},{"name":"amount","type":"uint256"}],"payable":false,"stateMutability":"nonpayable","type":"constructor"},
{"anonymous":false,"inputs":[{"indexed":true,"name":"_from","type":"address"},{"indexed":true,"name":"_to","type":"address"},{"indexed":false,"name":"_value","type":"uint256"}],"name":"Transfer","type":"event"}
]
     */
    //endregion

    /**
     * abi中指定方法的参数的个数
     *
     * @param abi
     * @param functionName
     * @return
     */
    public static int abiFuncInSize(String abi, String functionName) {
        return getFunctionInputs(abi, functionName).size();
    }


    public static LinkedHashMap<String, String> getFunctionInputs(String abi, String functionName) {
        return convertToMap(getFunctionElements(abi, functionName, "inputs"));
    }

    public static LinkedHashMap<String, String> getFunctionOutputs(String abi, String functionName) {
        return convertToMap(getFunctionElements(abi, functionName, "outputs"));
    }

    /**
     * abi中指定方法的参数的个数
     * {"constant":false,"inputs":[{"name":"_to","type":"address"},{"name":"_value","type":"uint256"},{"name":"memos","type":"string"}],"name":"transfer","outputs":[{"name":"success","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},
     *
     * @param abi
     * @param functionName
     * @param elementName: inputs, outputs, constant, etc.
     * @return
     */
    public static List<Object> getFunctionElements(String abi, String functionName, String elementName) {
        Map<String, Object> function = getFunction(abi, functionName);
        List<Object> elements = (List<Object>) function.get(elementName);
        return elements;
    }

    public static Map<String, Object> getFunction(String abi, String functionName) {
        if (StringUtils.isNullOrEmpty(abi) || StringUtils.isNullOrEmpty(functionName)) {
            return null;
        }
        try {
            JSONArray array = JSON.parseArray(abi);
            for (Object object : array) {
                Map<String, Object> function = (Map<String, Object>) object;

                if (functionName.equals(function.get("name"))) {
                    return function;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static LinkedHashMap<String, String> convertToMap(List<Object> list) {
        LinkedHashMap<String, String> inputsMap = new LinkedHashMap<>();
        for (Object object : list) {
            Map<String, Object> param = (Map<String, Object>) object;
            inputsMap.put(param.get("name").toString(), param.get("type").toString());
        }
        return inputsMap;
    }

}
