package cn.obcc.driver.eth.module.tech.callback;

import cn.obcc.config.ObccConfig;
import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.eth.utils.EthUtils;
import cn.obcc.driver.module.IContractHandler;
import cn.obcc.driver.module.ITokenHandler;
import cn.obcc.vo.driver.BlockTxInfo;
import cn.obcc.driver.vo.ContractExecRec;
import cn.obcc.driver.vo.TokenRec;
import cn.obcc.exception.enums.EChainTxType;
import cn.obcc.vo.driver.ContractInfo;
import cn.obcc.vo.driver.TokenInfo;
import com.alibaba.fastjson.JSON;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class EthNewBlockMonitor {

    public Logger logger = LoggerFactory.getLogger(getClass());

    public List<Disposable> subcribeList = new ArrayList<>();

    private Web3j web3j;
    private ObccConfig config;
    IChainDriver<Web3j> driver;

    public List<Disposable> init(Web3j web3j, IChainDriver<Web3j> drive, ObccConfig config) {

        this.web3j = web3j;
        this.config = config;
        this.driver = drive;
        /**
         * 新区块监听
         */
        // newBlockFilter(web3j);
        /**
         * 新交易监听 //
         */
        newTransactionFilter(web3j, config.getChain().name());
        /**
         * 遍历旧区块、交易
         */
        // replayFilter(web3j);
        /**
         * 从某一区块开始直到最新区块、交易
         */
        // catchUpFilter(web3j);
        /**
         * 取消监听
         */
        // subscription.unsubscribe();

        return subcribeList;
    }

    private boolean isContract(Web3j web3j, Transaction t) {
        return EthUtils.isContractAddr(web3j, t.getTo());
    }


    private void parseContract(Web3j web3j, Transaction t, BlockTxInfo tx) throws Exception {
        IContractHandler contractHandler = driver.getContractHandler();
        ITokenHandler tokenHandler = driver.getTokenHandler();
        ContractInfo contractInfo = contractHandler.getContract(t.getTo());
        if (contractInfo == null) {
            logger.debug("Contract " + t.getTo() + "is not  Contract  registed in this system.");
        }
        ContractExecRec rec = contractHandler.parseExecRec(contractInfo, t.getInput());

        tx.setTxType(EChainTxType.Contract);
        tx.setContractAddress(t.getTo());
        tx.setMethod(rec.getMethod());
        tx.setMethodParams(JSON.toJSONString(rec.getParams()));

        if (tokenHandler.isToken(t.getTo())) {
            TokenInfo tokenInfo = tokenHandler.getToken(t.getTo());
            TokenRec tokenRec = tokenHandler.parseExecRec(tokenInfo, rec);
            tx.setDestAddr(tokenRec.getDestAddr());
            tx.setAmount(tokenRec.getAmount());
            tx.setMemos(tokenRec.getMemo());
            tx.setTxType(EChainTxType.Token);
        }
    }

    private String getAmount(Transaction t) {
        return Convert.fromWei(new BigDecimal(t.getValue()), Unit.ETHER).toPlainString();
    }

    private void newTransactionFilter(Web3j web3j, String chainCode) {
        Flowable<Transaction> flow = web3j.transactionFlowable();
        Transaction a;
        TransactionReceipt r;

        Disposable d;
        d = flow.subscribe(t -> {
            if (t == null) return;
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

                if (isContract(web3j, t)) {
                    parseContract(web3j, t, txInfo);
                }

                parseStatus(web3j, hash, gasPrice, txInfo);
                txInfo.setBlockTime(EthUtils.getTradeTime(web3j, blockHash));

                //todo:修改异步
                //把数据写到数据库中
                driver.getCallbackListener().writeToDb(txInfo);
                //通知给上链的junction
                driver.getCallbackListener().notify(txInfo);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }, t -> {
            t.printStackTrace();
        });
        subcribeList.add(d);
    }

    private void parseStatus(Web3j web3j, String hash, BigInteger gasPrice, BlockTxInfo tx) {
        try {
            TransactionReceipt tr = web3j.ethGetTransactionReceipt(hash).send().getResult();
            if (tr != null) {
                tx.setGasUsed(EthUtils.calUsedGasFee(tr, gasPrice));
                tx.setState(EthUtils.getState(tr.getStatus()) + "");

            } else {
                System.out.println("hahs:" + hash + " can not get  TransactionReceipt.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    private static void newBlockFilter(Web3j web3j) {
//        web3j.
//                blockFlowable(false).
//                subscribe(block -> {
//                    System.out.println("new block come in");
//                    System.out.println("block number" + block.getBlock().getNumber());
//                });
//    }
//    private static void replayFilter(Web3j web3j) {
//        BigInteger startBlock = BigInteger.valueOf(2000000);
//        BigInteger endBlock = BigInteger.valueOf(2010000);
//        /**
//         * 遍历旧区块
//         */
//        Subscription subscription = web3j.
//                replayBlocksObservable(
//                        DefaultBlockParameter.valueOf(startBlock),
//                        DefaultBlockParameter.valueOf(endBlock),
//                        false).
//                subscribe(ethBlock -> {
//                    System.out.println("replay block");
//                    System.out.println(ethBlock.getBlock().getNumber());
//                });
//
//        /**
//         * 遍历旧交易
//         */
//        Subscription subscription1 = web3j.
//                replayTransactionsObservable(
//                        DefaultBlockParameter.valueOf(startBlock),
//                        DefaultBlockParameter.valueOf(endBlock)).
//                subscribe(transaction -> {
//                    System.out.println("replay transaction");
//                    System.out.println("txHash " + transaction.getHash());
//                });
//    }
//
//    private static void catchUpFilter(Web3j web3j) {
//        BigInteger startBlock = BigInteger.valueOf(2000000);
//
//        /**
//         * 遍历旧区块，监听新区块
//         */
//        Subscription subscription = web3j.catchUpToLatestAndSubscribeToNewBlocksObservable(
//                DefaultBlockParameter.valueOf(startBlock), false)
//                .subscribe(block -> {
//                    System.out.println("block");
//                    System.out.println(block.getBlock().getNumber());
//                });
//
//        /**
//         * 遍历旧交易，监听新交易
//         */
//        Subscription subscription2 = web3j.catchUpToLatestAndSubscribeToNewTransactionsObservable(
//                DefaultBlockParameter.valueOf(startBlock))
//                .subscribe(tx -> {
//                    System.out.println("transaction");
//                    System.out.println(tx.getHash());
//                });
//    }
}
