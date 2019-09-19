package cn.obcc.contract.solc.utils;

import cn.obcc.vo.contract.FuncVo;
import cn.obcc.vo.contract.NameTypeVo;

import lombok.NonNull;

import java.util.*;
import java.util.stream.Collectors;

import static cn.obcc.contract.solc.utils.AbiElemUtils.*;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName AbiFuncUtils
 * @desc TODO
 * @date 2019/9/18 0018  8:31
 **/
public class AbiFuncUtils {


    /**
     * @param abi
     * @param type view nonpayable
     * @return
     */
    public static List<FuncVo> getFuncList(@NonNull String abi, String type) {
        List<Map<String, Object>> fnList = getTopElemList(abi, "function");
        if (fnList == null) {
            return null;
        }
        return fnList.stream().filter(
                (fn) -> {
                    if (type == null) {
                        return true;
                    } else {
                        return type.equals(fn.get("stateMutability"));
                    }
                })
                .map((fn) -> {
                    return convertToFunc(fn);
                })
                .collect(Collectors.toList());

    }

    public static FuncVo getFuncVo(@NonNull String abi, @NonNull String funcName) {
        List<FuncVo> list = getAllFuncList(abi).stream().filter((FuncVo vo) -> {
            return funcName.equalsIgnoreCase(vo.getName());
        }).collect(Collectors.toList());
        if (list == null || list.size() == 0) {
            logger.error("funcName: {} cannot find function in abi:{}", funcName, abi);
            return null;
        }
        return list.get(0);
    }

    public static List<FuncVo> getAllFuncList(@NonNull String abi) {
        return getFuncList(abi, null);
    }

    public static List<String> getInputTypes(@NonNull FuncVo vo) {
        return vo.getInputs().stream().map(nameTypeVo -> {
            return nameTypeVo.getType();
        }).collect(Collectors.toList());
    }

    public static List<String> getOutPutTypes(@NonNull FuncVo vo) {
        return vo.getOutputs().stream().map(nameTypeVo -> {
            return nameTypeVo.getType();
        }).collect(Collectors.toList());
    }

    public static List<String> getInputNames(@NonNull FuncVo vo) {
        return vo.getInputs().stream().map(nameTypeVo -> {
            return nameTypeVo.getName();
        }).collect(Collectors.toList());
    }


    public static List<String> getOutPutNames(@NonNull FuncVo vo) {
        return vo.getOutputs().stream().map(nameTypeVo -> {
            return nameTypeVo.getName();
        }).collect(Collectors.toList());
    }


    private static FuncVo convertToFunc(Map<String, Object> fn) {
        FuncVo vo = new FuncVo();
        vo.setName(fn.get("name").toString());
        vo.setConstant((boolean) fn.get("constant"));
        vo.setPayable((boolean) fn.get("payable"));
        vo.setStateMutability(fn.get("stateMutability").toString());
        vo.setType(fn.get("type").toString());
        vo.setInputs(convertNameType((List<Object>) fn.get("inputs")));
        vo.setOutputs(convertNameType((List<Object>) fn.get("outputs")));
        return vo;
    }

    public static List<FuncVo> getExecFuncList(@NonNull String abi) {
        return getFuncList(abi, "nonpayable");
    }

    public static List<FuncVo> getViewFuncList(@NonNull String abi) {
        return getFuncList(abi, "view");
    }

    /**
     * abi中指定方法的参数的个数
     *
     * @param abi
     * @param funcName
     * @return
     */
    public static int paramCount(String abi, String funcName) {
        return getInputs(abi, funcName).size();
    }


    public static List<NameTypeVo> getArgsInputs(String abi) {
        Map<String, Object> function = getConstructor(abi);
        List<Object> elements = (List<Object>) function.get("inputs");
        return convertNameType(elements);
    }

    public static FuncVo getArgsVo(String abi) {
        Map<String, Object> function = getConstructor(abi);
        return convertToFunc(function);
    }



    /**
     * <pre>
     *  "inputs": [
     *     {
     *         "indexed": true,
     *             "name": "_from",
     *             "type": "address"
     *     },
     *     {
     *         "indexed": true,
     *             "name": "_to",
     *             "type": "address"
     *     },
     *     {
     *         "indexed": false,
     *             "name": "_value",
     *             "type": "uint256"
     *     }
     *  ]*
     * </pre>
     *
     * @param abi
     * @param functionName
     * @return
     */
    public static List<NameTypeVo> getInputs(String abi, String functionName) {
        return convertNameType(getFuncEls(abi, functionName, "inputs"));
    }

    public static List<NameTypeVo> getOutputs(String abi, String functionName) {
        return convertNameType(getFuncEls(abi, functionName, "outputs"));
    }

    /**
     * abi中指定方法 数据
     * {"constant":false,"inputs":[{"name":"_to","type":"address"},{"name":"_value","type":"uint256"},{"name":"memos","type":"string"}],"name":"transfer","outputs":[{"name":"success","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},
     *
     * @param abi
     * @param fname
     * @param elName: inputs, outputs, constant, etc.
     * @return
     */
    public static List<Object> getFuncEls(String abi, String fname, String elName) {
        Map<String, Object> function = getFunc(abi, fname);
        List<Object> elements = (List<Object>) function.get(elName);
        return elements;
    }

    public static boolean exist(@NonNull String abi, @NonNull String elName) {
        return AbiElemUtils.getTopElem(abi, elName, "function") == null ? false : true;
    }

    private static List<NameTypeVo> convertNameType(List<Object> list) {
        List<NameTypeVo> retList = new ArrayList<>();
        for (Object object : list) {
            Map<String, Object> param = (Map<String, Object>) object;
            retList.add(new NameTypeVo(param.get("name").toString(), param.get("type").toString()));
        }
        return retList;
    }

}
