package cn.obcc.driver.eth.module;

import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.module.IBlockHandler;
import cn.obcc.vo.driver.BlockInfo;
import cn.obcc.vo.driver.BlockTxInfo;
import cn.obcc.driver.vo.DetailBlockInfo;
import cn.obcc.config.ReqConfig;
import cn.obcc.vo.RetData;
import org.web3j.protocol.Web3j;

public class EthBlockHandler extends BaseHandler<Web3j> implements IBlockHandler<Web3j> {


	@Override
	public RetData<Long> getInterval(ReqConfig config) throws Exception {
		return null;
	}

	@Override
	public RetData<Long> getBlockHeight(ReqConfig<Web3j> config) throws Exception {
		return null;
	}

	@Override
	public RetData<BlockInfo> getBlockInfo(long blockHeight, ReqConfig<Web3j> config) throws Exception {
		return null;
	}

	@Override
	public RetData<DetailBlockInfo> getDetailBlockInfo(long blockHeight, ReqConfig<Web3j> config) throws Exception {
		return null;
	}

	@Override
	public RetData<BlockTxInfo> pull(String hash) throws Exception {
		return null;
	}

//	@Override
	public boolean writeToDb(BlockTxInfo txInfo, ReqConfig<Web3j> config) throws Exception {
		return false;
	}

	//@Override
	public boolean writeToDb(BlockInfo blockInfo, ReqConfig<Web3j> config) throws Exception {
		return false;
	}
}
