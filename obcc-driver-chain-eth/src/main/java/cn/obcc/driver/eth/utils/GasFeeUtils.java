package cn.obcc.driver.eth.utils;

import cn.obcc.exception.ObccException;
import cn.obcc.enums.EExceptionCode;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author mgicode
 * @version 1.0
 * @ClassName GasFeeUtils
 * @desc
 * @data 2019/9/5 16:22
 **/
public class GasFeeUtils {

    /**
     * 获取普通交易的gas上限
     *
     * @param transaction 交易对象
     * @return gas 上限
     */
    public static BigInteger getGasLimit(Web3j web3j, Transaction transaction) throws ObccException {
        BigInteger gasLimit = BigInteger.ZERO;
        try {
            EthEstimateGas ethEstimateGas = web3j.ethEstimateGas(transaction).send();
            gasLimit = ethEstimateGas.getAmountUsed();
        } catch (IOException e) {
            throw ObccException.create(EExceptionCode.IO_EXCEPTION, "web3j mcEstimateGas error." + e);
        }
        return gasLimit;
    }

    public static BigInteger getGasPrice(Web3j web3j) throws ObccException {
        BigInteger gasPrice = BigInteger.ZERO;
        try {
            gasPrice = web3j.ethGasPrice().send().getGasPrice();

        } catch (IOException e) {
            throw ObccException.create(EExceptionCode.IO_EXCEPTION, "web3j mcGasPrice error." + e);
        }
        return gasPrice;
    }


    public static String calUsedGasFee(TransactionReceipt tr, BigInteger gasPrice) {

        if (tr == null) {
            return null;
        }
        BigInteger gasFee = tr.getGasUsed().multiply(gasPrice);

        String gasFeeStr = Convert.fromWei(new BigDecimal((gasFee)), Convert.Unit.ETHER).toPlainString();

        return gasFeeStr;
    }

    //        if (!EthUtils.isNgDigit(gasTwo.getGasPrice()) || !EthUtils.isNgDigit(gasTwo.getGasLimit())) {
//            Transaction tran = new Transaction(account.getAccount(), null,
//                    null, null, destAddress, BigInteger.valueOf(0), account.getMemos());
//
//            // 不是必须的 可以使用默认值
//            BigInteger gasLimit = EthUtils.getGasLimit(web3j, tran);
//            gasLimit = BigInteger.valueOf(gasLimit.longValue() * EthConstants.SafeFactor);
//
//            BigInteger gasPrice = EthUtils.getGasPrice(web3j);
//            return new StaticGasProvider(gasPrice, gasLimit);
//
//        }
}