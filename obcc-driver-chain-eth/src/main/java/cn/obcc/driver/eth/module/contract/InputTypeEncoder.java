package cn.obcc.driver.eth.module.contract;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName InputTypeEncoder
 * @desc TODO
 * @date 2019/9/18 0018  18:48
 **/
public class InputTypeEncoder {


    public static List<Type> createInputTypeList(List<String> types, List<Object> values) {
//		if(types.size() != values.size()){
//			throw new Exception("The size of type list doesn't equal to the size of value list!");
//		}
        List<Type> inputParameters = new ArrayList<>();
        int count = types.size();
        for (int i = 0; i < count; i++) {
            inputParameters.add(createInputType(types.get(i), values.get(i)));
        }
        return inputParameters;
    }


    private static Type createInputType(String type, Object value) {
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


    public static List<Class<? extends Type>> createInputTypeClzList(List<String> typeNames) {
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

}
