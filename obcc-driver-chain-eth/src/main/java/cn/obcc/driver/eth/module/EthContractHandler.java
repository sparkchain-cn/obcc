package cn.obcc.driver.eth.module;

import cn.obcc.config.ExProps;
import cn.obcc.driver.contract.ContractHandler;
import cn.obcc.driver.module.IContractHandler;
import cn.obcc.driver.module.fn.IUpchainFn;
import cn.obcc.driver.vo.ChainPipe;
import cn.obcc.driver.vo.ContractExecRec;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.vo.RetData;
import cn.obcc.vo.driver.BlockTxInfo;
import cn.obcc.vo.driver.ContractInfo;
import org.web3j.protocol.Web3j;


public class EthContractHandler extends ContractHandler<Web3j> implements IContractHandler<Web3j> {

    @Override
    public void onDeploy(SrcAccount srcAccount, ContractInfo contractInfo, IUpchainFn<BlockTxInfo> fn, ExProps config) {

    }

    @Override
    public String doDeploy(ChainPipe pipe) throws Exception {
        return null;
    }

    @Override
    public String doInvoke(ChainPipe pipe) throws Exception {
        return null;
    }

    @Override
    public ContractExecRec parseExecRec(ContractInfo contractInfo, String input) throws Exception {
        return null;
    }
}
