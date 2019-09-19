package cn.obcc.driver.eth.module.tech.callback;

import cn.obcc.config.ObccConfig;
import cn.obcc.driver.IChainDriver;
import cn.obcc.driver.eth.module.tech.common.BlockTxInfoParser;
import cn.obcc.vo.driver.BlockTxInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Transaction;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName EthCbSupport
 * @desc TODO
 * @date 2019/9/17 0017  14:16
 **/
public class EthCbSupport {

    public Logger logger = LoggerFactory.getLogger(EthCbSupport.class);

    private Web3j web3j;
    private ObccConfig config;
    IChainDriver<Web3j> driver;

    public EthCbSupport(Web3j web3j, IChainDriver<Web3j> drive, ObccConfig config) {

        this.web3j = web3j;
        this.config = config;
        this.driver = drive;
    }


    public BlockTxInfo supportUpchain(Transaction t) throws Exception {
        String hash = t.getHash();
        if (driver.getStateMonitor().existHash(hash)) {
            //过滤非本应用的
            if (!t.getInput().startsWith(config.getMemoPreHex())) {
                return null;
            }
            boolean isContract = BlockTxInfoParser.isContract(web3j, t);
            //解析区块流水
            String chainCode = driver.getConfig().getChain().getName();
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
    public BlockTxInfo supportQuery(Transaction t) throws Exception {
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
        String chainCode = driver.getConfig().getChain().getName();
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


    public BlockTxInfo supportAll(Transaction t) throws Exception {
        String hash = t.getHash();
        //过滤非本应用的
        boolean isContract = BlockTxInfoParser.isContract(web3j, t);
        //解析区块流水
        String chainCode = driver.getConfig().getChain().getName();
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
}
