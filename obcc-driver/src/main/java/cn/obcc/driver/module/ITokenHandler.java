package cn.obcc.driver.module;

import cn.obcc.driver.IChainHandler;
import cn.obcc.driver.module.fn.ITokenCreateFn;
import cn.obcc.driver.module.fn.ITokenOperateFn;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.vo.driver.TokenInfo;
import cn.obcc.driver.vo.TransactionInfo;
import cn.obcc.driver.vo.TokenParams;
import cn.obcc.config.ReqConfig;
import cn.obcc.vo.RetData;

import java.math.BigInteger;

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
    public void createToken(String bizId, SrcAccount account, TokenParams params,
                            ITokenCreateFn fn, ReqConfig<T> config) throws Exception;

    /**
     * 已经存在的token，add上去
     *
     * @param bizId
     * @param params
     * @param fn
     * @param config
     * @return
     * @throws Exception
     */
    public RetData<String> addToken(String bizId, TokenInfo params,
                                    ITokenCreateFn fn, ReqConfig<T> config) throws Exception;

    /**
     * @param tokenCodeOrContractAddress
     * @param config
     * @return
     * @throws Exception
     */
    public RetData<TokenInfo> getToken(String tokenCodeOrContractAddress, ReqConfig<T> config) throws Exception;


    public RetData<String> balanceOf(TokenInfo token, String address, ReqConfig<T> config) throws Exception;

    public RetData<String> freezeOf(TokenInfo token, String address, ReqConfig<T> config) throws Exception;

    public RetData<TransactionInfo> operateInfo(TokenInfo token, String hash, ReqConfig<T> config) throws Exception;

    /**********************************************************************************************************/
    public RetData<String> transfer(String bizId, SrcAccount account, TokenInfo token,
                                    String destAccount, ReqConfig<T> config, ITokenOperateFn fn) throws Exception;

    public RetData<String> freeze(String bizId, SrcAccount account, TokenInfo token,
                                  BigInteger amount, ReqConfig<T> config, ITokenOperateFn fn) throws Exception;

    public RetData<String> unfreeze(String bizId, SrcAccount account, TokenInfo token,
                                    BigInteger amount, ReqConfig<T> config, ITokenOperateFn fn) throws Exception;

    /*********************************************************************************************/
    public RetData<String> burn(String bizId, SrcAccount account, TokenInfo token,
                                BigInteger amount, ReqConfig<T> config) throws Exception;

    public RetData<String> supply(String bizId, SrcAccount account, TokenInfo token,
                                  BigInteger amount, ReqConfig<T> config) throws Exception;

    /*************************************************************************************************************/
    public RetData<String> approve(String bizId, SrcAccount account, TokenInfo token,
                                   String destAccount, BigInteger amount, ReqConfig<T> config) throws Exception;

    public RetData<String> transferFrom(String bizId, SrcAccount account, TokenInfo token,
                                        BigInteger amount, ReqConfig<T> config) throws Exception;
    /**************************************************************************************************/

}
