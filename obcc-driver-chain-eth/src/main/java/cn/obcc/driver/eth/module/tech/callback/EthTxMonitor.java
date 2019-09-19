package cn.obcc.driver.eth.module.tech.callback;

import cn.obcc.config.ObccConfig;
import cn.obcc.driver.IChainDriver;
import cn.obcc.vo.driver.BlockTxInfo;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Transaction;

import java.util.ArrayList;
import java.util.List;

public class EthTxMonitor {

    public Logger logger = LoggerFactory.getLogger(EthTxMonitor.class);

    public List<Disposable> subcribeList = new ArrayList<>();

    private Web3j web3j;
    private ObccConfig config;
    IChainDriver<Web3j> driver;
    EthCbSupport ethCbSupport;

    public List<Disposable> init(Web3j web3j, IChainDriver<Web3j> drive, ObccConfig config) {

        this.web3j = web3j;
        this.config = config;
        this.driver = drive;
        ethCbSupport = new EthCbSupport(web3j, drive, config);

        newTransactionFilter(web3j, config.getChain().getName());

        return subcribeList;
    }

    private void newTransactionFilter(Web3j web3j, String chainCode) {
        Flowable<Transaction> flow = web3j.transactionFlowable();
        Disposable d = flow.subscribe((t) -> {
            try {
                if (t == null) {
                    return;
                }
                BlockTxInfo info = null;
                switch (driver.getConfig().getCallBackLevel()) {
                    case SupportUpchain:
                        info = ethCbSupport.supportUpchain(t);
                        break;
                    case SupportQuery:
                        info = ethCbSupport.supportQuery(t);
                        break;
                    case SupportAll:
                        info = ethCbSupport.supportAll(t);
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


}
