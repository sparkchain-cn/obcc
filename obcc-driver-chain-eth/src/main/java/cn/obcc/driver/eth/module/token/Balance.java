//package cn.obcc.driver.eth.module.token;
//
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.math.BigInteger;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.web3j.abi.FunctionEncoder;
//import org.web3j.abi.datatypes.Function;
//import org.web3j.protocol.Web3j;
//import org.web3j.protocol.core.DefaultBlockParameterName;
//import org.web3j.protocol.core.methods.request.Transaction;
//import org.web3j.protocol.core.methods.response.EthCall;
//import org.web3j.protocol.core.methods.response.EthGetBalance;
//import org.web3j.utils.Convert;
//import org.web3j.utils.Numeric;
//
//import cn.obcc.driver.eth.module.contract.TokenContract;
//import cn.obcc.driver.eth.utils.EthUtils;
//import cn.obcc.driver.utils.JunctionUtils;
//import cn.obcc.exception.ObccException;
//import cn.obcc.exception.enums.EExceptionCode;
//import cn.obcc.utils.base.StringUtils;
//import cn.obcc.config.ReqConfig;
//import cn.obcc.vo.RetData;
//
//public class Balance {
//
//    public static Logger logger = LoggerFactory.getLogger(Balance.class);
//
//    public static RetData<String> getEthBalance(String addr, ReqConfig<Web3j> config) throws Exception {
//        try {
//            addr = JunctionUtils.hexAddrress(addr);
//            Web3j web3j = config.getClient();
//
//            EthGetBalance ethGetBalance = web3j.ethGetBalance(addr, DefaultBlockParameterName.LATEST).send();
//            BigInteger balance = ethGetBalance.getBalance();
//            BigDecimal bDecimal = Convert.fromWei(balance + "", Convert.Unit.ETHER);
//            System.out.println("address " + addr + " balance " + balance + "wei");
//
//            return RetData.succuess(bDecimal.toPlainString());
//        } catch (IOException e) {
//            throw ObccException.create(EExceptionCode.IO_EXCEPTION, " web3j ethGetBalance io exception." + e);
//
//        }
//    }
//
//    public static RetData<String> getErc20Balance(String addr, String contractAddress, String currency,
//                                                  Integer precision, ReqConfig<Web3j> config) throws Exception {
//
//        if (contractAddress == null) {
//            // 传过来的 contractAddress 为空，获取配置数据
//            // BcTokenConfig tokenConfig =
//            // EthPoolClient.getTokenConfig(config.getChainCode(), currency);
//            // contractAddress = tokenConfig.getContractAddress();
//            throw ObccException.create(EExceptionCode.PARAMETER_INVALID, "contractAddress can be not empty." + null);
//        }
//
//        Web3j web3j = config.getClient();
//
//        Function function = TokenContract.getContractBalanceData(addr);
//        String encodedFunction = FunctionEncoder.encode(function);
//
//        Transaction tc = Transaction.createEthCallTransaction(addr, contractAddress, encodedFunction);
//
//        EthCall ethCall = null;
//        try {
//            ethCall = web3j.ethCall(tc, DefaultBlockParameterName.LATEST).send();
//        } catch (IOException e) {
//            throw ObccException.create(EExceptionCode.IO_EXCEPTION, "io exception." + e);
//        }
//
//        String value = ethCall.getValue();
//        if (StringUtils.isNullOrEmpty(value)) {
//            throw ObccException.create(EExceptionCode.IO_EXCEPTION,
//                    "web3j get erc20 balance return null or empty." + null);
//        }
//        if (value.equalsIgnoreCase("0x")) {
//            value += "0";
//        }
//
//        // 根据精度计算余额
//        BigDecimal bigDecimal = EthUtils.fromValueInMin(Numeric.toBigInt(value), precision);
//        return RetData.succuess(bigDecimal.stripTrailingZeros().toPlainString());
//
//    }
//
//    public static RetData<String> getBalance(String addr, String token, String issuer, Integer precision,
//                                             ReqConfig<Web3j> config) throws Exception {
//        if (EthUtils.isOriginToken(token)) {
//            return getEthBalance(addr, config);
//        } else {
//            return getErc20Balance(addr, issuer, token, precision, config);
//        }
//    }
//}
