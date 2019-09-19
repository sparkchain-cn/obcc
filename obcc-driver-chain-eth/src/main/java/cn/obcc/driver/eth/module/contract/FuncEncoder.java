package cn.obcc.driver.eth.module.contract;

import cn.obcc.contract.solc.utils.AbiFuncUtils;
import cn.obcc.vo.contract.FuncVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.*;
import org.web3j.abi.datatypes.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class FuncEncoder {
    public static Logger logger = LoggerFactory.getLogger(FuncEncoder.class);


    public static String encodeFunc(String abi, String funcName,List<Object> params) {
        Function function = createFunc(abi,funcName,params);
        return FunctionEncoder.encode(function);
    }
    public static Function createFunc(String abi, String funcName,List<Object> params) {
        FuncVo vo = AbiFuncUtils.getFuncVo(abi, funcName);

        List<String> inputTypes = AbiFuncUtils.getInputTypes(vo);
        List<String> outputTypes = AbiFuncUtils.getOutPutTypes(vo);

        List<Type> inputParameters = InputTypeEncoder.createInputTypeList(inputTypes, params);
        List<TypeReference<?>> outputParameters = OutputTypeEncoder.createOutputTypeRefList(outputTypes);
        Function function = new Function(funcName, inputParameters, outputParameters);
        return function;
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

