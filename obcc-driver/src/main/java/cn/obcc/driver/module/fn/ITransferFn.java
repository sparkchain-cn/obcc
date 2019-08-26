package cn.obcc.driver.module.fn;

import cn.obcc.exception.enums.StateEnum;

import java.util.Map;

public interface ITransferFn {

	/**
	 *
	 * @param bizId
	 * @param hash
	 * @param state
	 * @param resp
	 * @throws Exception
	 */
    public void exec(String bizId, String hash, StateEnum state, Map<String, String> resp) throws Exception;

}
