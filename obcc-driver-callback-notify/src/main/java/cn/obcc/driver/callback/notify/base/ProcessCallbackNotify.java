package cn.obcc.driver.callback.notify.base;

import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.callback.notify.ICallBackNotify;
import cn.obcc.driver.ICallbackRegister;
import cn.obcc.exception.enums.ETransferState;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.vo.BizState;
import cn.obcc.vo.driver.BlockTxInfo;
import com.alibaba.fastjson.JSON;
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

    protected BizState getBizState(String hash, BlockTxInfo txInfo) throws Exception {
        BizState bizState = getDriver().getStateMonitor().getHashState(hash);
        if (bizState != null) {
            return bizState;
        }
        String bizId = null;
        if (txInfo != null && txInfo.getMemosObj() != null) {
            bizId = txInfo.getMemosObj().getBizId();
        }
        if (bizId != null) {
            return getDriver().getStateMonitor().getBizState(bizId);
        }
        return null;
    }

    @Override
    public void notify(BlockTxInfo txInfo) throws Exception {
        String hash = txInfo.getHash();
        //1、查找BizState
        BizState bizState = getBizState(hash, txInfo);
        logger.debug(JSON.toJSONString(bizState));
        //todo:
        if (bizState == null) {
            logger.error("区块返回流水时，hash:{}没有(在状态服务中)找到bizId,可能时间过长，状态服务清除或其它错误。", hash);
            return;
        }
        //2、调整BizState状诚，共识回来的，在状态服务中加上1
        bizState.incConsensusSize();
        if (bizState.getConsensusSize() == bizState.getTxSize()) {
            bizState.setTransferState(ETransferState.STATE_CHAIN_CONSENSUS);
        } else {
            //这个状态会变化，一边上链，一边区块回来，有可能会在35和75中跳动
            bizState.setTransferState(ETransferState.STATE_CHAIN_HALF_CONSENSUS);
        }
        //3、状态回调通知
        try {
            getDriver().getCallbackRegister().getStateListener(bizState.getBizId()).exec(bizState, txInfo);
        } catch (Exception e) {
            logger.error("", e);
        }
        //4、状态服务中除去当前hash的关联
        getDriver().getStateMonitor().delHashState(txInfo.getHash());
        logger.debug("remove hahs {} and BizState relation", hash);

        //5、所有交易都回来，那么除去bizId和BizState的关联
        if (bizState.getConsensusSize() == bizState.getTxSize()) {
            getDriver().getStateMonitor().delBizState(bizState.getBizId());
            logger.debug("remove bizId {} and BizState relation", bizState.getBizId());
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
