package cn.obcc.driver.eth.module.contract;


import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ContractEncoder {

    //region common invoke
    public static String getFnEncodeData(String functionName, List<String> inputTypes, List<String> inputValues, List<String> outputTypes) {
        Function function = genFnEncodeData(functionName, inputTypes, inputValues, outputTypes);
        return FunctionEncoder.encode(function);
    }

    public static Function genFnEncodeData(String functionName, List<String> inputTypes, List<String> inputValues, List<String> outputTypes) {
        List<Type> inputParameters = genInputParams(inputTypes, inputValues);
        List<TypeReference<?>> outputParameters = genOutputParams(outputTypes);
        Function function = new Function(functionName, inputParameters, outputParameters);
        return function;
    }

    public static List<Type> genInputParams(List<String> types, List<String> values) {
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


    private static Type genInputParam(String type, String value) {
        if (type.equalsIgnoreCase("address")) {
            Address param = new Address(value);
            return param;
        } else if (type.equalsIgnoreCase("uint256")) {
//			BigInteger amountBInt = Convert.toSha(value, Convert.Unit.MC).toBigInteger();
            BigInteger amountBInt = BigInteger.valueOf(Long.parseLong(value));
            Uint256 param = new Uint256(amountBInt);
            return param;
        } else if (type.equalsIgnoreCase("string")) {
            Utf8String param = new Utf8String(value);
            return param;
        } else if (type.equalsIgnoreCase("bool")) {
            Bool param = new Bool(Boolean.parseBoolean(value));
            return param;
        } else {
            return null;
        }
    }

    public static List<TypeReference<?>> genOutputParams(List<String> outputTypes) {
        List<TypeReference<?>> outputParameters = new ArrayList<>();

        outputTypes.forEach(output -> {
            outputParameters.add(genOutputParam(output));
        });

        return outputParameters;
    }

    private static TypeReference genOutputParam(String type) {
        if (type.equalsIgnoreCase("address")) {
            TypeReference<Address> param = new TypeReference<Address>() {
            };
            return param;
        } else if (type.equalsIgnoreCase("uint256")) {
            TypeReference<Uint256> param = new TypeReference<Uint256>() {
            };
            return param;
        } else if (type.equalsIgnoreCase("string")) {
            TypeReference<Utf8String> param = new TypeReference<Utf8String>() {
            };
            return param;
        } else if (type.equalsIgnoreCase("bool")) {
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
}
