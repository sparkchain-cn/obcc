package cn.obcc.driver.module;

import cn.obcc.driver.IChainHandler;
import cn.obcc.driver.module.fn.IUpchainFn;
import cn.obcc.driver.vo.*;
import cn.obcc.driver.vo.params.TokenParams;
import cn.obcc.vo.driver.BlockTxInfo;
import cn.obcc.vo.driver.TokenInfo;
import cn.obcc.config.ExProps;
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
                            IUpchainFn<BlockTxInfo> fn, ExProps config) throws Exception;

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
                                    IUpchainFn<BlockTxInfo> fn, ExProps config) throws Exception;

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


    public String balanceOf(TokenInfo token, String address, ExProps config) throws Exception;

    public String freezeOf(TokenInfo token, String address, ExProps config) throws Exception;

    public BlockTxInfo operateInfo(TokenInfo token, String hash, ExProps config) throws Exception;

    /**********************************************************************************************************/
    public String transfer(String bizId, SrcAccount account, TokenInfo token,
                                    String destAccount, ExProps config, IUpchainFn<BlockTxInfo> fn) throws Exception;

    public String freeze(String bizId, SrcAccount account, TokenInfo token,
                                  BigInteger amount, ExProps config, IUpchainFn<BlockTxInfo> fn) throws Exception;

    public String unfreeze(String bizId, SrcAccount account, TokenInfo token,
                                    BigInteger amount, ExProps config, IUpchainFn<BlockTxInfo> fn) throws Exception;

    /*********************************************************************************************/
    public String burn(String bizId, SrcAccount account, TokenInfo token,
                                BigInteger amount, ExProps config) throws Exception;

    public String supply(String bizId, SrcAccount account, TokenInfo token,
                                  BigInteger amount, ExProps config) throws Exception;

    /*************************************************************************************************************/
    public String approve(String bizId, SrcAccount account, TokenInfo token,
                                   String destAccount, BigInteger amount, ExProps config) throws Exception;

    public String transferFrom(String bizId, SrcAccount account, TokenInfo token,
                                        BigInteger amount, ExProps config) throws Exception;
    /**************************************************************************************************/

}
