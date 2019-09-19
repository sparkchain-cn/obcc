package cn.obcc.contract.solc;

import cn.obcc.contract.solc.utils.SolCompiler;
import cn.obcc.def.contract.IContractParseStragegy;
import cn.obcc.driver.IChainDriver;
import cn.obcc.stragegy.BaseStrategy;
import cn.obcc.vo.contract.CompileVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName JsonStragegy
 * @desc TODO
 * @date 2019/8/27 0027  9:15
 **/
public class ContractSolStragegy extends BaseStrategy<IChainDriver> implements IContractParseStragegy<IChainDriver> {
    public static Logger logger = LoggerFactory.getLogger(ContractSolStragegy.class);

    @Override
    public CompileVo compile(String sourceCode, String solcPath, String tempPath, Boolean deleteTemp) throws Exception {
        return SolCompiler.compile(sourceCode, solcPath, tempPath, deleteTemp);
    }
}
