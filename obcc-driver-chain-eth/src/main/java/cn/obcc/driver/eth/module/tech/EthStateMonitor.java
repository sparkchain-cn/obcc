package cn.obcc.driver.eth.module.tech;

import org.web3j.protocol.Web3j;

import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.tech.IStateMonitor;
import cn.obcc.config.ReqConfig;
import cn.obcc.vo.RetData;

public class EthStateMonitor extends BaseHandler<Web3j> implements IStateMonitor<Web3j> {

	@Override
	public RetData<Long> getBlockHeight(ReqConfig config) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
