package cn.obcc.driver.tech.base.excutor;

import cn.obcc.config.ExConfig;
import cn.obcc.driver.IChainDriver;
import cn.obcc.listener.IStateListener;
import cn.obcc.vo.driver.ChainPipe;
import cn.obcc.vo.driver.FromAccount;
import cn.obcc.enums.ETransferState;
import cn.obcc.vo.BizState;
import cn.obcc.vo.db.BlockTxInfo;
import cn.obcc.vo.db.ContractInfo;
import lombok.NonNull;

import java.util.List;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName DeployExecutor
 * @desc TODO
 * @date 2019/9/18 0018  13:35
 **/
public class DeployInvokeExecutor {

    public static String deploy(IChainDriver driver, @NonNull String bizId, @NonNull FromAccount fromAccount, @NonNull ContractInfo ci, List<Object> params,
                                IStateListener stateListener, @NonNull ExConfig exConfig) throws Exception {
        //hex memo
        // String hex = getDeployHexData(ci.getBin(), ci.getAbi(), params);
        //fromAccount.setMemos(hex);

        //配置扩展参数
        exConfig.setNeedHandleMemo(false);
        exConfig.setNeedSplit(false);
        //exProps.setUpchainType(EUpchainType.ContractDeploy);

        //build chainPipe
        ChainPipe pipe = new ChainPipe();

        pipe.getBizState().setChainCode(driver.getConfig().getChain().getName());
        pipe.getBizState().setBizId(bizId);
        pipe.setFromAccount(fromAccount);
        pipe.setAmount("0");
        pipe.setExConfig(exConfig);
        pipe.setDestAddr("0x");

        //build state Listener
        pipe.setStateListener((BizState bizState, Object resp) -> {
            if (bizState.getTransferState() == ETransferState.STATE_CHAIN_CONSENSUS) {
                BlockTxInfo txInfo = (BlockTxInfo) resp;
                ci.setContractAddr(txInfo.getContractAddress());
                ci.setHash(bizState.getHashes());
                ci.setState(ETransferState.STATE_CHAIN_CONSENSUS.getStatus());
                driver.getLocalDb().getContractInfoDao().update(ci);
            }
            if (stateListener != null) {
                stateListener.exec(bizState, resp);
            }
        });

        //deploy
        return driver.getAccountHandler().transfer(pipe);

    }


    public static String invoke(IChainDriver driver, @NonNull String bizId, @NonNull String contractAddr, @NonNull FromAccount fromAccount
            , IStateListener stateListener, ExConfig config) throws Exception {
        // String hex = getInvokeHexData(contractInfo.getAbi(), fnName, params);
        // fromAccount.setMemos(hex);
        config.setNeedHandleMemo(false);
        config.setNeedSplit(false);
        //config.setUpchainType(EUpchainType.ContractInvoke);

        ChainPipe pipe = new ChainPipe();
        pipe.getBizState().setChainCode(driver.getConfig().getChain().getName());
        pipe.getBizState().setBizId(bizId);
        pipe.setFromAccount(fromAccount);
        pipe.setAmount("0");
        pipe.setExConfig(config);
        pipe.setDestAddr(contractAddr);
        pipe.setStateListener(stateListener);

        return driver.getAccountHandler().transfer(pipe);
    }

}
