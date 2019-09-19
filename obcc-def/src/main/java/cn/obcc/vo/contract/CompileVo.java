package cn.obcc.vo.contract;

import cn.obcc.vo.driver.ContractBin;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName SolcVo
 * @desc TODO
 * @date 2019/8/26 0026  11:35
 **/
@Data
public class CompileVo {

    private String source;
    private String compileResult;
    private String exception;

    private Map<String, ContractBin> map = new HashMap<>();

}
