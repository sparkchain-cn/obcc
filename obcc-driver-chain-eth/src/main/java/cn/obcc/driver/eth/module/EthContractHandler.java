package cn.obcc.driver.eth.module;

import cn.obcc.driver.module.fn.IContractCompileFn;
import cn.obcc.driver.module.fn.IContractDeployFn;
import cn.obcc.driver.module.fn.IContractInvokeFn;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.config.ReqConfig;
import org.web3j.protocol.Web3j;

import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.module.IContractHandler;
import cn.obcc.vo.driver.ContractInfo;
import cn.obcc.vo.RetData;


public class EthContractHandler extends BaseHandler<Web3j> implements IContractHandler<Web3j> {


    @Override
    public RetData<ContractInfo> compile(String bizId, String contract, ReqConfig<Web3j> config) throws Exception {
        return null;
    }

    @Override
    public void compile(String bizId, String contract, IContractCompileFn fn, ReqConfig<Web3j> config) throws Exception {

    }

    @Override
    public RetData<Boolean> addContract(String bizId, ContractInfo params) throws Exception {
        return null;
    }

    @Override
    public ContractInfo getContract(String bizId) throws Exception {
        return null;
    }

    @Override
    public RetData<String> deploy(String bizId, SrcAccount srcAccount, ContractInfo contract, IContractDeployFn fn, ReqConfig<Web3j> config) throws Exception {
        return null;
    }

    @Override
    public RetData<String> deploy(String bizId, SrcAccount srcAccount, String contract, IContractDeployFn fn, ReqConfig<Web3j> config) throws Exception {
        return null;
    }

    @Override
    public RetData<Object> query(ContractInfo contractInfo, ReqConfig<Web3j> config, String methodName, Object... params) throws Exception {
        return null;
    }

    @Override
    public RetData<String> invoke(String bizId, ContractInfo contractInfo, SrcAccount srcAccount, ReqConfig<Web3j> config, IContractInvokeFn fn, Object... params) throws Exception {
        return null;
    }
}
