package cn.obcc.driver.eth.module.account;

import cn.obcc.config.ReqConfig;
import cn.obcc.driver.eth.EthConstants;
import cn.obcc.driver.eth.utils.EthUtils;
import cn.obcc.driver.tech.INonceCalculator;
import cn.obcc.driver.utils.JunctionUtils;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.exception.ObccException;
import cn.obcc.exception.enums.EExceptionCode;
import cn.obcc.utils.base.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.gas.StaticGasProvider;

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


    public static  EthSendTransaction trySendTx(SrcAccount account, BigInteger amount, String destAddress,
                                         INonceCalculator nonceCalculator, ReqConfig<Web3j> config) throws Exception {
        EthSendTransaction result = null;
        int i = 0;
        BigInteger nowSeq = BigInteger.valueOf(Long.parseLong(account.getNonce()));
        while (i <= 20) {
            i++;
            result = sendTx(config.getClient(), account, amount, destAddress);
            if (result == null) {
                throw ObccException.create(EExceptionCode.RETURN_NULL_OR_EMPTY,
                        "web3j mcSendRawTransaction return null object(McSendTransaction).");
            }
            Response.Error err = result.getError();
            if (err == null) {
                return result;
            }
            // 当前的nounce+1,来计算
            if (EthUtils.isNouceLowError(err.getMessage())) {
                //"nonce too low";//在区块上，
                nowSeq = nowSeq.add(BigInteger.valueOf(1L));
                nonceCalculator.adjustNonce(account.getAccount(), nowSeq.longValue(), config);
                continue;
            } else if (EthUtils.isInNouceQueueError(err.getMessage())) {
                // known transaction";//在队列中，nonce存在，参数不同，说明不是冲掉
                nowSeq = nowSeq.add(BigInteger.valueOf(1L));
                nonceCalculator.adjustNonce(account.getAccount(), nowSeq.longValue(), config);
                continue;
            } else if (EthUtils.isUnderPriceError(err.getMessage())) {
                // "replacement transaction underpriced";
                // 在队列中，nonce存在，参数相同，说明要冲掉，但是冲掉费用过低,增加其limit
                Long gasLimit = account.getGasLimit().longValue() * (1 + EthConstants.SafeFactor / 100);
                account.setGasLimit(gasLimit);
                continue;
            }
        }
        return result;

    }

    public static  EthSendTransaction sendTx(Web3j web3j, SrcAccount account, BigInteger amount, String destAddress) throws ObccException {

        String signedTransactionData = EthUtils.signEthTxData(
                account.getSecret(), destAddress, BigInteger.valueOf(Long.parseLong(account.getNonce())),
                account.getGasPrice(), account.getGasLimit(), amount, account.getMemos());

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

}
