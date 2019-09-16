package cn.obcc.driver.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName ContractCompile
 * @desc TODO
 * @date 2019/8/28 0028  9:42
 **/

@Data
public class CompileResult {

    private String bizId; //unique

    private String content;

    private List<ContractBin> contractBinList = new ArrayList<>();

    private int state;//成功或失败

    private String compileResult;
    private String compileException;


}
