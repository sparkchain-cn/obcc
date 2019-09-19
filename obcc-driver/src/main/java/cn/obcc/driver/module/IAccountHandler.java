package cn.obcc.driver.module;

import cn.obcc.driver.IDriverHandler;
import cn.obcc.listener.IStateListener;
import cn.obcc.driver.vo.*;
import cn.obcc.config.ExConfig;
import cn.obcc.vo.driver.AccountInfo;
import cn.obcc.vo.driver.BlockTxInfo;

/**
 * @author mgicode
 * @desc IAccount
 * @email 546711211@qq.com
 * @date 2019年8月22日 上午9:59:42
 */
public interface IAccountHandler<T> extends IDriverHandler<T> {

    /**
     * 在链上创建钱包,包括激活其钱包,<br>
     * 目前链（moac,jingtum spp)
     *
     * @return address:钱包地址<br>
     * privateKey:钱包私钥<br>
     */
    public Account createAccount() throws Exception;


    /**
     * @param bizId
     * @param username
     * @param pwd
     * @return
     * @throws Exception
     */
    public AccountInfo createAccount(String bizId, String username, String pwd, ExConfig config) throws Exception;

    /**
     * @param pipe 参数
     *             //@param config  系统配置,在这中间配置是否需要拆分
     * @return hash:交易的返回的hashs,其它参数每个链不一样,如果memo的过大，会自动拆分成多条上链。
     * @throws Exception
     */
    public String syncTransfer(ChainPipe pipe) throws Exception;

    /**
     * @param pipe
     * @return
     * @throws Exception
     */
    public String transfer(ChainPipe pipe) throws Exception;

    /**
     * @param account
     * @param amount
     * @param destAddr
     * @param config
     * @return
     * @throws Exception
     */
    public Long[] calGas(FromAccount account, String amount, String destAddr, ExConfig config) throws Exception;


    /**
     * @param account
     * @param config
     * @return
     * @throws Exception
     */
    public boolean checkAccount(FromAccount account, ExConfig config) throws Exception;


    public default String pay(String bizId, FromAccount account, String amount, String destAddr) throws Exception {
        return pay(bizId, account, amount, destAddr, null);
    }

    public default String pay(String bizId, FromAccount account, String amount, String destAddr, IStateListener callback) throws Exception {
        ExConfig config = new ExConfig();
        config.setNeedSplit(false);
        return pay(bizId, account, amount, destAddr, callback, config);
    }
    //  public String pay(String bizId, FromAccount account, String amount, String destAddr, IStateListener callback, ExConfig config) throws Exception;

    public String pay(String bizId, FromAccount account, String amount, String destAddr, IStateListener callback, ExConfig config) throws Exception;


    public default String text(String bizId, FromAccount account, String destAddr) throws Exception {
        return text(bizId, account, destAddr, null);
    }

    public default String text(String bizId, FromAccount account, String destAddr, IStateListener callback) throws Exception {
        return text(bizId, account, destAddr, callback, null);
    }


    public default String text(String bizId, FromAccount account, String destAddr, IStateListener callback, ExConfig config) throws Exception {
        return pay(bizId, account, "0", destAddr, callback, config);
    }

    /**
     * 根据 transfer返回的hash获取该次支付的相关信息;<br>
     * 多条记录进行合并
     *
     * @param bizId
     * @param config
     * @return
     * @throws Exception
     */
    public BizTxInfo getTxByBizId(String bizId, ExConfig config) throws Exception;

    /**
     * 异步取，特别是文件上链，其可能有几千个，最好采用异步。
     *
     * @param hashes
     * @param config
     * @return
     * @throws Exception
     */
    public BizTxInfo getTxByHashes(String hashes, ExConfig config) throws Exception;


    /**
     * 根据 transfer返回的hash获取该次支付的相关信息;<br>
     *
     * @param hash
     * @param config
     * @return
     * @throws Exception
     */
    public BlockTxInfo getTxByHash(String hash, ExConfig config) throws Exception;


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
    public String getBalance(String address, ExConfig config) throws Exception;

}
