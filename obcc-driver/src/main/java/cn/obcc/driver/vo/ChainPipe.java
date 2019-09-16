package cn.obcc.driver.vo;


import cn.obcc.config.ExProps;
import cn.obcc.driver.module.fn.IUpchainFn;
import cn.obcc.exception.enums.EChainTxType;
import cn.obcc.exception.enums.ETransferStatus;
import cn.obcc.exception.enums.EUpchainType;
import lombok.Data;

import java.util.Arrays;

/**
 * @author mgicode
 * @version 1.0
 * @ClassName ChainPipe
 * @desc
 * @data 2019/9/5 17:57
 **/
@Data
public class ChainPipe {

    private String chainCode;
    private EChainTxType chainTxType = EChainTxType.Orign;
    private String bizId;
    private SrcAccount srcAccount;

    private String destAddr;
    private String contractAddr;
    private String amount;

    private ExProps config = new ExProps();

    private IUpchainFn callbackFn = new IUpchainFn() {
        @Override
        public void exec(String bizId, String hash, EUpchainType upchainType, ETransferStatus state, Object resp) throws Exception {

        }
    };

    private String method;
    private Object[] params;


    public ChainPipe copy() {

        ChainPipe pipe = new ChainPipe();
        pipe.setChainCode(this.getChainCode());
        pipe.setChainTxType(this.getChainTxType());
        pipe.setBizId(this.getBizId());
        pipe.setSrcAccount(new SrcAccount() {{
            SrcAccount now = ChainPipe.this.getSrcAccount();
            setSrcAddr(now.getSrcAddr());
            setSecret(now.getSecret());
            setNonce(now.getNonce());
            setMemos(now.getMemos());
            setGasLimit(now.getGasLimit());
            setGasPrice(now.getGasPrice());
        }});
        pipe.setDestAddr(destAddr);
        pipe.setContractAddr(contractAddr);
        pipe.setAmount(amount);
        pipe.setConfig(config); //没变
        pipe.setCallbackFn(callbackFn); //没变
        pipe.setMethod(method);
        if (params != null) {
            pipe.setParams(Arrays.copyOf(params, params.length));
        }

        return pipe;
    }
}
