package cn.obcc.driver.module;

import cn.obcc.driver.IDriverHandler;
import cn.obcc.listener.IStateListener;
import cn.obcc.driver.vo.CompileResult;
import cn.obcc.vo.contract.ContractRec;
import cn.obcc.driver.vo.FromAccount;
import cn.obcc.vo.driver.ContractInfo;
import cn.obcc.config.ExConfig;

import java.util.List;

/**
 * @author mgicode
 * @desc IContract
 * @email 546711211@qq.com
 * @date 2019年8月22日 上午10:06:43
 */
public interface IContractHandler<T> extends IDriverHandler<T> {

    /**
     * <p>
     * 动态编译指定的合约文本，并把编译成功的合约结果保存到数据库中
     * 合约文本可以包括多个合约，会形成多条合约记录
     * </p>
     *
     * @param pkgName  编译的包名，用来定位和查找合给
     * @param contract 合约的文本
     * @param config
     * @return
     * @throws Exception
     */
    public CompileResult compile(String pkgName, String contract, ExConfig config) throws Exception;


    /**
     * 根据包名和编译时生成合约名找到对应的合约
     *
     * @param pkgName      包名
     * @param contractName 合约名
     * @return
     * @throws Exception
     */
    public ContractInfo getContract(String pkgName, String contractName) throws Exception;


    /**
     * <p>
     * 部署之后从链上取得contractAddr并更新到数据库的合约记录中
     * 根据该contractAddr找到合约记录
     * </p>
     *
     * @param contractAddr
     * @return
     * @throws Exception
     */
    public ContractInfo getContract(String contractAddr) throws Exception;


    /**
     * 增加外部已经部署的合约
     *
     * @param info
     * @return
     * @throws Exception
     */
    public Boolean addContract(ContractInfo info) throws Exception;


    public String deployNoArgs(String pkgName, String contract,
                               String bizId, FromAccount fromAccount,
                               IStateListener stateListener, ExConfig exConfig) throws Exception;


    public String deployNoArgs(String pkgName, String contractName, String contract,
                               String bizId, FromAccount fromAccount, IStateListener stateListener, ExConfig exConfig) throws Exception;


    public String deploy(String pkgName, String contract,
                         String bizId, FromAccount fromAccount, List<Object> params,
                         IStateListener stateListener, ExConfig exConfig) throws Exception;

    public String deploy(String pkgName, String contractName, String contract,
                         String bizId, FromAccount fromAccount, List<Object> params,
                         IStateListener stateListener, ExConfig exConfig) throws Exception;

    public String deploy(String bizId, FromAccount fromAccount, ContractInfo contract, List<Object> params,
                         IStateListener fn, ExConfig config) throws Exception;


    /**
     * @param srcAddr
     * @param contractInfo
     * @param funcName
     * @param params
     * @param config
     * @return hexData 原始hexData，需要转换
     * @throws Exception
     */
    public String query(String srcAddr, ContractInfo contractInfo, String funcName, List<Object> params, ExConfig config) throws Exception;

    /**
     * @param srcAddr
     * @param abi
     * @param contractAddr
     * @param funcName
     * @param params
     * @param config
     * @return hexData 原始hexData，需要转换
     * @throws Exception
     */
    public String query(String srcAddr, String abi, String contractAddr, String funcName, List<Object> params, ExConfig config) throws Exception;

    /**
     * @param srcAddr
     * @param contractAddr
     * @param funcName
     * @param params
     * @param config
     * @return hexData 原始hexData，需要转换
     * @throws Exception
     */
    public String queryWithAddr(String srcAddr, String contractAddr, String funcName, List<Object> params, ExConfig config) throws Exception;

    /**
     * @param srcAddr
     * @param pkgName
     * @param contractName
     * @param funcName
     * @param params
     * @param config
     * @return hexData 原始hexData，需要转换
     * @throws Exception
     */
    public String queryWithName(String srcAddr, String pkgName, String contractName, String funcName, List<Object> params, ExConfig config) throws Exception;


    public String invoke(String bizId, ContractInfo contractInfo, FromAccount fromAccount,
                         String fnName, List<Object> params, IStateListener stateListener, ExConfig config) throws Exception;


    public String invoke(String bizId, String abi, String contractAddr, FromAccount fromAccount,
                         String fnName, List<Object> params, IStateListener stateListener, ExConfig exConfig) throws Exception;

    /**
     * 根据区块的记录解析出来其方法名和参数
     *
     * @param input
     * @return
     * @throws Exception
     */
    public ContractRec parseTxInfo(ContractInfo contractInfo, String input) throws Exception;


    //[{"outputs":[{"name":"","type":"string"}],"constant":true,"payable":false,"inputs":[],"name":"name","stateMutability":"view","type":"function"},{"outputs":[{"name":"","type":"uint256"}],"constant":true,"payable":false,"inputs":[],"name":"TOTAL_TOKENS","stateMutability":"view","type":"function"},{"outputs":[{"name":"","type":"uint256"}],"constant":true,"payable":false,"inputs":[],"name":"totalSupply","stateMutability":"view","type":"function"},{"outputs":[{"name":"","type":"uint8"}],"constant":true,"payable":false,"inputs":[],"name":"decimals","stateMutability":"view","type":"function"},{"outputs":[{"name":"balance","type":"uint256"}],"constant":true,"payable":false,"inputs":[{"name":"_owner","type":"address"}],"name":"balanceOf","stateMutability":"view","type":"function"},{"outputs":[{"name":"","type":"string"}],"constant":true,"payable":false,"inputs":[],"name":"symbol","stateMutability":"view","type":"function"},{"outputs":[{"name":"success","type":"bool"}],"constant":false,"payable":false,"inputs":[{"name":"_to","type":"address"},{"name":"_value","type":"uint256"}],"name":"transfer","stateMutability":"nonpayable","type":"function"},{"outputs":[{"name":"","type":"uint256"}],"constant":true,"payable":false,"inputs":[],"name":"ONE_TOKEN","stateMutability":"view","type":"function"},{"payable":false,"inputs":[],"stateMutability":"nonpayable","type":"constructor"},{"inputs":[{"indexed":true,"name":"_from","type":"address"},{"indexed":true,"name":"_to","type":"address"},{"indexed":false,"name":"_value","type":"uint256"}],"name":"Transfer","anonymous":false,"type":"event"}]
}
