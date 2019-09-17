package cn.obcc.driver.callback.notify.base;

import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.callback.notify.ICallBackNotify;
import cn.obcc.driver.ICallbackRegister;
import cn.obcc.driver.module.base.BaseTokenHandler;
import cn.obcc.exception.enums.ETransferStatus;
import cn.obcc.exception.enums.EUpchainType;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.vo.BcMemo;
import cn.obcc.vo.BizState;
import cn.obcc.vo.driver.BlockTxInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mgicode
 * @version 1.0
 * @ClassName ProcessCallbackNotify
 * @desc
 * @data 2019/9/3 14:47
 **/
public class ProcessCallbackNotify<T> extends BaseHandler<T> implements ICallBackNotify<T> {
    public static final Logger logger = LoggerFactory.getLogger(ProcessCallbackNotify.class);

    @Override
    public void notify(BlockTxInfo txInfo) throws Exception {
        String hash = txInfo.getHash();
        //合约的执行没有bizId
        String bizId = null;
        BizState bizState = getDriver().getStateMonitor().getHashState(hash);
        if (txInfo != null && txInfo.getMemosObj() != null) {
            bizId = txInfo.getMemosObj().getBizId();
        }
        if (bizId == null) {
            bizId = bizState.getBizId();
        }

        txInfo.setBizId(bizId);
        txInfo.setRecId(bizState.getRecId());

        if (StringUtils.isNullOrEmpty(bizId)) {
            logger.error("区块返回流水时，hash:{}没有(在状态服务中)找到bizId,可能时间过长，状态服务清除或其它错误。", hash);
        }

        ICallbackRegister register = getDriver().getCallbackRegister();
        register.getCallbackFn(txInfo.getMemosObj().getBizId())
                .exec(bizId, hash, EUpchainType.BlockBack, ETransferStatus.STATE_CHAIN_CONSENSUS, txInfo);
        //状态服务器中清除
        getDriver().getStateMonitor().delHashState(txInfo.getHash());
        //一个合约执行，一次只有一条交易
        if (txInfo.isContract() || txInfo.isLast()) {
            register.unRegister(bizId);
        }
    }

    @Override
    public boolean filter(BlockTxInfo txInfo) throws Exception {
        return true;
//        BcMemo memo = txInfo.getMemosObj();
//        return memo != null
//                && !StringUtils.isNullOrEmpty(memo.getBizId())
//                && getDriver().getCallbackRegister().exist(memo.getBizId());
    }
}
