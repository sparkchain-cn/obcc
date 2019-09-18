package cn.obcc.driver.contract.solc.abi;

import cn.obcc.utils.base.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.obcc.driver.contract.solc.utils.AbiElemUtils.getConstructor;
import static cn.obcc.driver.contract.solc.utils.AbiElemUtils.getFunc;
import static cn.obcc.driver.contract.solc.utils.AbiFuncUtils.getFuncEls;

/**
 * <pre>
 *  [
 *   {
 *     "outputs": [
 *       {
 *         "name": "",
 *         "type": "string"
 *       }
 *     ],
 *     "constant": true,
 *     "payable": false,
 *     "inputs": [],
 *     "name": "name",
 *     "stateMutability": "view",
 *     "type": "function"
 *   },
 *   {
 *     "outputs": [
 *       {
 *         "name": "",
 *         "type": "uint256"
 *       }
 *     ],
 *     "constant": true,
 *     "payable": false,
 *     "inputs": [],
 *     "name": "TOTAL_TOKENS",
 *     "stateMutability": "view",
 *     "type": "function"
 *   },
 *   {
 *     "outputs": [
 *       {
 *         "name": "",
 *         "type": "uint256"
 *       }
 *     ],
 *     "constant": true,
 *     "payable": false,
 *     "inputs": [],
 *     "name": "totalSupply",
 *     "stateMutability": "view",
 *     "type": "function"
 *   },
 *   {
 *     "outputs": [
 *       {
 *         "name": "",
 *         "type": "uint8"
 *       }
 *     ],
 *     "constant": true,
 *     "payable": false,
 *     "inputs": [],
 *     "name": "decimals",
 *     "stateMutability": "view",
 *     "type": "function"
 *   },
 *   {
 *     "outputs": [
 *       {
 *         "name": "balance",
 *         "type": "uint256"
 *       }
 *     ],
 *     "constant": true,
 *     "payable": false,
 *     "inputs": [
 *       {
 *         "name": "_owner",
 *         "type": "address"
 *       }
 *     ],
 *     "name": "balanceOf",
 *     "stateMutability": "view",
 *     "type": "function"
 *   },
 *   {
 *     "outputs": [
 *       {
 *         "name": "",
 *         "type": "string"
 *       }
 *     ],
 *     "constant": true,
 *     "payable": false,
 *     "inputs": [],
 *     "name": "symbol",
 *     "stateMutability": "view",
 *     "type": "function"
 *   },
 *   {
 *     "outputs": [
 *       {
 *         "name": "success",
 *         "type": "bool"
 *       }
 *     ],
 *     "constant": false,
 *     "payable": false,
 *     "inputs": [
 *       {
 *         "name": "_to",
 *         "type": "address"
 *       },
 *       {
 *         "name": "_value",
 *         "type": "uint256"
 *       }
 *     ],
 *     "name": "transfer",
 *     "stateMutability": "nonpayable",
 *     "type": "function"
 *   },
 *   {
 *     "outputs": [
 *       {
 *         "name": "",
 *         "type": "uint256"
 *       }
 *     ],
 *     "constant": true,
 *     "payable": false,
 *     "inputs": [],
 *     "name": "ONE_TOKEN",
 *     "stateMutability": "view",
 *     "type": "function"
 *   },
 *   {
 *     "payable": false,
 *     "inputs": [],
 *     "stateMutability": "nonpayable",
 *     "type": "constructor"
 *   },
 *   {
 *     "inputs": [
 *       {
 *         "indexed": true,
 *         "name": "_from",
 *         "type": "address"
 *       },
 *       {
 *         "indexed": true,
 *         "name": "_to",
 *         "type": "address"
 *       },
 *       {
 *         "indexed": false,
 *         "name": "_value",
 *         "type": "uint256"
 *       }
 *     ],
 *     "name": "Transfer",
 *     "anonymous": false,
 *     "type": "event"
 *   }
 * ]
 *
 * </pre>
 */
public class AbiFormatParser {


//        "name": "transfer",
//         "stateMutability": "nonpayable",
//          "type": "function"

    public static List<String> getMethodNames(String abi) {
        if (StringUtils.isNullOrEmpty(abi)) {
            return null;
        }
//        JSON.parseArray(abi).stream().filter(((Map<String, Object>) x)->{
//            return "function".equals(x.get("type")) && "nonpayable".equals(x.get("stateMutability"))
//        })

        List<String> ret = new ArrayList<>();
        try {
            JSONArray array = JSON.parseArray(abi);
            for (Object object : array) {
                Map<String, Object> function = (Map<String, Object>) object;

                if ("function".equals(function.get("type")) && "nonpayable".equals(function.get("stateMutability"))) {
                    ret.add((String) function.get("name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }


    public static List<String> getConstructorInputTypes(String abi) {
        Map<String, Object> function = getConstructor(abi);
        List<Map<String, Object>> elements = (List<Map<String, Object>>) function.get("inputs");
        List<String> ret = elements.stream().map((e) -> {
            return (String) e.get("type");
        }).collect(Collectors.toList());

        return ret;
    }

    public static List<String> getFunctionInputTypes(String abi, String functionName) {
        Map<String, Object> function = getFunc(abi, functionName);
        List<Map<String, Object>> elements = (List<Map<String, Object>>) function.get("inputs");
        List<String> ret = elements.stream().map((e) -> {
            return (String) e.get("type");
        }).collect(Collectors.toList());

        return ret;
    }

    public static List<String> getFunctionInputNames(String abi, String functionName) {
        Map<String, Object> function = getFunc(abi, functionName);
        List<Map<String, Object>> elements = (List<Map<String, Object>>) function.get("inputs");
        List<String> ret = elements.stream().map((e) -> {
            return (String) e.get("name");
        }).collect(Collectors.toList());

        return ret;
    }


    public static List<String> getFunctionOutputTypes(String abi, String functionName) {
        Map<String, Object> function = getFunc(abi, functionName);
        List<Map<String, Object>> elements = (List<Map<String, Object>>) function.get("outputs");
        List<String> ret = elements.stream().map((e) -> {
            return (String) e.get("type");
        }).collect(Collectors.toList());

        return ret;
    }




}
