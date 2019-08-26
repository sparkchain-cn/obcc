package cn.obcc.driver.tech;

import cn.obcc.driver.IChainHandler;
import cn.obcc.driver.vo.BcMemo;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.config.ReqConfig;
import cn.obcc.vo.RetData;

public interface IMemoParser<T> extends IChainHandler<T> {

	public RetData<String> encode(SrcAccount transInfo, ReqConfig<T> config) throws Exception;

	public RetData<BcMemo> decode(SrcAccount transInfo, ReqConfig<T> config) throws Exception;

}
