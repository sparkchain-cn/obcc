package cn.obcc.stmt.ledger;

import cn.obcc.config.ExConfig;
import cn.obcc.driver.module.fn.IStateListener;
import cn.obcc.driver.vo.FromAccount;
import cn.obcc.exception.ObccException;
import cn.obcc.exception.enums.EExceptionCode;
import cn.obcc.stmt.ILedgerStatement;
import cn.obcc.stmt.base.BaseStatement;
import cn.obcc.stmt.fn.ITokenSendFn;
import cn.obcc.vo.BizState;
import cn.obcc.vo.Page;
import cn.obcc.vo.driver.AccountInfo;
import cn.obcc.vo.driver.TokenInfo;
import lombok.NonNull;

public class LedgerStatement extends BaseStatement implements ILedgerStatement {

    @Override
    public String createAccount(@NonNull String bizId, @NonNull String username, @NonNull String pwd) throws Exception {
        getDriver().getAccountHandler().createAccount(bizId, username, pwd, null);
        return null;
    }


    @Override
    public String activate(@NonNull String bizId, @NonNull String username, @NonNull long tokenCount) throws Exception {
        checkBizId(bizId);
        FromAccount account = new FromAccount();
        account.setSrcAddr(config.getTokenCreateAccount().getKey());
        account.setSecret(config.getTokenCreateAccount().getVal());
        AccountInfo dest = getAccountInfo(username);

        return getDriver().getAccountHandler().pay(bizId, account, tokenCount + "", dest.getAddress());
    }

    @Override
    public String createToken(@NonNull String bizId, @NonNull String tokenCode, @NonNull String tokenName, @NonNull long count) throws Exception {
        checkBizId(bizId);
        FromAccount account = new FromAccount();
        account.setSrcAddr(config.getTokenCreateAccount().getKey());
        account.setSecret(config.getTokenCreateAccount().getVal());

        return getDriver().getTokenHandler().createToken(bizId, account, tokenName, tokenCode, count);
    }

    @Override
    public String send(String bizId, String tokenCode, String srcUser,
                       String destUser, long count, String memo, ITokenSendFn fn) throws Exception {
        checkBizId(bizId);

        AccountInfo ai = getDriver().getLocalDb().getAccountInfoDao().getByProp("userName", srcUser);

        FromAccount account = new FromAccount();
        account.setSrcAddr(ai.getAddress());
        account.setSecret(ai.getSecret());
        account.setMemos(memo);

        TokenInfo tokenInfo = getDriver().getTokenHandler().getToken(tokenCode);
        if (tokenInfo == null) {
            throw ObccException.create(EExceptionCode.RETURN_NULL_OR_EMPTY, "tokencode:{0} 找不到TokenInfo对象", tokenCode);
        }

        String destAddr = getDriver().getLocalDb().getAccountInfoDao().getByProp("userName", destUser).getAddress();

        return getDriver().getTokenHandler().transfer(bizId, account, tokenInfo, destAddr, tokenCode, null, null);

    }


    @Override
    public String getBalance(@NonNull String username, String tokenCode) throws Exception {
        AccountInfo info = getAccountInfo(username);
        if (tokenCode == null || tokenCode.equals(config.getChain().getToken())) {
            return getDriver().getAccountHandler().getBalance(info.getAddress(), new ExConfig());
        }
        TokenInfo tokenInfo = getDriver().getTokenHandler().getToken(tokenCode);
        if (tokenInfo == null) {
            throw ObccException.create(EExceptionCode.RETURN_NULL_OR_EMPTY, "tokencode:{0} 找不到TokenInfo对象", tokenCode);
        }
        return getDriver().getTokenHandler().balanceOf(tokenInfo, info.getAddress(), new ExConfig());

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
