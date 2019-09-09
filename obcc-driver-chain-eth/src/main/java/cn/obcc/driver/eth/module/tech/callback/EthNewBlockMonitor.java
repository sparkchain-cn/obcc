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

    private BlockTxInfo supportUpchain(Transaction t) throws Exception {
        String hash = t.getHash();

        if (driver.getStateMonitor().exist(hash)) {
            //过滤非本应用的
            if (!t.getInput().startsWith(config.getMemoPreHex())) {
                return null;
            }
            boolean isContract = BlockTxInfoParser.isContract(web3j, t);
            //解析区块流水
            String chainCode = driver.getObccConfig().getChain().getName();
            BlockTxInfo txInfo = BlockTxInfoParser.parseTxInfo(web3j, chainCode, driver, t, isContract);
            //todo:修改异步
            if (txInfo != null) {
                //通知给上链的junction
                driver.getCallbackListener().notify(txInfo);
            }

            return txInfo;
        }

        return null;
    }

    /**
     * 解析只是我们的合约和交易的流水，并保存
     *
     * @param t
     * @return
     * @throws Exception
     */
    private BlockTxInfo supportQuery(Transaction t) throws Exception {
        String hash = t.getHash();
        //过滤非本应用的
        boolean isContract = BlockTxInfoParser.isContract(web3j, t);
        //是合约不在数据库中，过滤。对数据库冲击比较
        if (isContract && driver.getContractHandler().getContract(t.getTo()) == null) {
            return null;
        } //不是合约，备注不是以指定的前缀开头，过滤
        else if (!isContract && !t.getInput().startsWith(config.getMemoPreHex())) {
            return null;
        }

        //解析区块流水
        String chainCode = driver.getObccConfig().getChain().getName();
        BlockTxInfo txInfo = BlockTxInfoParser.parseTxInfo(web3j, chainCode, driver, t, isContract);
        //todo:修改异步
        if (txInfo != null) {
            //把数据写到数据库中
            driver.getCallbackListener().writeToDb(txInfo);
            //通知给上链的junction
            driver.getCallbackListener().notify(txInfo);
        }

        return txInfo;
    }


    private BlockTxInfo supportAll(Transaction t) throws Exception {
        String hash = t.getHash();
        //过滤非本应用的
        boolean isContract = BlockTxInfoParser.isContract(web3j, t);
        //解析区块流水
        String chainCode = driver.getObccConfig().getChain().getName();
        BlockTxInfo txInfo = BlockTxInfoParser.parseTxInfo(web3j, chainCode, driver, t, isContract);
        //todo:修改异步
        if (txInfo != null) {
            //把数据写到数据库中
            driver.getCallbackListener().writeToDb(txInfo);
            //通知给上链的junction
            driver.getCallbackListener().notify(txInfo);
        }

        return txInfo;
    }

    private void newTransactionFilter(Web3j web3j, String chainCode) {
        Flowable<Transaction> flow = web3j.transactionFlowable();
        Disposable d;
        d = flow.subscribe((t) -> {
            try {
                if (t == null) return;
                BlockTxInfo info = null;
                switch (driver.getObccConfig().getCallBackLevel()) {
                    case SupportUpchain:
                        info = supportUpchain(t);
                        break;
                    case SupportQuery:
                        info = supportQuery(t);
                        break;
                    case SupportAll:
                        info = supportAll(t);
                        break;
                    default:
                        return;
                }


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
