package cn.obcc.driver.eth.module;

import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.module.ITokenHandler;
import cn.obcc.driver.module.fn.IUpchainFn;
import cn.obcc.driver.vo.*;
import cn.obcc.driver.vo.params.TokenParams;
import cn.obcc.vo.driver.BlockTxInfo;
import cn.obcc.vo.driver.TokenInfo;
import cn.obcc.config.ExProps;
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
    public void createToken(String bizId, SrcAccount account, TokenParams params, IUpchainFn<BlockTxInfo> fn, ExProps config) throws Exception {

    }

    @Override
    public RetData<String> addToken(String bizId, TokenInfo params, IUpchainFn<BlockTxInfo> fn, ExProps config) throws Exception {
        return null;
    }

    @Override
    public boolean isToken(String contractAddr) throws Exception {
        return false;
    }

    @Override
    public ContractExecRec parseExecRec(TokenInfo info, String input) throws Exception {
        return null;
    }

    @Override
    public boolean isToken(TokenInfo info, ContractExecRec rec) {
        return false;
    }

    @Override
    public TokenRec parseExecRec(TokenInfo info, ContractExecRec rec) throws Exception {
        return null;
    }

    @Override
    public TokenInfo getToken(String tokenCodeOrContractAddr) throws Exception {
        return null;
    }


    @Override
    public String balanceOf(TokenInfo token, String address, ExProps config) throws Exception {
        return null;
    }

    @Override
    public String freezeOf(TokenInfo token, String address, ExProps config) throws Exception {
        return null;
    }

    @Override
    public BlockTxInfo operateInfo(TokenInfo token, String hash, ExProps config) throws Exception {
        return null;
    }

    @Override
    public String transfer(String bizId, SrcAccount account, TokenInfo token, String destAccount, ExProps config, IUpchainFn<BlockTxInfo> fn) throws Exception {
        return null;
    }

    @Override
    public String freeze(String bizId, SrcAccount account, TokenInfo token, BigInteger amount, ExProps config, IUpchainFn<BlockTxInfo> fn) throws Exception {
        return null;
    }

    @Override
    public String unfreeze(String bizId, SrcAccount account, TokenInfo token, BigInteger amount, ExProps config, IUpchainFn<BlockTxInfo> fn) throws Exception {
        return null;
    }

    @Override
    public String burn(String bizId, SrcAccount account, TokenInfo token, BigInteger amount, ExProps config) throws Exception {
        return null;
    }

    @Override
    public String supply(String bizId, SrcAccount account, TokenInfo token, BigInteger amount, ExProps config) throws Exception {
        return null;
    }

    @Override
    public String approve(String bizId, SrcAccount account, TokenInfo token, String destAccount, BigInteger amount, ExProps config) throws Exception {
        return null;
    }

    @Override
    public String transferFrom(String bizId, SrcAccount account, TokenInfo token, BigInteger amount, ExProps config) throws Exception {
        return null;
    }
}
