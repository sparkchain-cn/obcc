package cn.obcc.driver.callback.notify.base;

import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.callback.notify.ICallBackNotify;
import cn.obcc.driver.tech.register.ICallbackRegister;
import cn.obcc.exception.enums.ETransferStatus;
import cn.obcc.exception.enums.EUpchainType;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.vo.BcMemo;
import cn.obcc.vo.driver.BlockTxInfo;

/**
 * @author mgicode
 * @version 1.0
 * @ClassName ProcessCallbackNotify
 * @desc
 * @data 2019/9/3 14:47
 **/
public class ProcessCallbackNotify<T> extends BaseHandler<T> implements ICallBackNotify<T> {

    public void notify(BlockTxInfo txInfo) throws Exception {
        String bizId = txInfo.getMemosObj().getBizId();
        String hash = txInfo.getHash();
        ICallbackRegister register = getDriver().getCallbackRegister();
        register.getCallbackFn(txInfo.getMemosObj().getBizId())
                .exec(bizId, hash, EUpchainType.BlockBack, ETransferStatus.STATE_CHAIN_CONSENSUS, txInfo);
      //状态服务器中清除
        getDriver().getStateMonitor().delState(txInfo.getHash());
        register.unRegister(bizId);
    }

    public boolean filter(BlockTxInfo txInfo) throws Exception {
        BcMemo memo = txInfo.getMemosObj();
        return memo != null
                && !StringUtils.isNullOrEmpty(memo.getBizId())
                && getDriver().getCallbackRegister().exist(memo.getBizId());
    }
}
