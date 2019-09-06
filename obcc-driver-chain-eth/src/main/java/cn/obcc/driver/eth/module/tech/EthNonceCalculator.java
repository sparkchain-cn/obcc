package cn.obcc.driver.eth.module.tech;

import cn.obcc.driver.nonce.NonceCalculator;
import cn.obcc.driver.tech.INonceCalculator;
import cn.obcc.exception.ObccException;
import cn.obcc.exception.enums.EExceptionCode;
import cn.obcc.vo.RetData;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;

import java.io.IOException;
import java.math.BigInteger;

public class EthNonceCalculator extends NonceCalculator<Web3j> implements INonceCalculator<Web3j> {

    @Override
    public RetData<Long> getNonceFromChain(String address) throws Exception {
        BigInteger nonce = getTransactionNonce(getClient(), address);
        if (nonce == null) {
            return RetData.error("get nonce is null!");
        }
        return RetData.succuess(nonce.longValue());
    }

    /**
     * 获取账号交易次数 nonce
     *
     * @param address 钱包地址
     * @return nonce
     */
    public static BigInteger getTransactionNonce(Web3j web3j, String address) throws ObccException {
        BigInteger nonce = BigInteger.ZERO;
        try {
            EthGetTransactionCount ethGetTransactionCount = web3j
                    .ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).send();

            nonce = ethGetTransactionCount.getTransactionCount();
        } catch (IOException e) {
            throw ObccException.create(EExceptionCode.IO_EXCEPTION,
                    "web3j mcGetTransactionCount io exception for " + address + e);
        }
        return nonce;
    }

}
