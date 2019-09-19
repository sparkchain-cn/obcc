package cn.obcc.driver.eth.module.contract;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName OutputTypeEncoder
 * @desc TODO
 * @date 2019/9/18 0018  18:45
 **/
public class OutputTypeEncoder {





    public static List<TypeReference<?>> createOutputTypeRefList(List<String> outputTypes) {
        List<TypeReference<?>> outputParameters = new ArrayList<>();
        outputTypes.forEach(output -> {
            outputParameters.add(createOutputTypeRef(output));
        });

        return outputParameters;
    }

    private static TypeReference createOutputTypeRef(String type) {
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
}
