package cn.obcc.driver.contract;

import cn.obcc.config.ExConfig;
import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.contract.compile.ContractCompiler;
import cn.obcc.driver.contract.core.CommContractExecutor;
import cn.obcc.driver.contract.core.DeployExecutor;
import cn.obcc.driver.module.IContractHandler;
import cn.obcc.listener.IStateListener;
import cn.obcc.driver.vo.CompileResult;
import cn.obcc.driver.vo.FromAccount;
import cn.obcc.exception.ObccException;
import cn.obcc.enums.EExceptionCode;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.vo.driver.ContractInfo;
import com.alibaba.fastjson.JSON;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName NonceCu
 * @desc TODO
 * @date 2019/8/26 0026  8:36
 **/
public abstract class BaseContractHandler<T> extends BaseHandler<T> implements IContractHandler<T> {

    public static final Logger logger = LoggerFactory.getLogger(BaseContractHandler.class);

    /**
     * 构建 abi 中函数的名称与其生成的对应的methodId，形成Map
     *
     * @param abi
     * @return
     */
    protected abstract Map<String, String> buildMethodIdNameMap(String abi);

    /**
     * 根据abi构建部署合约的的构建方法的字符串
     *
     * @param abi
     * @return
     * @throws Exception
     */
    public abstract String encodeConstructor(String abi, List<Object> params) throws Exception;

    /**
     * 生成部署合约生成hex数据
     *
     * @param bin
     * @param abi
     * @param params
     * @return
     * @throws Exception
     */
    public String getDeployHexData(String bin, String abi, List<Object> params) throws Exception {
        return bin + encodeConstructor(abi, params);
    }

    /**
     * 生成调用合约生成hex数据
     *
     * @param abi
     * @param fnName
     * @param inputValues
     * @return
     * @throws Exception
     */
    public abstract String getInvokeHexData(String abi, String fnName, List<Object> inputValues) throws Exception;


    @Override
    public CompileResult compile(String pkgName, String contract, ExConfig config) throws Exception {
        CompileResult contractCompile = ContractCompiler.compile(contract, getConfig());
        contractCompile.setPkgName(pkgName);
        if (contractCompile.getState() == -1) {
            return contractCompile;
        }
        //每个合约保存
        contractCompile.getContractBinList().stream().forEach((contractBin) -> {
            String mapStr = JSON.toJSONString(buildMethodIdNameMap(contractBin.getAbi()));
            CommContractExecutor.saveContract(driver, pkgName, contract, contractBin, mapStr);
        });
        return contractCompile;
    }


    @Override
    public ContractInfo getContract(@NotEmpty String pkgName, @NotEmpty String contractName) throws Exception {
        return getDriver().getLocalDb().getContractInfoDao()
                .get(" pkg_name=? && name= ? ", new Object[]{pkgName, contractName});
    }

    @Override
    public ContractInfo getContract(@NonNull String contractAddr) throws Exception {
        return getDriver().getLocalDb().getContractInfoDao().getContractByAddr(contractAddr);
    }


    @Override
    public Boolean addContract(@NonNull ContractInfo info) throws Exception {
        if (StringUtils.isNullOrEmpty(info.getPkgName())
                || StringUtils.isNullOrEmpty(info.getAbi())
                || StringUtils.isNullOrEmpty(info.getName())
                || StringUtils.isNullOrEmpty(info.getContractAddr())) {
            logger.error("pkgName,name,abi,contractAddr 不能为空.");
            return false;
        }
        getDriver().getLocalDb().getContractInfoDao().add(info);
        return true;
    }


    @Override
    public String deployNoArgs(@NonNull String pkgName, @NonNull String contract,
                               @NonNull String bizId, @NonNull FromAccount fromAccount,
                               IStateListener stateListener, ExConfig exConfig) throws Exception {
        return deployNoArgs(pkgName, null, contract, bizId, fromAccount, stateListener, exConfig);
    }

    @Override
    public String deployNoArgs(@NonNull String pkgName, String contractName, @NonNull String contract,
                               @NonNull String bizId, @NonNull FromAccount fromAccount, IStateListener stateListener, ExConfig exConfig) throws Exception {
        return deploy(pkgName, null, contract, bizId, fromAccount, null, stateListener, exConfig);
    }

    @Override
    public String deploy(@NonNull String pkgName, @NonNull String contract,
                         @NonNull String bizId, @NonNull FromAccount fromAccount, List<Object> params,
                         IStateListener stateListener, ExConfig exConfig) throws Exception {
        return deploy(pkgName, null, contract, bizId, fromAccount, params, stateListener, exConfig);
    }


