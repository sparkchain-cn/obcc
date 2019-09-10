package cn.obcc.vo.driver;

import cn.obcc.vo.Entity;
import com.alibaba.fastjson.JSON;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Map;

@Table(name = "contract_info")
public class ContractInfo extends Entity {

    private String bizId; //unique

    private String name;

    private String content;
    private String abi;
    private String bin;
    //{"transfer":"0x2323233","burn":"03de3scdds"}
    private String methodNameIdMapStr;

    private String hash;
    private String contractAddr;

    private int state;

    private String compileResult;
    private String compileException;

    @Column(name = "biz_id")
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

    public String getAbi() {
        return abi;
    }

    public void setAbi(String abi) {
        this.abi = abi;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Column(name = "contract_addr")
    public String getContractAddr() {
        return contractAddr;
    }

    public void setContractAddr(String contractAddr) {
        this.contractAddr = contractAddr;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Column(name = "compile_result")
    public String getCompileResult() {
        return compileResult;
    }

    public void setCompileResult(String compileResult) {
        this.compileResult = compileResult;
    }

    @Column(name = "compile_exception")
    public String getCompileException() {
        return compileException;
    }

    public void setCompileException(String compileException) {
        this.compileException = compileException;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethodNameIdMapStr() {
        return methodNameIdMapStr;
    }

    public void setMethodNameIdMapStr(String methodNameIdMapStr) {
        this.methodNameIdMapStr = methodNameIdMapStr;
    }

    @Transient
    public Map<String, String> getMethodIdNameMap() {
        return (Map<String, String> )JSON.parse(getMethodNameIdMapStr());
    }
}
