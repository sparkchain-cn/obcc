package cn.obcc.driver.contract.solc.vo;

import cn.obcc.driver.vo.ContractBin;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName SolcVo
 * @desc TODO
 * @date 2019/8/26 0026  11:35
 **/
public class ContractInfo {

    private String source;
    private String compileResult;
    private String exception;

    private Map<String, ContractBin> map = new HashMap<>();


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getCompileResult() {
        return compileResult;
    }

    public void setCompileResult(String compileResult) {
        this.compileResult = compileResult;
    }


    public Map<String, ContractBin> getMap() {
        return map;
    }

    public void setMap(Map<String, ContractBin> map) {
        this.map = map;
    }
}
