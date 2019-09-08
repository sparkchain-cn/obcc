package cn.obcc.driver.vo;


import cn.obcc.config.ExProps;
import cn.obcc.driver.module.fn.IUpchainFn;
import cn.obcc.exception.enums.EChainTxType;

import java.util.Arrays;

/**
 * @author mgicode
 * @version 1.0
 * @ClassName ChainPipe
 * @desc
 * @data 2019/9/5 17:57
 **/
public class ChainPipe {

    private String chainCode;
    private EChainTxType chainTxType = EChainTxType.Orign;
    private String bizId;
    private SrcAccount srcAccount;

    private String destAddr;
    private String contractAddr;
    private String amount;

    private ExProps config=new ExProps();

    private IUpchainFn callbackFn;

    private String method;
    private Object[] params;


    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public SrcAccount getSrcAccount() {
        return srcAccount;
    }

    public void setSrcAccount(SrcAccount srcAccount) {
        this.srcAccount = srcAccount;
    }

    public String getDestAddr() {
        return destAddr;
    }

    public void setDestAddr(String destAddr) {
        this.destAddr = destAddr;
    }

    public String getContractAddr() {
        return contractAddr;
    }

    public void setContractAddr(String contractAddr) {
        this.contractAddr = contractAddr;
    }

    public String getChainCode() {
        return chainCode;
    }

    public void setChainCode(String chainCode) {
        this.chainCode = chainCode;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }


    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public ExProps getConfig() {
        return config;
    }

    public void setConfig(ExProps config) {
        this.config = config;
    }

    public IUpchainFn getCallbackFn() {
        return callbackFn;
    }

    public void setCallbackFn(IUpchainFn callbackFn) {
        this.callbackFn = callbackFn;
    }

    public EChainTxType getChainTxType() {
        return chainTxType;
    }

    public void setChainTxType(EChainTxType chainTxType) {
        this.chainTxType = chainTxType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }


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
        if (params != null)
            pipe.setParams(Arrays.copyOf(params, params.length));

        return pipe;
    }
}
