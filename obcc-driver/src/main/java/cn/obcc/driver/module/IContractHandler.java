package cn.obcc.driver.module;

import cn.obcc.driver.IChainHandler;
import cn.obcc.driver.module.fn.IUpchainFn;
import cn.obcc.driver.vo.ChainPipe;
import cn.obcc.driver.vo.CompileResult;
import cn.obcc.driver.vo.ContractExecRec;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.vo.driver.BlockTxInfo;
import cn.obcc.vo.driver.ContractInfo;
import cn.obcc.config.ExProps;
import cn.obcc.vo.RetData;

import java.util.List;

/**
 * @author mgicode
 * @desc IContract
 * @email 546711211@qq.com
 * @date 2019年8月22日 上午10:06:43
 */
public interface IContractHandler<T> extends IChainHandler<T> {

    public CompileResult compile(String code, String contract, ExProps config) throws Exception;



    public ContractInfo getContract(String code, String contractName) throws Exception;

    public ContractInfo getContract(String contractAddr) throws Exception;

    public Boolean addContract(String code, ContractInfo info) throws Exception;


    public String deploy(String bizId, SrcAccount srcAccount, ContractInfo contract,
                         IUpchainFn<BlockTxInfo> fn, ExProps config, List<String> params) throws Exception;


    public String query(String srcAddr, ContractInfo contractInfo, ExProps config,
                        String methodName, List<String> params) throws Exception;


    public String invoke(String bizId, ContractInfo contractInfo,
                         SrcAccount srcAccount, ExProps config, IUpchainFn<BlockTxInfo> fn, String methodName, List<String> params) throws Exception;


    /**
     * 根据区块的记录解析出来其方法名和参数
     *
     * @param input
     * @return
     * @throws Exception
     */
    public ContractExecRec parseExecRec(ContractInfo contractInfo, String input) throws Exception;


    /**
     * 根据abi构建部署合约的的构建方法的字符串
     *
     * @param abi
     * @return
     * @throws Exception
     */
    public String encodeConstructor(String abi, List<String> params) throws Exception;


    public default String getDeployHexData(String bin, String abi, List<String> params) throws Exception {
        return bin + encodeConstructor(abi, params);
    }

    public String getInvokeHexData(String abi, String fnName, List<String> inputValues) throws Exception;

    //[{"outputs":[{"name":"","type":"string"}],"constant":true,"payable":false,"inputs":[],"name":"name","stateMutability":"view","type":"function"},{"outputs":[{"name":"","type":"uint256"}],"constant":true,"payable":false,"inputs":[],"name":"TOTAL_TOKENS","stateMutability":"view","type":"function"},{"outputs":[{"name":"","type":"uint256"}],"constant":true,"payable":false,"inputs":[],"name":"totalSupply","stateMutability":"view","type":"function"},{"outputs":[{"name":"","type":"uint8"}],"constant":true,"payable":false,"inputs":[],"name":"decimals","stateMutability":"view","type":"function"},{"outputs":[{"name":"balance","type":"uint256"}],"constant":true,"payable":false,"inputs":[{"name":"_owner","type":"address"}],"name":"balanceOf","stateMutability":"view","type":"function"},{"outputs":[{"name":"","type":"string"}],"constant":true,"payable":false,"inputs":[],"name":"symbol","stateMutability":"view","type":"function"},{"outputs":[{"name":"success","type":"bool"}],"constant":false,"payable":false,"inputs":[{"name":"_to","type":"address"},{"name":"_value","type":"uint256"}],"name":"transfer","stateMutability":"nonpayable","type":"function"},{"outputs":[{"name":"","type":"uint256"}],"constant":true,"payable":false,"inputs":[],"name":"ONE_TOKEN","stateMutability":"view","type":"function"},{"payable":false,"inputs":[],"stateMutability":"nonpayable","type":"constructor"},{"inputs":[{"indexed":true,"name":"_from","type":"address"},{"indexed":true,"name":"_to","type":"address"},{"indexed":false,"name":"_value","type":"uint256"}],"name":"Transfer","anonymous":false,"type":"event"}]
}
