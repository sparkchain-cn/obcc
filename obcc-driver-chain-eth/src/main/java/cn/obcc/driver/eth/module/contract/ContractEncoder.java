package cn.obcc.driver.eth.module.contract;


import cn.obcc.driver.eth.module.tech.common.BlockTxInfoParser;
import cn.obcc.vo.KeyValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.*;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.web3j.abi.datatypes.Type.MAX_BIT_LENGTH;
import static org.web3j.abi.datatypes.Type.MAX_BYTE_LENGTH;

public class ContractEncoder {
    public static Logger logger = LoggerFactory.getLogger(ContractEncoder.class);

    //region common invoke
    public static String getFnEncodeData(String functionName, List<String> inputTypes, List<Object> inputValues, List<String> outputTypes) {
        Function function = genFnEncodeData(functionName, inputTypes, inputValues, outputTypes);
        return FunctionEncoder.encode(function);
    }

    public static Function genFnEncodeData(String functionName, List<String> inputTypes, List<Object> inputValues, List<String> outputTypes) {
        List<Type> inputParameters = genInputParams(inputTypes, inputValues);
        List<TypeReference<?>> outputParameters = genOutputParams(outputTypes);
        Function function = new Function(functionName, inputParameters, outputParameters);
        return function;
    }

    public static List<Type> genInputParams(List<String> types, List<Object> values) {
//		if(types.size() != values.size()){
//			throw new Exception("The size of type list doesn't equal to the size of value list!");
//		}

        List<Type> inputParameters = new ArrayList<>();
        int count = types.size();
        for (int i = 0; i < count; i++) {
            inputParameters.add(genInputParam(types.get(i), values.get(i)));
        }
        return inputParameters;
    }


    public static List<Type> genDefaultInputParams(List<String> types) {
//		if(types.size() != values.size()){
//			throw new Exception("The size of type list doesn't equal to the size of value list!");
//		}

        List<Type> inputParameters = new ArrayList<>();
        int count = types.size();
        for (int i = 0; i < count; i++) {
            inputParameters.add(genInputParam(types.get(i), "1"));
        }
        return inputParameters;
    }

    private static Type genInputParam(String type, Object value) {
        if ("address".equalsIgnoreCase(type)) {
            Address param = new Address((String) value);
            return param;
        } else if ("uint256".equalsIgnoreCase(type)) {
//			BigInteger amountBInt = Convert.toSha(value, Convert.Unit.MC).toBigInteger();
            BigInteger amount = BigInteger.valueOf((Long) value);
            Uint256 param = new Uint256(amount);
            return param;
        } else if ("string".equalsIgnoreCase(type)) {
            Utf8String param = new Utf8String((String) value);
            return param;
        } else if ("bool".equalsIgnoreCase(type)) {
            Bool param = new Bool((Boolean) value);
            return param;
        } else {
            return null;
        }
    }


    public static List<Class<? extends Type>> genInputParamTypes(List<String> typeNames) {
        List<Class<? extends Type>> ret = new ArrayList<>();
        for (String type : typeNames) {

            if ("address".equalsIgnoreCase(type)) {
                ret.add(Address.class);
            } else if ("uint256".equalsIgnoreCase(type)) {
                ret.add(Uint256.class);
            } else if ("string".equalsIgnoreCase(type)) {
                ret.add(Utf8String.class);
            } else if ("bool".equalsIgnoreCase(type)) {
                ret.add(Bool.class);
            } else {
                ret.add(null);
            }
        }
        return ret;
    }

    public static List<TypeReference<?>> genOutputParams(List<String> outputTypes) {
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        outputTypes.forEach(output -> {
            outputParameters.add(genOutputParam(output));
        });

        return outputParameters;
    }

    private static TypeReference genOutputParam(String type) {
        if ("address".equalsIgnoreCase(type)) {
            TypeReference<Address> param = new TypeReference<Address>() {
            };
            return param;
        } else if ("uint256".equalsIgnoreCase(type)) {
            TypeReference<Uint256> param = new TypeReference<Uint256>() {
            };
            return param;
        } else if ("string".equalsIgnoreCase(type)) {
            TypeReference<Utf8String> param = new TypeReference<Utf8String>() {
            };
            return param;
        } else if ("bool".equalsIgnoreCase(type)) {
            TypeReference<Bool> param = new TypeReference<Bool>() {
            };
            return param;
        } else {
            return null;
        }
    }

    public static BigInteger toBig(String value, int decimal) {
        double v1 = Double.parseDouble(value);
        double v2 = v1 * Math.pow(10, decimal);
        BigDecimal v3 = new BigDecimal(v2);
        BigInteger v4 = v3.toBigInteger();
        return v4;
    }
    //endregion


    public static String buildMethodSignature(String methodName, List<String> parameters) {
        StringBuilder result = new StringBuilder();
        result.append(methodName);
        result.append("(");
        String params = parameters.stream()
                .collect(Collectors.joining(","));
        result.append(params);
        result.append(")");
        return result.toString();
    }

    public static String buildMethodId(String methodSignature) {
        byte[] input = methodSignature.getBytes();
        byte[] hash = Hash.sha3(input);
        return Numeric.toHexString(hash).substring(0, 10);
    }

    public static String buildMethodId(String methodName, List<String> inputParameters) {
        String methodSignature = buildMethodSignature(methodName, inputParameters);
        String methodId = buildMethodId(methodSignature);
        return methodId;
        // FunctionEncoder.encode()
    }


    public static List<KeyValue> decodeParameters(String input, List<String> inputNames, List<Class<? extends Type>> parameters) {
        List<KeyValue> map = new ArrayList<>();

        input = input.substring(12);
        //解析长度
        List<TypeVo> list = new ArrayList<>();
        for (Class<? extends Type> parameter : parameters) {
            TypeVo vo = new TypeVo();
            String p = input.substring(0, 64);
            input = input.substring(64);
            Type v = null;
            vo.setType(parameter.getClass());
            if (isDynamic(parameter)) {
                vo.setLength(p);
                vo.setDync(true);
            } else {
                //v = ObccTypeDecoder.decode(p, parameter.getClass());
                vo.setContent(p);
            }
            list.add(vo);
        }

        //找到动态内容
        for (TypeVo vo : list) {
            if (vo.isDync()) {
                Type type = ObccTypeDecoder.decodeNumeric(vo.getLength(), vo.getType());
                int length = (int) type.getValue();
                String v = input.substring(0, length);
                vo.setContent(v);
                input = input.substring(length);
            }
        }
        if (input.length() != 0) {
            logger.error("解析出错。");
        }

        int i = 0;
        for (TypeVo vo : list) {
            Type type = ObccTypeDecoder.decode(vo.getLength() + vo.getContent(), vo.getType());
            map.add(new KeyValue(inputNames.get(i), type.getValue()));
            i++;
        }
        return map;
    }


    static boolean isDynamic(Class parameter) {
        return parameter.isAssignableFrom(DynamicBytes.class)
                || parameter.isAssignableFrom(Utf8String.class)
                || parameter.isAssignableFrom(DynamicArray.class);
    }


}

