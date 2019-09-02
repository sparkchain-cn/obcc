package cn.obcc.driver.module;

import cn.obcc.driver.IChainHandler;
import cn.obcc.driver.module.fn.IContractCompileFn;
import cn.obcc.driver.module.fn.IContractDeployFn;
import cn.obcc.driver.module.fn.IContractInvokeFn;
import cn.obcc.driver.vo.ContractCompile;
import cn.obcc.driver.vo.ContractExecRec;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.vo.driver.ContractInfo;
import cn.obcc.config.ReqConfig;
import cn.obcc.vo.RetData;
import cn.obcc.vo.driver.TokenInfo;

/**
 * @author mgicode
 * @desc IContract
 * @email 546711211@qq.com
 * @date 2019年8月22日 上午10:06:43
 */
public interface IContractHandler<T> extends IChainHandler<T> {

    public RetData<ContractCompile> compile(String bizId, String contract, ReqConfig<T> config) throws Exception;

    public void compile(String bizId, String contract, IContractCompileFn fn, ReqConfig<T> config) throws Exception;


    public ContractInfo getContract(String bizId,String contractName) throws Exception;


    public ContractInfo getContract(String contractAddr) throws Exception;

    public RetData<Boolean> addContract(String bizId, ContractInfo params) throws Exception;

    /**
     * 返回区块链的hash
     *
     * @param bizId
     * @param srcAccount
     * @param contract
     * @param fn
     * @param config
     * @return
     * @throws Exception
     */
    public RetData<String> deploy(String bizId, SrcAccount srcAccount, ContractInfo contract,
                                  IContractDeployFn fn, ReqConfig<T> config) throws Exception;

    /**
     * @param bizId
     * @param srcAccount
     * @param contract
     * @param fn
     * @param config
     * @return
     * @throws Exception
     */
    public RetData<String> deploy(String bizId, SrcAccount srcAccount, String contract,  String name,
                                  IContractDeployFn fn, ReqConfig<T> config) throws Exception;


    public RetData<Object> query(ContractInfo contractInfo, ReqConfig<T> config, String methodName, Object... params) throws Exception;


    public RetData<String> invoke(String bizId, ContractInfo contractInfo, SrcAccount srcAccount, ReqConfig<T> config,
                                  IContractInvokeFn fn, Object... params) throws Exception;


    /**
     * 根据区块的记录解析出来其方法名和参数
     * @param input
     * @return
     * @throws Exception
     */
    public ContractExecRec parseExecRec(ContractInfo contractInfo, String input) throws Exception;

    //[{"outputs":[{"name":"","type":"string"}],"constant":true,"payable":false,"inputs":[],"name":"name","stateMutability":"view","type":"function"},{"outputs":[{"name":"","type":"uint256"}],"constant":true,"payable":false,"inputs":[],"name":"TOTAL_TOKENS","stateMutability":"view","type":"function"},{"outputs":[{"name":"","type":"uint256"}],"constant":true,"payable":false,"inputs":[],"name":"totalSupply","stateMutability":"view","type":"function"},{"outputs":[{"name":"","type":"uint8"}],"constant":true,"payable":false,"inputs":[],"name":"decimals","stateMutability":"view","type":"function"},{"outputs":[{"name":"balance","type":"uint256"}],"constant":true,"payable":false,"inputs":[{"name":"_owner","type":"address"}],"name":"balanceOf","stateMutability":"view","type":"function"},{"outputs":[{"name":"","type":"string"}],"constant":true,"payable":false,"inputs":[],"name":"symbol","stateMutability":"view","type":"function"},{"outputs":[{"name":"success","type":"bool"}],"constant":false,"payable":false,"inputs":[{"name":"_to","type":"address"},{"name":"_value","type":"uint256"}],"name":"transfer","stateMutability":"nonpayable","type":"function"},{"outputs":[{"name":"","type":"uint256"}],"constant":true,"payable":false,"inputs":[],"name":"ONE_TOKEN","stateMutability":"view","type":"function"},{"payable":false,"inputs":[],"stateMutability":"nonpayable","type":"constructor"},{"inputs":[{"indexed":true,"name":"_from","type":"address"},{"indexed":true,"name":"_to","type":"address"},{"indexed":false,"name":"_value","type":"uint256"}],"name":"Transfer","anonymous":false,"type":"event"}]
}
