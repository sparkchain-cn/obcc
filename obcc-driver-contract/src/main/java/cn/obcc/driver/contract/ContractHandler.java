package cn.obcc.driver.contract;

import cn.obcc.config.ExProps;
import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.contract.compile.ContractCompiler;
import cn.obcc.driver.module.IContractHandler;
import cn.obcc.driver.module.fn.IUpchainFn;
import cn.obcc.driver.vo.ChainPipe;
import cn.obcc.driver.vo.CompileResult;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.exception.enums.EChainTxType;
import cn.obcc.exception.enums.EExceptionCode;
import cn.obcc.exception.enums.ETransferStatus;
import cn.obcc.exception.enums.EUpchainType;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.uuid.UuidUtils;
import cn.obcc.vo.RetData;
import cn.obcc.vo.driver.BlockTxInfo;
import cn.obcc.vo.driver.ContractInfo;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName NonceCu
 * @desc TODO
 * @date 2019/8/26 0026  8:36
 **/
public abstract class ContractHandler<T> extends BaseHandler<T> implements IContractHandler<T> {
    public static final Logger logger = LoggerFactory.getLogger(ContractHandler.class);

    protected abstract Map<String, String> buildMethodNameIdMap(ContractInfo contractInfo);

    @Override
    public CompileResult compile(String bizId, String contract, ExProps config) throws Exception {
        CompileResult contractCompile = ContractCompiler.compile(contract, getObccConfig());
        contractCompile.setBizId(bizId);
        if (contractCompile.getState() == -1) return contractCompile;
        //每个合约保存
        contractCompile.getContractBinList().stream().forEach((contractBin) -> {
            ContractInfo contractInfo = new ContractInfo();
            contractInfo.setBizId(bizId + contractBin.getName());
            contractInfo.setName(contractBin.getName());
            contractInfo.setAbi(contractBin.getAbi());
            contractInfo.setBin(contractBin.getBinary());
            contractInfo.setContent(contract);
            contractInfo.setMethodNameIdMapStr(JSON.toJSONString(buildMethodNameIdMap(contractInfo)));
            try {
                getDriver().getLocalDb().getContractInfoDao().add(contractInfo);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("compile error.", e);
            }
        });
        return contractCompile;
    }

    /**
     * 部署前或部署后的合约都可以，编译成功后。
     *
     * @param bizId
     * @param contractName
     * @return
     * @throws Exception
     */
    @Override
    public ContractInfo getContract(@NotEmpty String bizId, @NotEmpty String contractName) throws Exception {
        return getDriver().getLocalDb().getContractInfoDao()
                .findOne(" biz_id=? && name=? ", new Object[]{bizId + contractName, contractName});
    }

    /**
     * 部署后的合给
     *
     * @param contractAddr
     * @return
     * @throws Exception
     */
    @Override
    public ContractInfo getContract(@NotEmpty String contractAddr) throws Exception {
        if (StringUtils.isNullOrEmpty(contractAddr)) return null;
        return getDriver().getLocalDb().getContractInfoDao()
                .findOne(" contract_addr = ? ", new Object[]{contractAddr});
    }


    /**
     * 外部的合约加到本地数据库中来
     *
     * @param bizId
     * @param info
     * @return
     * @throws Exception
     */
    @Override
    public Boolean addContract(@NotEmpty String bizId, @NotNull ContractInfo info) throws Exception {
        info.setBizId(bizId + info.getName());
        getDriver().getLocalDb().getContractInfoDao().add(info);
        return true;
    }

    @Override
    public String deploy(@NotEmpty String bizId, @NotNull SrcAccount srcAccount, @NotNull ContractInfo contractInfo,
                         IUpchainFn<BlockTxInfo> fn, ExProps config, List<String> params) throws Exception {
        try {
            String hex = getDeployHexData(contractInfo.getBin(), contractInfo.getAbi(), params);
            srcAccount.setMemos(hex);
            config.setNeedHandleMemo(false);
            config.setNeedSplit(false);
            config.setUpchainType(EUpchainType.ContractDeploy);

            ChainPipe pipe = new ChainPipe();
            pipe.setChainCode(getDriver().getObccConfig().getChain().getName());
            pipe.setChainTxType(EChainTxType.Contract);
            pipe.setBizId(bizId);
            pipe.setSrcAccount(srcAccount);
            pipe.setAmount("0");
            pipe.setConfig(config);
            pipe.setDestAddr("0x");
            pipe.setCallbackFn(new IUpchainFn<BlockTxInfo>() {
                @Override
                public void exec(String bizId, String hash, EUpchainType upchainType, ETransferStatus state, BlockTxInfo resp) throws Exception {
                    if (state == ETransferStatus.STATE_CHAIN_CONSENSUS) {
                        contractInfo.setContractAddr(resp.getContractAddress());
                        contractInfo.setHash(hash);
                        contractInfo.setState(ETransferStatus.STATE_CHAIN_CONSENSUS.getStatus());
                        getDriver().getLocalDb().getContractInfoDao().update(contractInfo);
                    }
                    if (fn != null) {
                        fn.exec(bizId, hash, upchainType, state, (BlockTxInfo) resp);
                    }
                }
            });

            getDriver().getAccountHandler().transfer(pipe);

            //todo:保存到数据库
        } catch (Exception e) {
            e.printStackTrace();
        }

        return UuidUtils.get() + "";
    }

    @Override
    public String invoke(String bizId, ContractInfo contractInfo,
                         SrcAccount srcAccount, ExProps config, IUpchainFn<BlockTxInfo> fn, String methodName, List<String> params) throws Exception {
        try {
            String hex = getInvokeHexData(contractInfo.getAbi(), methodName, params);
            srcAccount.setMemos(hex);
            config.setNeedHandleMemo(false);
            config.setNeedSplit(false);
            config.setUpchainType(EUpchainType.ContractInvoke);

            ChainPipe pipe = new ChainPipe();
            pipe.setChainCode(getDriver().getObccConfig().getChain().getName());
            pipe.setChainTxType(EChainTxType.Contract);
            pipe.setBizId(bizId);
            pipe.setSrcAccount(srcAccount);
            pipe.setAmount("0");
            pipe.setConfig(config);
            pipe.setDestAddr(contractInfo.getContractAddr());
            pipe.setCallbackFn(fn);

            getDriver().getAccountHandler().transfer(pipe);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return UuidUtils.get() + "";
    }

}
