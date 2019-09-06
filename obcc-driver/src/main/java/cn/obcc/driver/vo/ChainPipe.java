package cn.obcc.driver.vo;


import cn.obcc.config.ExProps;
import cn.obcc.driver.module.fn.IUpchainFn;
import cn.obcc.exception.enums.EChainTxType;

/**
 * @author mgicode
 * @version 1.0
 * @ClassName ChainPipe
 * @desc
 * @data 2019/9/5 17:57
 **/
public class ChainPipe {

    private String chainCode;
    private EChainTxType txType;
    private String bizId;
    private SrcAccount account;

    private String destAddr;
    private String contractAddr;
    private String amount;

    private ExProps config;

    private IUpchainFn fn;

    private String method;
    private Object[] params;


    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public SrcAccount getAccount() {
        return account;
    }

    public void setAccount(SrcAccount account) {
        this.account = account;
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

    public IUpchainFn getFn() {
        return fn;
    }

    public void setFn(IUpchainFn fn) {
        this.fn = fn;
    }

    public EChainTxType getTxType() {
        return txType;
    }

    public void setTxType(EChainTxType txType) {
        this.txType = txType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
