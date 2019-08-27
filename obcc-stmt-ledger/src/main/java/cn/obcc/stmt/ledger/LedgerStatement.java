package cn.obcc.stmt.ledger;

import cn.obcc.stmt.ILedgerStatement;
import cn.obcc.stmt.base.BaseStatement;
import cn.obcc.stmt.fn.ITokenSendFn;
import cn.obcc.vo.Page;

public class LedgerStatement extends BaseStatement implements ILedgerStatement {


	@Override
	public void createAccount(String bizId, String username, String pwd) throws Exception {

	}

	@Override
	public void activate(String bizId, String username, long tokenCount) throws Exception {

	}

	@Override
	public void createToken(String bizId, String tokenCode, String tokenname, long count) throws Exception {

	}

	@Override
	public void send(String bizId,String tokenCode, String srcUsername, String destUsername, long count, String memo,
			ITokenSendFn fn) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void getBalance(String username, String tokenCode) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Page<Object> getBills(String username, String tokenCode, String destUsername, String limit)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
