package cn.obcc.driver.contract.compile;

import cn.obcc.config.ObccConfig;
import cn.obcc.driver.contract.solc.core.AbiParser;
import cn.obcc.driver.contract.solc.core.SolcCompiler;
import cn.obcc.driver.vo.CompileResult;
import cn.obcc.exception.ObccException;
import cn.obcc.exception.enums.EContractType;
import cn.obcc.exception.enums.EExceptionCode;
import cn.obcc.utils.base.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * ContractCompiler
 *
 * @author ecasona
 * @version 1.4
 * @date 2019/8/28 9:03
 * @details 合约编译器
 */
public class ContractCompiler {
    public static final Logger logger = LoggerFactory.getLogger(ContractCompiler.class);




    public static CompileResult compile(String contract, ObccConfig config) throws Exception {
        if (config.getContractType() == EContractType.SOLC) {
            return solcCompile(contract, config);
        }
        logger.error("合约类型{}不支持", config.getContractType());
        throw ObccException.create(EExceptionCode.CONTRACT_TYPE_NO_SUPPORT, "合约类型不支持");
        //return null;
    }

    /**
     * solidaity 编译
     *
     * @param contract
     * @param config
     * @return
     */
    public static CompileResult solcCompile(String contract, ObccConfig config) {
        CompileResult contractInfo = new CompileResult();
        cn.obcc.driver.contract.solc.vo.ContractInfo info;
        info = SolcCompiler.compile(contract, config.getSolcPath(), config.getTempPath(), true);
        if (StringUtils.isNotNullOrEmpty(info.getException())) {
            contractInfo.setCompileException(info.getException());
            contractInfo.setCompileResult(info.getCompileResult());
            contractInfo.setState(-1);
            return contractInfo;
        }
        contractInfo.setState(1);
        info.getMap().forEach((key, value) -> contractInfo.getContractBinList().add(value));
        return contractInfo;
    }

    public static Map<String, String> getFunction(String methodName, String abi) {
        return AbiParser.getFunctionInputs(abi, methodName);

    }
}