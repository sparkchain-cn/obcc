package cn.obcc.driver.tech.base.excutor;

import cn.obcc.driver.IChainDriver;
import cn.obcc.vo.db.ContractInfo;
import cn.obcc.vo.driver.CompileResult;
import cn.obcc.vo.driver.ContractBin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName CommContractExector
 * @desc TODO
 * @date 2019/9/18 0018  11:43
 **/
public class ContractExecutor {
    public static final Logger logger = LoggerFactory.getLogger(ContractExecutor.class);

    public static void saveContract(IChainDriver driver, String pkgName, String contract, ContractBin contractBin, String methodIdName) {
        ContractInfo contractInfo = new ContractInfo();
        contractInfo.setPkgName(pkgName);
        contractInfo.setName(contractBin.getName());
        contractInfo.setAbi(contractBin.getAbi());
        contractInfo.setBin(contractBin.getBinary());
        contractInfo.setContent(contract);
        //解析时查询
        // contractInfo.setMethodIdNameMapStr(JSON.toJSONString(buildMethodIdNameMap(contractInfo)));
        contractInfo.setMethodIdNameMapStr(methodIdName);
        try {
            driver.getLocalDb().getContractInfoDao().add(contractInfo);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("compile save error.", e);
        }
    }

    public static String computeContractName(CompileResult result) {
        if (result.getContractBinList() == null || result.getContractBinList().size() == 0) {
            return null;
        }
        int len = 0;
        String name = null;
        for (ContractBin c : result.getContractBinList()) {
            if (c.getBinary().length() >= len) {
                len = c.getBinary().length();
                name = c.getName();
            }
        }

        return name;
    }
}