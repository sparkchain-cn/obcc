package cn.obcc.driver.contract;

import cn.obcc.config.ReqConfig;
import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.contract.solc.core.SolcCompiler;
import cn.obcc.driver.module.IContractHandler;
import cn.obcc.driver.module.fn.IContractCompileFn;
import cn.obcc.driver.module.fn.IContractDeployFn;
import cn.obcc.driver.module.fn.IContractInvokeFn;
import cn.obcc.driver.vo.ContractCompile;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.exception.enums.EContractType;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.vo.RetData;
import cn.obcc.vo.driver.ContractInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName NonceCu
 * @desc TODO
 * @date 2019/8/26 0026  8:36
 **/
public abstract class ContractHandler<T> extends BaseHandler<T> implements IContractHandler<T> {

    @Override
    public RetData<ContractCompile> compile(String bizId, String contract, ReqConfig<T> config) throws Exception {
        if (getObccConfig().getContractType() == EContractType.SOLC) {

            cn.obcc.driver.contract.solc.vo.ContractInfo ci =
                    SolcCompiler.compile(contract, getObccConfig().getSolcPath(), getObccConfig().getTempPath(), true);
            ContractInfo contractInfo = new ContractInfo();
            if (StringUtils.isNullOrEmpty(ci.getException())) {
                contractInfo.setBizId(bizId);
                contractInfo.setCompileException(ci.getException());
                contractInfo.setCompileResult(ci.getCompileResult());
                return RetData.error("201", "ddd", contractInfo);
            }
        }
        return null;
    }

    @Override
    public void compile(String bizId, String contract, IContractCompileFn fn, ReqConfig<T> config) throws Exception {

    }

    @Override
    public ContractInfo getContract(String bizId, String contractName) throws Exception {
        return null;
    }

    @Override
    public RetData<Boolean> addContract(String bizId, ContractInfo params) throws Exception {
        return null;
    }

    @Override
    public RetData<String> deploy(String bizId, SrcAccount srcAccount, ContractInfo contract,
                                  IContractDeployFn fn, ReqConfig<T> config) throws Exception {
        return null;
    }

    @Override
    public RetData<String> deploy(String bizId, SrcAccount srcAccount, String contract,
                                  IContractDeployFn fn, ReqConfig<T> config) throws Exception {
        return null;
    }

    @Override
    public RetData<Object> query(ContractInfo contractInfo, ReqConfig<T> config,
                                 String methodName, Object... params) throws Exception {
        return null;
    }

    @Override
    public RetData<String> invoke(String bizId, ContractInfo contractInfo,
                                  SrcAccount srcAccount, ReqConfig<T> config, IContractInvokeFn fn, Object... params) throws Exception {

        //4、adjuster
        Map map = new HashMap<String, Object>() {
            {
                put("bizId", bizId);
                put("account", srcAccount);
                put("callback", fn);
                put("type", "contractInvoke");
                put("params", params);
                put("contractInfo", contractInfo);
            }
        };
        getDriver().getSpeedAdjuster().offer(map);
        return RetData.succuess(map.hashCode());

    }
}
