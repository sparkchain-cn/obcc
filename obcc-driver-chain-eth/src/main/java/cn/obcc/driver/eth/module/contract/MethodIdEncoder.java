package cn.obcc.driver.eth.module.contract;

import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName MethodIdEncoder
 * @desc TODO
 * @date 2019/9/18 0018  22:25
 **/
public class MethodIdEncoder {

    private static String buildMethodSignature(String methodName, List<String> parameters) {
        StringBuilder result = new StringBuilder();
        result.append(methodName);
        result.append("(");
        String params = parameters.stream()
                .collect(Collectors.joining(","));
        result.append(params);
        result.append(")");
        return result.toString();
    }

    private static String buildMethodId(String methodSignature) {
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

}
