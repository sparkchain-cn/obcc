package cn.obcc.notify.consul;


import cn.obcc.def.callback.ICallBackNotify;
import cn.obcc.driver.IChainDriver;
import cn.obcc.stragegy.BaseStrategy;
import cn.obcc.vo.db.BlockTxInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mgicode
 * @version 1.0
 * @ClassName ProcessCallbackNotify
 * @desc
 * @data 2019/9/3 14:47
 **/
public class ConsulNofityStrategy extends BaseStrategy<IChainDriver> implements ICallBackNotify<IChainDriver> {
    public static final Logger logger = LoggerFactory.getLogger(ConsulNofityStrategy.class);


    @Override
    public void notify(BlockTxInfo txInfo) throws Exception {
        throw new RuntimeException("um impl.");
    }

    @Override
    public boolean filter(BlockTxInfo txInfo) throws Exception {
        return true;
//        BcMemo memo = txInfo.getMemosObj();
    }
}
