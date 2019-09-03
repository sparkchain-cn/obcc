//package cn.obcc.driver.eth.module.account;
//
//import java.io.IOException;
//import java.math.BigInteger;
//
//import cn.obcc.driver.IChainDriver;
//import cn.obcc.driver.eth.module.tech.callback.EthNewBlockMonitor;
//import cn.obcc.driver.module.IContractHandler;
//import cn.obcc.driver.module.ITokenHandler;
//import cn.obcc.driver.vo.ContractExecRec;
//import cn.obcc.driver.vo.TokenRec;
//import cn.obcc.exception.enums.EChainTxType;
//import cn.obcc.vo.driver.BlockTxInfo;
//import cn.obcc.vo.driver.ContractInfo;
//import cn.obcc.vo.driver.TokenInfo;
//import com.alibaba.fastjson.JSON;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.web3j.protocol.Web3j;
//import org.web3j.protocol.core.DefaultBlockParameter;
//import org.web3j.protocol.core.methods.response.EthBlock;
//import org.web3j.protocol.core.methods.response.Transaction;
//import org.web3j.protocol.core.methods.response.TransactionReceipt;
//
//import cn.obcc.config.constant.EthConstants;
//import cn.obcc.driver.eth.utils.ContractTransferInput;
//import cn.obcc.driver.eth.utils.EthUtils;
//import cn.obcc.driver.vo.TransactionInfo;
//import cn.obcc.exception.ObccException;
//import cn.obcc.exception.enums.EExceptionCode;
//import cn.obcc.exception.enums.StateEnum;
//
//import cn.obcc.utils.base.StringUtils;
//import cn.obcc.config.ReqConfig;
//
//public class AccountTxInfo {
//
//    public static Logger logger = LoggerFactory.getLogger(AccountTxInfo.class);
//
//    public static TransactionInfo getTransaction(String hash, IChainDriver<Web3j> driver,
//                                                 ReqConfig<Web3j> config) throws Exception {
//        try {
//            Web3j chain3j = config.getClient();
//            Transaction tx;
//            try {
//                tx = chain3j.ethGetTransactionByHash(hash).send().getTransaction().get();
//            } catch (Exception e) {
//                throw ObccException.create(EExceptionCode.FETCH_TX_FAIL, e.getMessage());
//            }
//            TransactionInfo spcTx = new TransactionInfo();
//
//            spcTx.setHash(hash);
//            // spcTx.setChain(config.getChainCode());
//            spcTx.setSourceAddress(tx.getFrom());
//            spcTx.setMemos("");
//            spcTx.setNonce(tx.getNonce().longValue());
//
//            // handle block info
//            String blockNumber = tx.getBlockNumberRaw();
//            if (StringUtils.isNullOrEmpty(blockNumber)) {
//                spcTx.setBlockNumber("");
//                spcTx.setState(StateEnum.CHAIN_PENDING.getState().intValue() + "");
//                spcTx.setTradeTime(0L);
//                spcTx.setBlockTime(0L);
//                spcTx.setContractAddress("");
//            } else {
//                BigInteger blockNumberInt = tx.getBlockNumber();
//                spcTx.setBlockNumber(blockNumberInt.toString());
//                spcTx.setState(StateEnum.BLOCK_SUCC.getState().intValue() + "");
//                EthBlock.Block block = chain3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(blockNumberInt), false)
//                        .send().getBlock();
//                Long tradeTime = EthUtils.getTradeTime(chain3j, block.getHash());
//                spcTx.setTradeTime(tradeTime);
//                spcTx.setBlockTime(tradeTime);
//                TransactionReceipt receipt = chain3j.ethGetTransactionReceipt(hash).send().getTransactionReceipt()
//                        .get();
//                spcTx.setContractAddress(receipt.getContractAddress());
//                String status = receipt.getStatus();
//                if (status.equalsIgnoreCase("0x0")) { // status == false
//                    spcTx.setState(StateEnum.CHAIN_TRADE_FAIL.getState().intValue() + "");
//                }
//                BigInteger gasFee = receipt.getGasUsed().multiply(tx.getGasPrice());
//                String gasFeeString = EthUtils.fromValueInMin(gasFee, 18).toString();
//                spcTx.setGasFee(gasFeeString);
//            }
//
//            // handle memos
//            if (EthUtils.isContractAddress(chain3j, tx.getTo())) {
//                // 非这里创建的transfer过滤掉
//                if (StringUtils.isNotNullOrEmpty(tx.getInput()) && tx.getInput().length() >= (10 + 64 + 64)) {
//                    ContractTransferInput contractTx = ContractTransferInput.parse(tx.getInput(), decimal);
//                    spcTx.setDestinationAddress(contractTx.getDestAddr());
//                    spcTx.setAmount(contractTx.getAmount());
//
//                    if (StringUtils.isNotNullOrEmpty(contractTx.getMemos())) {
//                        String memo = SpcMemoUtils.decodeOrign(contractTx.getMemos(), "");
////                        System.out.println("\n\n合约中解决出memo buzid::" + memo);
//                        if (StringUtils.isNotNullOrEmpty(memo)) {
//                            spcTx.setMemos(memo);
//                        } else {
//                            spcTx.setMemos(contractTx.getMemos());
//                        }
//                    } else {
//                        spcTx.setMemos(contractTx.getMemos());
//                    }
//                }
//            } else {
//                spcTx.setDestinationAddress(tx.getTo());
//                String amount = EthUtils.parseHexValue(tx.getValueRaw(), decimal);
//                spcTx.setAmount(amount);
//                String memo = SpcMemoUtils.decodeOrign(HexStringUtils.hexStr2Str(tx.getInput()), "");
//                spcTx.setMemos(memo);
//                spcTx.setContractAddress(EthConstants.EMPTY_ADDRESS);
//            }
//
//            return spcTx;
//        } catch (IOException e) {
//            String msg = "Can not get transaction from hash:" + hash + ", because io exception.";
//            throw ObccException.create(EExceptionCode.IO_EXCEPTION, msg);
//        }
//    }
//
//}
