package cn.obcc.driver.handler;

import cn.obcc.driver.IDriverHandler;
import cn.obcc.listener.IStateListener;
import cn.obcc.vo.contract.ContractRec;
import cn.obcc.vo.contract.TokenRec;
import cn.obcc.vo.db.TokenInfo;
import cn.obcc.config.ExConfig;
import cn.obcc.vo.driver.FromAccount;

/**
 * @author mgicode
 * @desc IAccount
 * @email 546711211@qq.com
 * @date 2019年8月22日 上午9:59:42
 */
public interface ITokenHandler<T> extends IDriverHandler<T> {

    public default String createToken(String bizId, FromAccount account, String tokenName, String tokenCode, Long tokenSupply, ExConfig config) throws Exception {
        return createToken(bizId, account, tokenName, tokenCode, tokenSupply, null, config);
    }

    public default String createToken(String bizId, FromAccount account, String tokenName, String tokenCode, Long tokenSupply) throws Exception {
        return createToken(bizId, account, tokenName, tokenCode, tokenSupply, (IStateListener) null);
    }

    public default String createToken(String bizId, FromAccount account, String tokenName, String tokenCode, Long tokenSupply,
                                      IStateListener fn) throws Exception {
        return createToken(bizId, account, tokenName, tokenCode, tokenSupply, fn, null);
    }

    public String createToken(String bizId, FromAccount account, String tokenName, String tokenCode, Long tokenSupply,
                              IStateListener fn, ExConfig config) throws Exception;

    /**
     * 调用合约编译，deploy，写到文件或sqlite中
     * pengrk created or updated at 2019年8月23日 下午5:28:03
     *
     * @param config
     * @return
     * @throws Exception
     */
    String createToken(String bizId, FromAccount account, String contract, String contractName,
                     String tokenName, String tokenCode, Long tokenSupply,
                     IStateListener fn, ExConfig config) throws Exception;

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


    public String balanceOf(TokenInfo token, String address, ExConfig config) throws Exception;

    public default String transfer(String bizId, FromAccount account, TokenInfo token,
                                   String destAccount, String amount, ExConfig config) throws Exception {
        return transfer(bizId, account, token, destAccount, amount, (IStateListener) null, config);
    }

    public default String transfer(String bizId, FromAccount account, TokenInfo token,
                                   String destAccount, String amount) throws Exception {
        return transfer(bizId, account, token, destAccount, amount, (IStateListener) null);
    }

    public default String transfer(String bizId, FromAccount account, TokenInfo token,
                                   String destAccount, String amount, IStateListener fn) throws Exception {
        return transfer(bizId, account, token, destAccount, amount, fn, null);
    }

    public String transfer(String bizId, FromAccount account, TokenInfo token,
                           String destAccount, String amount, IStateListener fn, ExConfig config) throws Exception;


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
    public String burn(String bizId, FromAccount account, TokenInfo token,
                       String amount, IStateListener fn, ExConfig config) throws Exception;

    /**
     * @param bizId
     * @param account
     * @param token
     * @param amount
     * @param config
     * @return
     * @throws Exception
     */
    public String supply(String bizId, FromAccount account, TokenInfo token,
                         String amount, IStateListener fn, ExConfig config) throws Exception;

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
    public String approve(String bizId, FromAccount account, TokenInfo token, String spenderAddr, String
            amount, IStateListener fn, ExConfig config) throws Exception;

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
    public String transferFrom(String bizId, FromAccount account, TokenInfo token, String srcAddr,
                               String toAddr, String amount, IStateListener fn, ExConfig config) throws Exception;


}
