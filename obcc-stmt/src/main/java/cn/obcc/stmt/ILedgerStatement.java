package cn.obcc.stmt;

import cn.obcc.stmt.fn.ITokenSendFn;
import cn.obcc.vo.Page;

public interface ILedgerStatement extends IStatement {

	/**
	 * 
	 * 创建账户时直接给大量的原生币，作GAS用<br>
	 * 直接激活<br>
	 * pengrk created or updated at 2019年8月22日 上午11:27:23
	 */
	public void createAccount(String username, String pwd) throws Exception;

	/**
	 * 
	 * 给指定用户激活的原生币数 ,主要用于gas不够<br>
	 * pengrk created or updated at 2019年8月22日 上午11:29:01
	 */
	public void activate(String username, long tokenCount) throws Exception;

	/**
	 * 
	 * 发布指定数量的积分<br>
	 * pengrk created or updated at 2019年8月22日 上午11:37:01
	 */
	public void createToken(String tokenCode, String tokenname, long count) throws Exception;

	/**
	 * 
	 * 积分转让<br>
	 * pengrk created or updated at 2019年8月22日 上午11:37:35
	 */
	public void send(String tokenCode, String srcUsername, String destUsername, long count, String memo,
			ITokenSendFn fn) throws Exception;

	/**
	 * 查看指定用户的积分量<br>
	 *
	 * pengrk created or updated at 2019年8月22日 上午11:38:54
	 */
	public void getBalance(String username, String tokenCode) throws Exception;

	/**
	 * 查看指定用户指定币种的账单<br>
	 *
	 * pengrk created or updated at 2019年8月22日 上午11:51:18
	 * 
	 * @param username
	 * @param tokenCode
	 * @param destUsername
	 * @param limit        筛选金额表达式  "x<10,x>5,x>10 or x<5, x>5 and x<10
	 * @return
	 * @throws Exception
	 */
	public Page<Object> getBills(String username, String tokenCode, String destUsername, String limit) throws Exception;

}
