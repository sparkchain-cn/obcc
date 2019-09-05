package cn.obcc.driver.eth.module;

import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.eth.module.tech.common.BlockTxInfoParser;
import cn.obcc.driver.module.IBlockHandler;
import cn.obcc.exception.ObccException;
import cn.obcc.exception.enums.EExceptionCode;
import cn.obcc.vo.driver.BlockInfo;
import cn.obcc.vo.driver.BlockTxInfo;
import cn.obcc.config.ReqConfig;
import cn.obcc.vo.RetData;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class EthBlockHandler extends BaseHandler<Web3j> implements IBlockHandler<Web3j> {


    @Override
    public Long getInterval() throws Exception {
        return 10 * 1000L;//10s
    }

    @Override
    public RetData<Long> getBlockHeight(ReqConfig<Web3j> config) throws Exception {
        Web3j web3j = config.getClient();
        BigInteger num = web3j.ethBlockNumber().send().getBlockNumber();
        if (num != null) {
            return RetData.succuess(num.longValue());
        }
        return RetData.error(EExceptionCode.RETURN_NULL_OR_EMPTY, "getBlockHeight can get value.");
    }

    @Override
    public RetData<BlockInfo> getBlockInfo(long blockHeight, ReqConfig<Web3j> config) throws Exception {
        Web3j web3j = config.getClient();
        EthBlock.Block block = web3j.ethGetBlockByNumber(
                DefaultBlockParameter.valueOf(BigInteger.valueOf(blockHeight)), false).send().getBlock();

        BlockInfo blockInfo = new BlockInfo();
        blockInfo.setBlockHash(block.getHash());
        blockInfo.setBlockNumber(block.getNumber() + "");

        List<String> txHashs = new ArrayList<String>();
        for (EthBlock.TransactionResult t : block.getTransactions()) {
            Transaction transaction = (Transaction) t.get();
            txHashs.add(transaction.getHash());
        }
        blockInfo.setTransactions(txHashs);
        blockInfo.setTradeTime(Long.parseLong(block.getTimestamp().toString()));

        return RetData.succuess(blockInfo);
    }



    @Override
    public RetData<BlockTxInfo> pull(String hash, ReqConfig<Web3j> config) throws Exception {
        try {
            Web3j web3j = config.getClient();
            Transaction tx;
            String chaincode = getObccConfig().getChain().getName();
            tx = web3j.ethGetTransactionByHash(hash).send().getTransaction().get();
            BlockTxInfo txInfo = BlockTxInfoParser.parseTxInfo(web3j, chaincode, getDriver(), tx);
            if (txInfo != null) {
                return RetData.succuess(txInfo);
            }
        } catch (Exception e) {
            throw ObccException.create(EExceptionCode.FETCH_TX_FAIL, e.getMessage());
        }
        return RetData.error("没有找到返回的结果");
    }

}
