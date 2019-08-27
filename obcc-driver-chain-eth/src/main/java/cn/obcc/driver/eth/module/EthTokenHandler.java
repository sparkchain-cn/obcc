package cn.obcc.driver.eth.module;

import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.module.ITokenHandler;
import cn.obcc.driver.module.fn.ITokenCreateFn;
import cn.obcc.driver.module.fn.ITokenOperateFn;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.vo.driver.TokenInfo;
import cn.obcc.driver.vo.TokenParams;
import cn.obcc.driver.vo.TransactionInfo;
import cn.obcc.config.ReqConfig;
import cn.obcc.vo.RetData;
import org.web3j.protocol.Web3j;

import java.math.BigInteger;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName EthTokenHandler
 * @desc TODO
 * @date 2019/8/24 0024  18:54
 **/
public class EthTokenHandler extends BaseHandler<Web3j> implements ITokenHandler<Web3j> {
    @Override
    public void createToken(String bizId, SrcAccount account, TokenParams params, ITokenCreateFn fn, ReqConfig<Web3j> config) throws Exception {

    }

    @Override
    public RetData<String> addToken(String bizId, TokenInfo params, ITokenCreateFn fn, ReqConfig<Web3j> config) throws Exception {
        return null;
    }

    @Override
    public RetData<TokenInfo> getToken(String tokenCode, ReqConfig<Web3j> config) throws Exception {
        return null;
    }

    @Override
    public RetData<String> balanceOf(TokenInfo token, String address, ReqConfig<Web3j> config) throws Exception {
        return null;
    }

    @Override
    public RetData<String> freezeOf(TokenInfo token, String address, ReqConfig<Web3j> config) throws Exception {
        return null;
    }

    @Override
    public RetData<TransactionInfo> operateInfo(TokenInfo token, String hash, ReqConfig<Web3j> config) throws Exception {
        return null;
    }

    @Override
    public RetData<String> transfer(String bizId, SrcAccount account, TokenInfo token, String destAccount, ReqConfig<Web3j> config, ITokenOperateFn fn) throws Exception {
        return null;
    }

    @Override
    public RetData<String> freeze(String bizId, SrcAccount account, TokenInfo token, BigInteger amount, ReqConfig<Web3j> config, ITokenOperateFn fn) throws Exception {
        return null;
    }

    @Override
    public RetData<String> unfreeze(String bizId, SrcAccount account, TokenInfo token, BigInteger amount, ReqConfig<Web3j> config, ITokenOperateFn fn) throws Exception {
        return null;
    }

    @Override
    public RetData<String> burn(String bizId, SrcAccount account, TokenInfo token, BigInteger amount, ReqConfig<Web3j> config) throws Exception {
        return null;
    }

    @Override
    public RetData<String> supply(String bizId, SrcAccount account, TokenInfo token, BigInteger amount, ReqConfig<Web3j> config) throws Exception {
        return null;
    }

    @Override
    public RetData<String> approve(String bizId, SrcAccount account, TokenInfo token, String destAccount, BigInteger amount, ReqConfig<Web3j> config) throws Exception {
        return null;
    }

    @Override
    public RetData<String> transferFrom(String bizId, SrcAccount account, TokenInfo token, BigInteger amount, ReqConfig<Web3j> config) throws Exception {
        return null;
    }
}
