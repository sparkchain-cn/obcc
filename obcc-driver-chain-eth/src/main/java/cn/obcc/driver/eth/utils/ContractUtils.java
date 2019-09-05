package cn.obcc.driver.eth.utils;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;

import java.io.IOException;

/**
 * @author mgicode
 * @version 1.0
 * @ClassName ContractUtils
 * @desc
 * @data 2019/9/5 16:25
 **/
public class ContractUtils {


    public static Boolean isContractAddr(Web3j web3j, String addr) {
        String code = null;
        try {
            code = web3j.ethGetCode(addr, DefaultBlockParameterName.LATEST).send().getCode();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        if ("0x".equals(code)) {
            return false;
        }
        return true;
    }

}
