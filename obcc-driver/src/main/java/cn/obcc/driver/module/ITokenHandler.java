package cn.obcc.driver.module;

import cn.obcc.driver.IChainHandler;
import cn.obcc.driver.module.fn.IUpchainFn;
import cn.obcc.driver.vo.*;
import cn.obcc.vo.driver.BlockTxInfo;
import cn.obcc.vo.driver.ContractInfo;
import cn.obcc.vo.driver.TokenInfo;
import cn.obcc.config.ExProps;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * @author mgicode
 * @desc IAccount
 * @email 546711211@qq.com
 * @date 2019年8月22日 上午9:59:42
 */
public interface ITokenHandler<T> extends IChainHandler<T> {

    /**
     * 调用合约编译，deploy，写到文件或sqlite中 pengrk created or updated at 2019年8月23日 下午5:28:03
     *
     * @param config
     * @return
     * @throws Exception
     */
    void createToken(String bizId, SrcAccount account, String contract, String contractName,
                     String tokenName, String tokenCode, Long tokenSupply,
                     IUpchainFn<BlockTxInfo> fn, ExProps config) throws Exception;

    /**
     * 已经存在的token，add上去
     *
     * @param info
     * @return
     * @throws Exception
     */
    public void addToken(TokenInfo info) throws Exception;

    /**
     * 根据合约地址判断是否为token
     *
     * @param contractAddr
     * @return
     */
    public boolean isExistToken(String contractAddr) throws Exception;


    /**
     * @param contractAddr
     * @return
     * @throws Exception
     */
    public TokenInfo getToken(String contractAddr) throws Exception;

    /**
     * 根据区块的记录解析出来其方法名和参数
     *
     * @param input
     * @return
     * @throws Exception
     */
    /**
     * 主要用来处理精度问题
     *
     * @param contractAddr
     * @param rec
     * @return
     * @throws Exception
     */
    public TokenRec parseTxInfo(String contractAddr, ContractRec rec) throws Exception;


    public String balanceOf(TokenInfo token, String address, ExProps config) throws Exception;


    public String transfer(String bizId, SrcAccount account, TokenInfo token,
                           String destAccount, String amount, ExProps config, IUpchainFn<BlockTxInfo> fn) throws Exception;


    /**
     * burn(uint256 _value)
     *
     * @param bizId
     * @param account
     * @param token
     * @param amount
     * @param config
     * @param fn
     * @return
     * @throws Exception
     */
    public String burn(String bizId, SrcAccount account, TokenInfo token,
                       String amount, ExProps config, IUpchainFn<BlockTxInfo> fn) throws Exception;

    /**
     * @param bizId
     * @param account
     * @param token
     * @param amount
     * @param config
     * @return
     * @throws Exception
     */
    public String supply(String bizId, SrcAccount account, TokenInfo token,
                         String amount, ExProps config, IUpchainFn<BlockTxInfo> fn) throws Exception;

    /**
     * approve(address _spender, uint256 _value)
     *
     * @param bizId
     * @param account
     * @param token
     * @param spenderAddr
     * @param amount
     * @param config
     * @return
     * @throws Exception
     */
    public String approve(String bizId, SrcAccount account, TokenInfo token, String spenderAddr, String
            amount, ExProps config, IUpchainFn<BlockTxInfo> fn) throws Exception;

    /**
     * transferFrom(address _from, address _to, uint256 _value)
     *
     * @param bizId
     * @param account
     * @param token
     * @param amount
     * @param config
     * @return
     * @throws Exception
     */
    public String transferFrom(String bizId, SrcAccount account, TokenInfo token, String srcAddr,
                               String toAddr, String amount, ExProps config, IUpchainFn<BlockTxInfo> fn) throws Exception;


}
