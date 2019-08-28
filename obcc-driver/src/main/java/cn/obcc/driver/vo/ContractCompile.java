package cn.obcc.driver.vo;

import java.util.List;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName ContractCompile
 * @desc TODO
 * @date 2019/8/28 0028  9:42
 **/
public class ContractCompile {

    private String bizId; //unique

    private String content;

    private List<ContractBin> contractBinList;

    private int state;//成功或失败

    private String compileResult;
    private String compileException;

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<ContractBin> getContractBinList() {
        return contractBinList;
    }

    public void setContractBinList(List<ContractBin> contractBinList) {
        this.contractBinList = contractBinList;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCompileResult() {
        return compileResult;
    }

    public void setCompileResult(String compileResult) {
        this.compileResult = compileResult;
    }

    public String getCompileException() {
        return compileException;
    }

    public void setCompileException(String compileException) {
        this.compileException = compileException;
    }
}
