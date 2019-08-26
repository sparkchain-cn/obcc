package cn.obcc.stmt.ledger;

import java.util.List;
import java.util.Map;

import cn.obcc.config.ObccConfig;
import cn.obcc.driver.IChainDriver;
import cn.obcc.exception.enums.EDbOperaType;
import cn.obcc.stmt.IDbStatement;
import cn.obcc.stmt.ILedgerStatement;
import cn.obcc.stmt.base.BaseStatement;
import cn.obcc.stmt.fn.ITokenSendFn;
import cn.obcc.stmt.module.db.Procedure;
import cn.obcc.stmt.module.db.Table;
import cn.obcc.stmt.module.db.TableDefine;
import cn.obcc.vo.Page;
import cn.obcc.vo.RetData;

public class LedgerStatement extends BaseStatement implements ILedgerStatement {

	@Override
	public void createAccount(String username, String pwd) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void activate(String username, long tokenCount) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void createToken(String tokenCode, String tokenname, long count) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void send(String tokenCode, String srcUsername, String destUsername, long count, String memo,
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
