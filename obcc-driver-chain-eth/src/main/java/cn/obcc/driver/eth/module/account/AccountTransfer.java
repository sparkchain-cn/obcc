package cn.obcc.driver.eth.module.account;

import cn.obcc.config.ExConfig;
import cn.obcc.driver.eth.EthConstants;
import cn.obcc.driver.tech.INonceCalculator;
import cn.obcc.vo.driver.FromAccount;
import cn.obcc.exception.ObccException;
import cn.obcc.enums.EExceptionCode;
import cn.obcc.utils.base.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.net.SocketTimeoutException;

/**
 * @author mgicode
 * @version 1.0
 * @ClassName AccountTransfer
 * @desc
 * @data 2019/9/2 21:20
 **/
public class AccountTransfer {
    public static final Logger logger = LoggerFactory.getLogger(AccountTransfer.class);


    public static EthSendTransaction trySendTx(FromAccount fromAccount, BigInteger amount, String destAddress,
                                               INonceCalculator nonceCalculator, ExConfig config, Web3j web3j) throws Exception {
        EthSendTransaction result = null;
        int i = 0;
        BigInteger nowSeq = BigInteger.valueOf(Long.parseLong(fromAccount.getNonce()));
        while (i <= 20) {
            i++;
            result = sendTx(web3j, fromAccount, amount, destAddress);
            if (result == null) {
                throw ObccException.create(EExceptionCode.RETURN_NULL_OR_EMPTY,
                        "web3j mcSendRawTransaction return null object(McSendTransaction).");
            }
            Response.Error err = result.getError();
            if (err == null) {
                return result;
            }
            // 当前的nounce+1,来计算
            if (isNouceLowError(err.getMessage())) {
                //"nonce too low";//在区块上，
                nowSeq = nowSeq.add(BigInteger.valueOf(1L));
                nonceCalculator.adjustNonce(fromAccount.getSrcAddr(), nowSeq.longValue(), config);
                fromAccount.setNonce(nowSeq + "");
                continue;
            } else if (isInNouceQueueError(err.getMessage())) {
                // known transaction";//在队列中，nonce存在，参数不同，说明不是冲掉
                nowSeq = nowSeq.add(BigInteger.valueOf(1L));
                nonceCalculator.adjustNonce(fromAccount.getSrcAddr(), nowSeq.longValue(), config);
                fromAccount.setNonce(nowSeq + "");
                continue;
            } else if (isUnderPriceError(err.getMessage())) {
                // "replacement transaction underpriced";
                // 在队列中，nonce存在，参数相同，说明要冲掉，但是冲掉费用过低,增加其limit
                Long gasLimit = fromAccount.getGasLimit().longValue() * (1 + EthConstants.SafeFactor / 100);
                fromAccount.setGasLimit(gasLimit);
                continue;
            }
        }
        return result;

    }

    public static EthSendTransaction sendTx(Web3j web3j, FromAccount account, BigInteger amount, String destAddress) throws ObccException {

        String signedTransactionData = signEthTxData(
                account.getSecret(), destAddress, BigInteger.valueOf(Long.parseLong(account.getNonce())),
                account.getGasPrice(), account.getGasLimit(), amount, account.getMemos() == null ? "" : account.getMemos());

        EthSendTransaction est = null;
        try {
            est = web3j.ethSendRawTransaction(signedTransactionData).send();
        } catch (SocketTimeoutException ex) {
            logger.error(StringUtils.exception(ex));
            throw ObccException.create(EExceptionCode.IO_EXCEPTION, "web3j SocketTimeoutException error.");
        } catch (IOException e) {
            logger.error(StringUtils.exception(e));
            throw ObccException.create(EExceptionCode.IO_EXCEPTION, "web3j IOException error.");
        }
        return est;
    }

    public static String signEthTxData(String privateKey, String to, BigInteger nonce, Long gasPrice, Long gasLimit,
                                       BigInteger value, String hexData) {
        // 把十进制的转换成ETH的Wei, 1ETH = 10^18 Wei
        System.out.println("nonce:" + nonce + ",GasPrice:" + gasPrice + ",GasLimit:" + gasLimit
                + ",to:" + to + ",value:" + value + ",hexData:" + hexData);
        System.out.println("privateKey:" + privateKey);
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, BigInteger.valueOf(gasPrice),
                BigInteger.valueOf(gasLimit), to, value, hexData);

        Credentials credentials = Credentials.create(privateKey);
        // 使用TransactionEncoder对RawTransaction进行签名操作
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        // 转换成0x开头的字符串
        return Numeric.toHexString(signedMessage);

    }

    public static boolean isNouceLowError(String msg) {
        if (msg == null) {
            return false;
        }
        if (msg.contains(EthConstants.ERROR_MOAC_NTL)) {
            return true;
        }
        return false;

    }

    public static boolean isInNouceQueueError(String msg) {
        if (msg == null) {
            return false;
        }
        if (msg.contains(EthConstants.ERROR_MOAC_KT)) {
            return true;
        }
        return false;

    }

    public static boolean isUnderPriceError(String msg) {
        if (msg == null) {
            return false;
        }
        if (msg.contains(EthConstants.ERROR_MOAC_RTU)) {
            return true;
        }
        return false;

    }


    private static String signTx(Integer chainId, RawTransaction rawTransaction, String privateKey) throws Exception {
        Credentials credentials = Credentials.create(privateKey);
        byte[] signedMessage = null;
        signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId.byteValue(), credentials);
        // 转换成0x开头的字符串
        return Numeric.toHexString(signedMessage);
    }


}
