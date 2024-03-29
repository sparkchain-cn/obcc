package cn.obcc.driver.eth.module.tech.common;

import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.eth.utils.ContractUtils;
import cn.obcc.driver.eth.utils.GasFeeUtils;
import cn.obcc.driver.handler.IContractHandler;
import cn.obcc.driver.handler.ITokenHandler;
import cn.obcc.vo.contract.ContractRec;
import cn.obcc.vo.contract.TokenRec;
import cn.obcc.enums.EChainTxType;
import cn.obcc.enums.ETransferState;
import cn.obcc.utils.base.DateUtils;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.vo.BcMemo;
import cn.obcc.vo.db.BlockTxInfo;
import cn.obcc.vo.db.ContractInfo;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author mgicode
 * @version 1.0
 * @ClassName BlockTxInfoParser
 * @desc
 * @data 2019/9/3 8:55
 **/
public class BlockTxInfoParser {

    public static Logger logger = LoggerFactory.getLogger(BlockTxInfoParser.class);

    public static boolean isContract(Web3j web3j, Transaction t) {
        return ContractUtils.isContractAddr(web3j, t.getTo());
    }

    public static BlockTxInfo parseTxInfo(Web3j web3j, String chainCode, IChainDriver driver, Transaction t) throws Exception {
        boolean isContract = isContract(web3j, t);
        return parseTxInfo(web3j, chainCode, driver, t, isContract);
    }


    public static BlockTxInfo parseTxInfo(Web3j web3j, String chainCode,
                                          IChainDriver driver, Transaction t, boolean isContract) throws Exception {
        try {
            BlockTxInfo txInfo = new BlockTxInfo();
            txInfo.setChainCode(chainCode);

            BigInteger gasPrice = t.getGasPrice();
            String blockHash = t.getBlockHash();
            String hash = t.getHash();

            txInfo.setTxType(EChainTxType.Orign);
            txInfo.setHash(t.getHash());

            txInfo.setSrcAddr(t.getFrom());
            txInfo.setNonce(t.getNonce().longValue());
            txInfo.setAmount(getAmount(t));
            txInfo.setDestAddr(t.getTo());
            txInfo.setGasPrice(t.getGasPrice().longValue() + "");
            txInfo.setGasLimit(t.getGas().longValue() + "");

            txInfo.setBlockHash(t.getBlockHash());
            txInfo.setBlockNumber(t.getBlockNumber().longValue() + "");

            if (isContract) {
                parseContract(web3j, driver, t, txInfo);
            } else {
                txInfo.setMemos(t.getInput());
            }
            parseStatus(web3j, hash, gasPrice, txInfo);
            txInfo.setBlockTime(getTradeTime(web3j, blockHash));
            if (!StringUtils.isNullOrEmpty(txInfo.getMemos())) {
                String preHex = driver.getConfig().getMemoPreHex();
                // if (txInfo.getMemos().startsWith(preHex)) {
                //  int preHexLen = preHex.length();
                BcMemo memo = driver.getMemoParser().decode(txInfo.getMemos());
                txInfo.setMemosObj(memo);
                // }
            }
            return txInfo;

        } catch (Exception e) {
            logger.error("解析区块流水出错", e);
        }

        return null;
    }

    public static void parseContract(Web3j web3j, IChainDriver driver, Transaction t, BlockTxInfo tx) throws
            Exception {
        IContractHandler contractHandler = driver.getContractHandler();
        ITokenHandler tokenHandler = driver.getTokenHandler();

        if (t.getTo() == null) {
            return;
        }
        ContractInfo contractInfo = contractHandler.getContract(t.getTo());
        if (contractInfo == null) {
            logger.debug("Contract " + t.getTo() + "is not  Contract  registed in this system.");
        }
        ContractRec rec = contractHandler.parseTxInfo(contractInfo, t.getInput());

        tx.setTxType(EChainTxType.Contract);
        tx.setContractAddress(t.getTo());
        if (rec != null) {
            tx.setMethod(rec.getMethod());
            tx.setMethodParams(JSON.toJSONString(rec.getParams()));
        } else {
            logger.warn("can not parse the  ");
        }

        if (tokenHandler.isExistToken(t.getTo())) {
            TokenRec tokenRec = tokenHandler.parseTxInfo(t.getTo(), rec);
            tx.setDestAddr(tokenRec.getDestAddr());
            tx.setAmount(tokenRec.getAmount());
            tx.setMemos(tokenRec.getMemo());
            tx.setTxType(EChainTxType.Token);
        }
    }

    private static String getAmount(Transaction t) {
        return Convert.fromWei(new BigDecimal(t.getValue()), Convert.Unit.ETHER).toPlainString();
    }

    private static void parseStatus(Web3j web3j, String hash, BigInteger gasPrice, BlockTxInfo tx) {
        try {
            TransactionReceipt tr = web3j.ethGetTransactionReceipt(hash).send().getResult();
            if (tr != null) {
                tx.setGasUsed(GasFeeUtils.calUsedGasFee(tr, gasPrice));
                tx.setState(getState(tr.getStatus()) + "");

            } else {
                System.out.println("hahs:" + hash + " can not get  TransactionReceipt.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 从tr中取得状态
     *
     * @param state
     * @return
     */
    public static ETransferState getState(String state) {
        if (StringUtils.isNullOrEmpty(state)) {
            return ETransferState.STATE_CHAIN_DEFINITE_FAILURE;
        }
        if ("1".equals(state) || "0x1".equalsIgnoreCase(state)) {
            return ETransferState.STATE_SPC_SUCCESS;
        } else if ("0".equals(state)) {
            return ETransferState.STATE_CHAIN_DEFINITE_FAILURE;
        }
        // tr.getStatus().equalsIgnoreCase("1")

        return ETransferState.STATE_CHAIN_DEFINITE_FAILURE;

    }


    // todo:取不到就是当前的时间
    public static String getTradeTime(Web3j web3j, String blockHash) {
        try {
            BigInteger timeStamp = null;
            if (!StringUtils.isNullOrEmpty(blockHash)) {
                timeStamp = web3j.ethGetBlockByHash(blockHash, false).send().getBlock().getTimestamp();
            }
            if (timeStamp == null) {
                return null;
            }
            return DateUtils.getDateFormat(new Date(timeStamp.longValue()), "MM/dd/yyyy HH:mm:ss");
            //return timeStamp == null ? (System.currentTimeMillis() / 1000) : timeStamp.longValue();
        } catch (IOException e) {
            logger.error(
                    "chain3j io exception mcGetBlockByHash error for:" + blockHash + "," + StringUtils.exception(e));
        } catch (Exception ex) {
            logger.error(
                    "chain3j io exception mcGetBlockByHash error for:" + blockHash + "," + StringUtils.exception(ex));
        }

        return null;
        // return (0L);
    }

}
