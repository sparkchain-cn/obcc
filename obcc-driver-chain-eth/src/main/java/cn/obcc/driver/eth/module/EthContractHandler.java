package cn.obcc.driver.eth.module;

import cn.obcc.config.ReqConfig;
import cn.obcc.driver.contract.ContractHandler;
import cn.obcc.driver.module.IContractHandler;
import cn.obcc.driver.module.fn.IContractDeployFn;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.vo.driver.ContractInfo;
import org.web3j.protocol.Web3j;


public class EthContractHandler extends ContractHandler<Web3j> implements IContractHandler<Web3j> {

    @Override
    protected void deploy(SrcAccount srcAccount, ContractInfo contractInfo, IContractDeployFn fn, ReqConfig<Web3j> config) {

    }
}
