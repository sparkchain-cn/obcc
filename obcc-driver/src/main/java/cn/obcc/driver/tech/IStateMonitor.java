package cn.obcc.driver.tech;

import cn.obcc.driver.IChainHandler;
import cn.obcc.config.ExProps;
import cn.obcc.vo.RetData;

/**
 * 链的状态，交易的状态（queue,pending)
 * 
 * @desc IMonitor
 * @author mgicode
 * @email 546711211@qq.com
 * @date 2019年8月22日 上午10:48:45
 *
 */
public interface IStateMonitor<T>  extends IChainHandler<T>{

	/**
	 * 
	 *
	 * pengrk created or updated at 2019年3月29日 下午3:34:51
	 * 
	 * @param config
	 * @return
	 * @throws Exception
	 *
	 */
	public Long getBlockHeight(ExProps config) throws Exception;
}