    @Override
    public String deploy(@NonNull String pkgName, String contractName, @NonNull String contract,
                         @NonNull String bizId, @NonNull FromAccount fromAccount, List<Object> params,
                         IStateListener stateListener, ExConfig exConfig) throws Exception {

        CompileResult result = compile(pkgName, contract, exConfig);
        if (result == null) {
            logger.error("compile contract error,return null. pkgName:{},contractName:{},contract:{}",
                    pkgName, contractName, contract);
            return null;
        }
        if (StringUtils.isNullOrEmpty(contractName)) {
            contractName = CommContractExecutor.computeContractName(result);
        }
        ContractInfo ci = getContract(pkgName, contractName);
        if (ci == null) {
            logger.error("compile contract but can not find it,may contractName not according to the contract Name." +
                    " pkgName:{},contractName:{},contract:{}", pkgName, contractName, contract);
            return null;
        }
        return deploy(bizId, fromAccount, ci, params, stateListener, exConfig);
    }


    @Override
    public String deploy(@NonNull String bizId, @NonNull FromAccount fromAccount, @NonNull ContractInfo ci, List<Object> params,
                         IStateListener stateListener, @NonNull ExConfig exConfig) throws Exception {

//        RecordInfo recordInfo = initOrGetRecordInfo(exProps);
//        recordInfo.setFuncName("constructor");
//        recordInfo.setFuncParams(JSON.toJSONString(params));

        //hex memo
        String hex = getDeployHexData(ci.getBin(), ci.getAbi(), params);
        fromAccount.setMemos(hex);

        return DeployExecutor.deploy(driver, bizId, fromAccount, ci, params, stateListener, exConfig);

    }

    @Override
    public String invoke(@NonNull String bizId, @NonNull ContractInfo contractInfo, @NonNull FromAccount fromAccount,
                         @NonNull String fnName, List<Object> params, IStateListener stateListener, @NonNull ExConfig exConfig) throws Exception {

//        RecordInfo recordInfo = initOrGetRecordInfo(exProps);
//        recordInfo.setFuncName(fnName);
//        recordInfo.setFuncParams(JSON.toJSONString(params));

        String hex = getInvokeHexData(contractInfo.getAbi(), fnName, params);
        fromAccount.setMemos(hex);

        return DeployExecutor.invoke(driver, bizId, contractInfo.getContractAddr(), fromAccount, stateListener, exConfig);

    }


    @Override
    public String invoke(@NonNull String bizId, @NonNull String abi, @NonNull String contractAddr, @NonNull FromAccount fromAccount,
                         @NonNull String fnName, List<Object> params, IStateListener stateListener, @NonNull ExConfig exConfig) throws Exception {

//        RecordInfo recordInfo = initOrGetRecordInfo(exProps);
//        recordInfo.setFuncName(fnName);
//        recordInfo.setFuncParams(JSON.toJSONString(params));
        String hex = getInvokeHexData(abi, fnName, params);
        fromAccount.setMemos(hex);

        return DeployExecutor.invoke(driver, bizId, contractAddr, fromAccount, stateListener, exConfig);

    }


    @Override
    public String query(String srcAddr, ContractInfo ci, String funcName, List<Object> params, ExConfig exConfig) throws Exception {

        if (StringUtils.isNullOrEmpty(ci.getContractAddr())) {
            throw ObccException.create(EExceptionCode.PARAMETER_INVALID,
                    "invoke contract view function,the contractAddr must be not empty.");
        }
        if (StringUtils.isNullOrEmpty(ci.getAbi())) {
            throw ObccException.create(EExceptionCode.PARAMETER_INVALID,
                    "invoke contract view function,the abi must be not empty.");
        }

        return query(srcAddr, ci.getAbi(), ci.getContractAddr(), funcName, params, exConfig);
    }

    @Override
    public String queryWithAddr(String srcAddr, String contractAddr, String funcName, List<Object> params, ExConfig exConfig) throws Exception {
        ContractInfo ci = getContract(contractAddr);
        if (ci == null) {
            logger.error("根据contractAddr:{} 从数据库中找不到对应的记录。", contractAddr);
            return null;
        }
        return query(srcAddr, ci, funcName, params, exConfig);
    }


    @Override
    public String queryWithName(@NonNull String srcAddr, @NonNull String pkgName, @NonNull String contractName,
                                @NonNull String funcName, List<Object> params, ExConfig exConfig) throws Exception {
        ContractInfo ci = getContract(pkgName, contractName);
        if (ci == null) {
            logger.error("根据pkgName:{} 和contractName:{} 从数据库中找不到对应的记录。", pkgName, contractName);
            return null;
        }
        return query(srcAddr, ci, funcName, params, exConfig);
    }

//    private RecordInfo initOrGetRecordInfo(@NonNull ExProps exProps) {
//        if (exProps.getRecordInfo() == null) {
//            exProps.setRecordInfo(new RecordInfo());
//        }
//        return exProps.getRecordInfo();
//    }


}
