package cn.obcc.driver.module;

import cn.obcc.driver.IChainHandler;
import cn.obcc.driver.module.fn.IContractDeployFn;
import cn.obcc.driver.module.fn.IContractInvokeFn;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.driver.vo.ContractInfo;
import cn.obcc.exception.enums.EContractType;
import cn.obcc.config.ReqConfig;
import cn.obcc.vo.RetData;

/**
 * @author mgicode
 * @desc IContract
 * @email 546711211@qq.com
 * @date 2019年8月22日 上午10:06:43
 */
public interface IContractHandler<T> extends IChainHandler<T> {

    public RetData<ContractInfo> compile(String contract, ReqConfig<T> config) throws Exception;

    public RetData<ContractInfo> compile(String contract, EContractType type,
                                         String version, ReqConfig<T> config) throws Exception;

    public RetData<String> deploy(String bizId, SrcAccount srcAccount, ContractInfo contract,
                                  IContractDeployFn fn, ReqConfig<T> config) throws Exception;

    public RetData<String> deploy(String bizId, SrcAccount srcAccount, String contract,
                                  IContractDeployFn fn, ReqConfig<T> config) throws Exception;


    public RetData<Object> query(ReqConfig<T> config, Object... params) throws Exception;


    public RetData<String> invoke(String bizId, SrcAccount srcAccount, ReqConfig<T> config,
                                  IContractInvokeFn fn, Object... params) throws Exception;


    //[{"outputs":[{"name":"","type":"string"}],"constant":true,"payable":false,"inputs":[],"name":"name","stateMutability":"view","type":"function"},{"outputs":[{"name":"","type":"uint256"}],"constant":true,"payable":false,"inputs":[],"name":"TOTAL_TOKENS","stateMutability":"view","type":"function"},{"outputs":[{"name":"","type":"uint256"}],"constant":true,"payable":false,"inputs":[],"name":"totalSupply","stateMutability":"view","type":"function"},{"outputs":[{"name":"","type":"uint8"}],"constant":true,"payable":false,"inputs":[],"name":"decimals","stateMutability":"view","type":"function"},{"outputs":[{"name":"balance","type":"uint256"}],"constant":true,"payable":false,"inputs":[{"name":"_owner","type":"address"}],"name":"balanceOf","stateMutability":"view","type":"function"},{"outputs":[{"name":"","type":"string"}],"constant":true,"payable":false,"inputs":[],"name":"symbol","stateMutability":"view","type":"function"},{"outputs":[{"name":"success","type":"bool"}],"constant":false,"payable":false,"inputs":[{"name":"_to","type":"address"},{"name":"_value","type":"uint256"}],"name":"transfer","stateMutability":"nonpayable","type":"function"},{"outputs":[{"name":"","type":"uint256"}],"constant":true,"payable":false,"inputs":[],"name":"ONE_TOKEN","stateMutability":"view","type":"function"},{"payable":false,"inputs":[],"stateMutability":"nonpayable","type":"constructor"},{"inputs":[{"indexed":true,"name":"_from","type":"address"},{"indexed":true,"name":"_to","type":"address"},{"indexed":false,"name":"_value","type":"uint256"}],"name":"Transfer","anonymous":false,"type":"event"}]
}
