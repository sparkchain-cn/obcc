package cn.obcc.stmt.ledger;

import cn.obcc.config.ExProps;
import cn.obcc.driver.module.fn.IUpchainFn;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.exception.ObccException;
import cn.obcc.exception.enums.EExceptionCode;
import cn.obcc.exception.enums.EStmtType;
import cn.obcc.stmt.ILedgerStatement;
import cn.obcc.stmt.base.BaseStatement;
import cn.obcc.stmt.fn.ITokenSendFn;
import cn.obcc.vo.Page;
import cn.obcc.vo.driver.AccountInfo;
import cn.obcc.vo.driver.RecordInfo;
import cn.obcc.vo.driver.TokenInfo;
import lombok.NonNull;

public class LedgerStatement extends BaseStatement implements ILedgerStatement {

    @Override
    public void createAccount(@NonNull String bizId, @NonNull String username, @NonNull String pwd) throws Exception {
        ExProps exProps = new ExProps() {{
            setRecordInfo(new RecordInfo() {{
                setStmtType(EStmtType.LEDGER_CREATE_USER);
            }});
        }};
        getDriver().getAccountHandler().createAccount(bizId, username, pwd, exProps);
    }


    @Override
    public void activate(@NonNull String bizId, @NonNull String username, @NonNull long tokenCount) throws Exception {
        checkBizId(bizId);
        SrcAccount account = new SrcAccount();
        account.setSrcAddr(config.getTokenCreateAccount().getKey());
        account.setSecret(config.getTokenCreateAccount().getVal());
        AccountInfo dest = getAccountInfo(username);
        //只做特有的部分
        ExProps exProps = new ExProps() {{
            setRecordInfo(new RecordInfo() {{
                setStmtType(EStmtType.LEDGER_CREATE_USER);
                setDestUser(username);
            }});
        }};
        getDriver().getAccountHandler().transfer(bizId, account, tokenCount + "", dest.getAddress(), exProps, null);
    }

    @Override
    public void createToken(@NonNull String bizId, @NonNull String tokenCode, @NonNull String tokenName, @NonNull long count) throws Exception {
        checkBizId(bizId);
        SrcAccount account = new SrcAccount();
        account.setSrcAddr(config.getTokenCreateAccount().getKey());
        account.setSecret(config.getTokenCreateAccount().getVal());
        IUpchainFn fn = (bizId1, hash, upchainType, state, resp) -> {
        };
        //只做特有的部分
        ExProps exProps = new ExProps() {{
            setRecordInfo(new RecordInfo() {{
                setStmtType(EStmtType.LEDGER_CREATE_USER);
                setToken(tokenCode);
            }});
        }};
        getDriver().getTokenHandler().createToken(bizId, account, tokenName, tokenCode, count, fn, exProps);
    }

    @Override
    public void send(String bizId, String tokenCode, String srcUsername,
                     String destUsername, long count, String memo, ITokenSendFn fn) throws Exception {
        checkBizId(bizId);
        SrcAccount account = new SrcAccount();
        account.setSrcAddr(config.getTokenCreateAccount().getKey());
        account.setSecret(config.getTokenCreateAccount().getVal());
        TokenInfo tokenInfo = getDriver().getTokenHandler().getToken(tokenCode);
        if (tokenInfo == null) {
            throw ObccException.create(EExceptionCode.RETURN_NULL_OR_EMPTY, "tokencode:{0} 找不到TokenInfo对象", tokenCode);
        }
        //只做特有的部分
        ExProps exProps = new ExProps() {{
            setRecordInfo(new RecordInfo() {{
                setStmtType(EStmtType.LEDGER_TOKEN_SEND);
                setSrcUser(srcUsername);
                setDestUser(destUsername);
                setToken(tokenCode);
            }});
        }};
        getDriver().getTokenHandler().transfer(bizId, account, tokenInfo, null, tokenCode, exProps, null);

    }


    @Override
    public String getBalance(@NonNull String username, String tokenCode) throws Exception {
        AccountInfo info = getAccountInfo(username);
        if (tokenCode == null || tokenCode.equals(config.getChain().getToken())) {
            return getDriver().getAccountHandler().getBalance(info.getAddress(), new ExProps());
        }
        TokenInfo tokenInfo = getDriver().getTokenHandler().getToken(tokenCode);
        if (tokenInfo == null) {
            throw ObccException.create(EExceptionCode.RETURN_NULL_OR_EMPTY, "tokencode:{0} 找不到TokenInfo对象", tokenCode);
        }
        return getDriver().getTokenHandler().balanceOf(tokenInfo, info.getAddress(), new ExProps());

    }

    @Override
    public Page<Object> getBills(String username, String tokenCode, String destUsername, String limit) throws Exception {
        // TODO
        throw new RuntimeException("un impl.");
    }

    protected AccountInfo getAccountInfo(String name) throws Exception {
        AccountInfo info = getDriver().getLocalDb().getAccountInfoDao().get(" user_name=? ", new Object[]{name});
        if (info == null) {
            throw ObccException.create(EExceptionCode.RETURN_NULL_OR_EMPTY, "username:{0} 找不到对象的账户", name);
        }
        return info;
    }
}
