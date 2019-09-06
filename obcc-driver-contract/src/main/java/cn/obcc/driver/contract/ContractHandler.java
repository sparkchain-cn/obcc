package cn.obcc.driver.contract;

import cn.obcc.config.ExProps;
import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.module.IContractHandler;
import cn.obcc.driver.module.fn.IUpchainFn;
import cn.obcc.driver.vo.CompileResult;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.exception.enums.EExceptionCode;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.vo.RetData;
import cn.obcc.vo.driver.BlockTxInfo;
import cn.obcc.vo.driver.ContractInfo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
    public CompileResult doCompile(String bizId, String contract, ExProps config) throws Exception {
        CompileResult contractCompile = ContractCompiler.compile(contract, getObccConfig());
        contractCompile.setBizId(bizId);
        //getDriver().getLocalDb().getContractInfoDao().
        return contractCompile;
    }

    @Override
    public void compile(@NotEmpty String bizId, @NotEmpty String contract, IUpchainFn<CompileResult> fn, ExProps config) throws Exception {
        new Thread(() -> {
            CompileResult contractCompile = ContractCompiler.compile(contract, getObccConfig());
            contractCompile.setBizId(bizId);
            if (fn != null) {
                // fn.exec(bizId, null, contractCompile);
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
        if(StringUtils.isNullOrEmpty(contractAddr)) return null;
        return getDriver()
                .getLocalDb()
                .getContractInfoDao()
                .findOne(" contract_addr = ? ",
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
    public String deploy(@NotEmpty String bizId, @NotNull SrcAccount srcAccount, @NotNull ContractInfo contract,
                                  IUpchainFn<BlockTxInfo> fn, ExProps config) throws Exception {
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

        return config.getSparkHash();
    }


    @Override
    public RetData<Object> query(ContractInfo contractInfo, ExProps config,
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
    public String invoke(String bizId, ContractInfo contractInfo,
                                  SrcAccount srcAccount, ExProps config, IUpchainFn<BlockTxInfo> fn, Object... params) throws Exception {

//        Map map = new HashMap<String, Object>() {
//            {
//                put("bizId", bizId);
//                put("account", srcAccount);
//                put("callback", fn);
//                put("type", "contractInvoke");
//                put("params", params);
//                put("contractInfo", contractInfo);
//            }
//        };
//        getDriver().getSpeedAdjuster().offer(map);
//        return RetData.succuess(map.hashCode());
        return null;
    }


    protected abstract void onDeploy(SrcAccount srcAccount, ContractInfo contractInfo, IUpchainFn<BlockTxInfo> fn, ExProps config);

}
