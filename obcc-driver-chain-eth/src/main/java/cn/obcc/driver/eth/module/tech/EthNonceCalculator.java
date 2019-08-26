package cn.obcc.driver.eth.module.tech;

import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.tech.INonceCalculator;
import cn.obcc.config.ReqConfig;
import cn.obcc.vo.RetData;
import org.web3j.protocol.Web3j;

public class EthNonceCalculator extends BaseHandler<Web3j> implements INonceCalculator<Web3j> {

	@Override
	public RetData<Long> getNonce(String address, ReqConfig<Web3j> config) throws Exception {
		// TODO Auto-generated method stub<Web3j>
		return null;
	}

}
