package cn.obcc.driver.module;

import cn.obcc.driver.IChainHandler;
import cn.obcc.driver.module.fn.ITransferFn;
import cn.obcc.driver.vo.*;
import cn.obcc.config.ReqConfig;
import cn.obcc.vo.driver.AccountInfo;
import cn.obcc.vo.RetData;
import cn.obcc.vo.driver.BlockTxInfo;

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
     * @return address:钱包地址<br>
     * privateKey:钱包私钥<br>
     */
    public RetData<Account> createAccount() throws Exception;


    /**
     * @param bizId
     * @param username
     * @param pwd
     * @return
     * @throws Exception
     */
    public RetData<AccountInfo> createAccount(String bizId, String username, String pwd) throws Exception;

    /**
     * @param pipe 参数
     *             //@param config  系统配置,在这中间配置是否需要拆分
     * @return hash:交易的返回的hashs,其它参数每个链不一样,如果memo的过大，会自动拆分成多条上链。
     * @throws Exception
     */
    public RetData<String> doTransfer(ChainPipe pipe) throws Exception;

    /**
     * @param account
     * @param amount
     * @param destAddress
     * @param config
     * @return
     * @throws Exception
     */
    public Long[] calGas(SrcAccount account, String amount,
                         String destAddress, ReqConfig<T> config) throws Exception;


    /**
     * @param account
     * @param config
     * @return
     * @throws Exception
     */
    public boolean checkAccount(SrcAccount account, ReqConfig<T> config) throws Exception;

    /**
     * @param bizId
     * @param account
     * @param amount
     * @param destAddress
     * @param config
     * @param callback
     * @return
     * @throws Exception
     */
    public RetData<String> transfer(String bizId, SrcAccount account, BigInteger amount,
                                         String destAddress, ReqConfig<T> config, ITransferFn callback) throws Exception;

    /**
     * 根据 transfer返回的hash获取该次支付的相关信息;<br>
     * 多条记录进行合并
     *
     * @param bizId
     * @param config
     * @return
     * @throws Exception
     */
    public RetData<BizTxInfo> getTxByBizId(String bizId, ReqConfig<T> config) throws Exception;

    /**
     * 异步取，特别是文件上链，其可能有几千个，最好采用异步。
     *
     * @param hashs
     * @param config
     * @return
     * @throws Exception
     */
    public RetData<BizTxInfo> getTxByHashs(String hashs, ReqConfig<T> config) throws Exception;


    /**
     * 根据 transfer返回的hash获取该次支付的相关信息;<br>
     *
     * @param hash
     * @param config
     * @return
     * @throws Exception
     */
    public RetData<BlockTxInfo> getTxByHash(String hash, ReqConfig<T> config) throws Exception;


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
