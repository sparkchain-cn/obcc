package cn.obcc.driver.module;

import cn.obcc.driver.IChainHandler;
import cn.obcc.driver.module.fn.ITransferFn;
import cn.obcc.driver.vo.Account;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.driver.vo.TransactionInfo;
import cn.obcc.config.ReqConfig;
import cn.obcc.vo.RetData;

import java.math.BigInteger;

/**
 * @author mgicode
 * @desc IAccount
 * @email 546711211@qq.com
 * @date 2019年8月22日 上午9:59:42
 */
public interface IAccountHandler<T> extends IChainHandler<T> {

    /**
     * 在链上创建钱包,包括激活其钱包,<br>
     * 目前链（moac,jingtum spp)
     *
     * @param config 系统统一的配置<br>
     * @return address:钱包地址<br>
     * privateKey:钱包私钥<br>
     */
    public RetData<Account> createAccount(ReqConfig<T> config) throws Exception;


    /**
     * @param account 参数
     * @param config  系统配置
     * @return hash:交易的返回的hash,其它参数每个链不一样
     * @throws Exception
     */
    public RetData<String> transfer(String bizId, SrcAccount account, BigInteger amount,
                                    String destAddress, ReqConfig<T> config, ITransferFn callback) throws Exception;

    public RetData<String> asyncTransfer(String bizId, SrcAccount account, BigInteger amount,
                                    String destAddress, ReqConfig<T> config, ITransferFn callback) throws Exception;
    /**
     * 根据 transfer返回的hash获取该次支付的相关信息;<br>
     *
     * @param hash
     * @param config
     * @return
     * @throws Exception
     */
    public RetData<TransactionInfo> getTransaction(String hash, ReqConfig<T> config) throws Exception;

    /**
     * 获取指定钱包中原生币的余额 <br>
     * moac :contractAddress,abi(原生币不需要，erc20需要）
     *
     * @param address 钱包地址
     * @param config  系统统一的配置<br>
     * @return balance:String 余额<br>
     * freezed:String 冻结金额<br>
     * currency:String 货币名称<br>
     */
    public RetData<String> getBalance(String address, ReqConfig<T> config) throws Exception;

}
