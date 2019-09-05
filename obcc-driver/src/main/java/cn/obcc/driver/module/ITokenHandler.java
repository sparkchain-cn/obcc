package cn.obcc.driver.module;

import cn.obcc.driver.IChainHandler;
import cn.obcc.driver.module.fn.ITokenCreateFn;
import cn.obcc.driver.module.fn.ITokenOperateFn;
import cn.obcc.driver.vo.*;
import cn.obcc.driver.vo.params.TokenParams;
import cn.obcc.vo.driver.BlockTxInfo;
import cn.obcc.vo.driver.TokenInfo;
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
     * 根据合约地址判断是否为token
     *
     * @param contractAddr
     * @return
     */
    public boolean isToken(String contractAddr) throws Exception;


    /**
     * 根据区块的记录解析出来其方法名和参数
     *
     * @param input
     * @return
     * @throws Exception
     */
    public ContractExecRec parseExecRec(TokenInfo info, String input) throws Exception;


    public default boolean isToken(TokenInfo info, ContractExecRec rec) {
        if (info.getTransferName() != null && info.getTransferName().equals(rec.getMethod())) {
            return true;
        }
        return false;
    }

    public default TokenRec parseExecRec(TokenInfo info, ContractExecRec rec) throws Exception {

        if (rec == null) return null;

        if (info.getTransferName() != null && info.getTransferName().equals(rec.getMethod())) {
            TokenRec tokenRec = new TokenRec();
            if (rec.getParams().size() == 3) {
                tokenRec.setDestAddr(rec.getParams().get(0).getVal());
                tokenRec.setAmount(rec.getParams().get(1).getVal());
                tokenRec.setMemo(rec.getParams().get(2).getVal());
            } else if (rec.getParams().size() == 2) {
                tokenRec.setDestAddr(rec.getParams().get(0).getVal());
                tokenRec.setAmount(rec.getParams().get(1).getVal());
            }
        }
        return null;
    }

    /**
     * @param tokenCodeOrContractAddr
     * @return
     * @throws Exception
     */
    public TokenInfo getToken(String tokenCodeOrContractAddr) throws Exception;


    public RetData<String> balanceOf(TokenInfo token, String address, ReqConfig<T> config) throws Exception;

    public RetData<String> freezeOf(TokenInfo token, String address, ReqConfig<T> config) throws Exception;

    public RetData<BlockTxInfo> operateInfo(TokenInfo token, String hash, ReqConfig<T> config) throws Exception;

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




    /**
     * 在解析区块流水时，从区块流水取出Token的目标地址，原来的目标是合约地址<br>
     * 需要转换出来
     *
     * @param input
     * @return
     * @throws Exception
     */
    // public String getDestAddr(TokenInfo token,String input) throws Exception;


    /**
     * 根据操作的方法,解析方法操作时带的备注，每种不同的合约，其操作不同<b>
     * 如果没有，那么就没有，本应用发的token，应该都带有说明
     *
     * @param input
     * @return
     * @throws Exception
     */
    //  public String getMemo(TokenInfo token,String method, String input) throws Exception;


}
