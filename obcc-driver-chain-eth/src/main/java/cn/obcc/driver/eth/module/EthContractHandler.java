package cn.obcc.driver.eth.module;

import cn.obcc.driver.module.fn.IContractDeployFn;
import cn.obcc.driver.module.fn.IContractInvokeFn;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.config.ReqConfig;
import org.web3j.protocol.Web3j;

import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.module.IContractHandler;
import cn.obcc.driver.vo.ContractInfo;
import cn.obcc.exception.enums.EContractType;
import cn.obcc.vo.RetData;


public class EthContractHandler  extends BaseHandler<Web3j> implements IContractHandler<Web3j> {

	@Override
	public RetData<ContractInfo> compile(String contract, ReqConfig<Web3j> config) throws Exception {
		return null;
	}

	@Override
	public RetData<ContractInfo> compile(String contract, EContractType type, String version, ReqConfig<Web3j> config) throws Exception {
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
	public RetData<Object> query(ReqConfig<Web3j> config, Object... params) throws Exception {
		return null;
	}

	@Override
	public RetData<String> invoke(String bizId, SrcAccount srcAccount, ReqConfig<Web3j> config, IContractInvokeFn fn, Object... params) throws Exception {
		return null;
	}


}
