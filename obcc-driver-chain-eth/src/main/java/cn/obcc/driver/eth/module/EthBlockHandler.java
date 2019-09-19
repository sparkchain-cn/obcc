package cn.obcc.driver.eth.module;

import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.eth.module.tech.common.BlockTxInfoParser;
import cn.obcc.driver.module.IBlockHandler;
import cn.obcc.exception.ObccException;
import cn.obcc.enums.EExceptionCode;
import cn.obcc.vo.driver.BlockInfo;
import cn.obcc.vo.driver.BlockTxInfo;
import cn.obcc.config.ExConfig;
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
    public Long getBlockHeight() throws Exception {
        Web3j web3j = getClient();
        BigInteger num = web3j.ethBlockNumber().send().getBlockNumber();
        if (num != null) {
            return num.longValue();
        }
        return null;
    }

    @Override
    public BlockInfo getBlockInfo(long blockHeight, ExConfig config) throws Exception {
        Web3j web3j = getClient();//config.getClient();
        EthBlock.Block block = web3j.ethGetBlockByNumber(
                DefaultBlockParameter.valueOf(BigInteger.valueOf(blockHeight)), false).send().getBlock();

        BlockInfo blockInfo = new BlockInfo();
        blockInfo.setBlockHash(block.getHash());
        blockInfo.setBlockNumber(block.getNumber() + "");

        List<String> txHashs = new ArrayList<String>();
        for (EthBlock.TransactionResult t : block.getTransactions()) {
            String hash = (String) t.get();
            txHashs.add(hash);
        }
        blockInfo.setTransactions(txHashs);
        blockInfo.setTradeTime(Long.parseLong(block.getTimestamp().toString()));

        return blockInfo;
    }


    @Override
    public BlockTxInfo getBlockTxInfo(String hash, ExConfig config) throws Exception {
        try {
            Web3j web3j = getClient();//config.getClient();
            Transaction tx;
            String chaincode = getConfig().getChain().getName();
            tx = web3j.ethGetTransactionByHash(hash).send().getTransaction().get();
            BlockTxInfo txInfo = BlockTxInfoParser.parseTxInfo(web3j, chaincode, getDriver(), tx);
            if (txInfo != null) {
                return txInfo;
            }
        } catch (Exception e) {
            throw ObccException.create(EExceptionCode.FETCH_TX_FAIL, e.getMessage());
        }
        return null;
    }

}
