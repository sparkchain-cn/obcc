package cn.obcc.driver.contract;

import cn.obcc.config.ReqConfig;
import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.module.IContractHandler;
import cn.obcc.driver.module.fn.IContractCompileFn;
import cn.obcc.driver.module.fn.IContractDeployFn;
import cn.obcc.driver.module.fn.IContractInvokeFn;
import cn.obcc.driver.vo.ContractBin;
import cn.obcc.driver.vo.ContractCompile;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.exception.enums.EExceptionCode;
import cn.obcc.vo.RetData;
import cn.obcc.vo.driver.ContractInfo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        ContractCompile contractCompile = ContractCompiler.compile(contract, getObccConfig());
        contractCompile.setBizId(bizId);
        //getDriver().getLocalDb().getContractInfoDao().
        return contractCompile.getState() == -1 ?
                RetData.error(EExceptionCode.CONTRACT_COMPILE_ERROR, contractCompile)
                : RetData.succuess(contractCompile);
    }

    @Override
    public void compile(@NotEmpty String bizId, @NotEmpty String contract, IContractCompileFn fn, ReqConfig<T> config) throws Exception {
        new Thread(() -> {
            ContractCompile contractCompile = ContractCompiler.compile(contract, getObccConfig());
            contractCompile.setBizId(bizId);
            if (fn != null) {
                fn.exec(bizId, null, contractCompile);
            }
        }).start();
    }

    @Override
    public ContractInfo getContract(@NotEmpty String bizId, @NotEmpty String contractName) throws Exception {
        return getDriver()
                .getLocalDb()
                .getContractInfoDao()
                .findOne(" biz_id=? && name=? ",
                        new Object[]{bizId, contractName});
    }


    @Override
    public ContractInfo getContract(@NotEmpty String contractAddr) throws Exception {
        return getDriver()
                .getLocalDb()
                .getContractInfoDao()
                .findOne(" contractAddr = ? ",
                        new Object[]{contractAddr});
    }


    @Override
    public Boolean addContract(@NotEmpty String bizId, @NotNull ContractInfo info) throws Exception {
        info.setBin(bizId);
        getDriver()
                .getLocalDb()
                .getContractInfoDao()
                .add(info);
        return true;
    }

    @Override
    public RetData<String> deploy(@NotEmpty String bizId, @NotNull SrcAccount srcAccount, @NotNull ContractInfo contract,
                                  IContractDeployFn fn, ReqConfig<T> config) throws Exception {
        new Thread(() -> {
            ContractInfo contractInfo = null;
            try {
                contractInfo = getContract(contract.getBizId(), contract.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            contractInfo.setBizId(bizId);
            onDeploy(srcAccount, contractInfo, fn, config);

        }).start();

        return RetData.succuess(config.getSparkHash());
    }

    //少一个name
    @Override
    public RetData<String> deploy(@NotEmpty String bizId,
                                  @NotNull SrcAccount srcAccount, @NotEmpty String contract, String name,
                                  IContractDeployFn fn, ReqConfig<T> config) throws Exception {

        new Thread(() -> {
            //1.compiler contract
            RetData<ContractCompile> compile = null;
            try {
                compile = compile(bizId, contract, config);

                if (!compile.isSuccess()) {
                    RetData<ContractCompile> finalCompile = compile;
//                    fn.exec(bizId, null, -1, new HashMap<String, String>() {{
//                        put("message", finalCompile.getMessage());
//                        put("code", finalCompile.getCode());
//                    }});
                    return;
                }
                //取出编译之后的合约
                ContractCompile data = (ContractCompile) compile.getData();
                List<ContractBin> collect =
                        data.getContractBinList()
                                .stream()
                                .filter(i -> i.getName().equals(name))
                                .collect(Collectors.toList());

                if (collect.size() == 0) {
//                    fn.exec(bizId, null, -1, new HashMap<String, String>() {{
//                        put("message", String.format("找不到合约名为:%s 的合约", name));
//                        put("code", "1001");
//                    }});
                    return;
                }
                ContractBin bin = collect.get(0);
                ContractInfo info = new ContractInfo();
                info.setBizId(bizId);
                info.setAbi(bin.getAbi());
                info.setBin(bin.getBinary());
                info.setName(bin.getName());
                //发布
                onDeploy(srcAccount, info, fn, config);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        return RetData.succuess(config.getSparkHash());
    }

    @Override
    public RetData<Object> query(ContractInfo contractInfo, ReqConfig<T> config,
                                 String methodName, Object... params) throws Exception {

        ContractInfo contract = getContract(contractInfo.getBizId(), contractInfo.getName());
        //根据方法验证参数
        Map<String, String> function = ContractCompiler.getFunction(methodName, contract.getAbi());
        if (function.size() != params.length) {
            return RetData.error(EExceptionCode.PARAMETER_INVALID, null);
        }
        //TODO 比较参数 类型
        return RetData.succuess();
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


    protected abstract void onDeploy(SrcAccount srcAccount, ContractInfo contractInfo, IContractDeployFn fn, ReqConfig<T> config);

}
