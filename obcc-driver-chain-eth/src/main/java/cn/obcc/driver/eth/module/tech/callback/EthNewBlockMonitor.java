package cn.obcc.driver.eth.module.tech.callback;

import cn.obcc.config.ObccConfig;
import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.eth.module.tech.common.BlockTxInfoParser;
import cn.obcc.utils.HexUtils;
import cn.obcc.vo.driver.BlockTxInfo;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.ArrayList;
import java.util.List;

public class EthNewBlockMonitor {

    public Logger logger = LoggerFactory.getLogger(EthNewBlockMonitor.class);

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
        newTransactionFilter(web3j, config.getChain().getName());
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


    private void newTransactionFilter(Web3j web3j, String chainCode) {
        Flowable<Transaction> flow = web3j.transactionFlowable();
        Transaction a;
        TransactionReceipt r;

        Disposable d;
        d = flow.subscribe((t) -> {
            try {
                if (t == null) return;
                //过滤非本应用的
                boolean isContract = BlockTxInfoParser.isContract(web3j, t);
                if (isContract) {
                    //todo:是我们的合约，我们的合约需要加上标识，或者从数据库取出进行比较
                } else {
                    if (!t.getInput().startsWith(HexUtils.str2Hex(config.getMemoPre()))) {
                        return;
                    }
                }
                //解析区块流水
                BlockTxInfo txInfo = BlockTxInfoParser.parseTxInfo(web3j, chainCode, driver, t, isContract);
                //todo:修改异步
                //把数据写到数据库中
                driver.getCallbackListener().writeToDb(txInfo);
                //通知给上链的junction
                driver.getCallbackListener().notify(txInfo);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, (e) -> {
            e.printStackTrace();
        });
        subcribeList.add(d);
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
