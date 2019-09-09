package cn.obcc.driver.eth.module;

import cn.obcc.config.ExProps;
import cn.obcc.driver.contract.ContractHandler;
import cn.obcc.driver.contract.compile.ContractCompiler;
import cn.obcc.driver.contract.solc.core.AbiParser;
import cn.obcc.driver.eth.module.contract.ContractEncoder;
import cn.obcc.driver.module.IContractHandler;
import cn.obcc.driver.module.fn.IUpchainFn;
import cn.obcc.driver.vo.ChainPipe;
import cn.obcc.driver.vo.ContractExecRec;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.exception.ObccException;
import cn.obcc.exception.enums.EChainTxType;
import cn.obcc.exception.enums.EExceptionCode;
import cn.obcc.exception.enums.ETransferStatus;
import cn.obcc.exception.enums.EUpchainType;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.uuid.UuidUtils;
import cn.obcc.vo.RetData;
import cn.obcc.vo.driver.BlockTxInfo;
import cn.obcc.vo.driver.ContractInfo;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class EthContractHandler extends ContractHandler<Web3j> implements IContractHandler<Web3j> {


    @Override
    public ContractExecRec parseExecRec(ContractInfo contractInfo, String input) throws Exception {
        return null;
    }

    @Override
    public String encodeConstructor(String abi, List<String> params) throws Exception {
        List<String> types = AbiParser.getConstructorInputTypes(abi);
        List<Type> deployParams = ContractEncoder.genInputParams(types, params);
        String hex = FunctionEncoder.encodeConstructor(deployParams);
        return hex;
    }


    @Override
    public String getInvokeHexData(String abi, String fnName, List<String> inputValues) throws Exception {

        List<String> inputTypes = AbiParser.getFunctionInputTypes(abi, fnName);
        List<String> outputTypes = AbiParser.getFunctionOutputTypes(abi, fnName);
        String hexData = ContractEncoder.getFnEncodeData(fnName, inputTypes, inputValues, outputTypes);

        return hexData;
    }


    @Override
    public String invoke(String bizId, ContractInfo contractInfo,
                         SrcAccount srcAccount, ExProps config, IUpchainFn<BlockTxInfo> fn, String methodName, List<String> params) throws Exception {

        try {
            String hex = getInvokeHexData(contractInfo.getAbi(), methodName, params);
            srcAccount.setMemos(hex);
            config.setNeedHandleMemo(false);
            config.setNeedSplit(false);
            config.setUpchainType(EUpchainType.ContractInvoke);

            ChainPipe pipe = new ChainPipe();
            pipe.setChainCode(getDriver().getObccConfig().getChain().getName());
            pipe.setChainTxType(EChainTxType.Contract);
            pipe.setBizId(bizId);
            pipe.setSrcAccount(srcAccount);
            pipe.setAmount("0");
            pipe.setConfig(config);
            pipe.setDestAddr(contractInfo.getContractAddr());
            pipe.setCallbackFn(fn);

            getDriver().getAccountHandler().transfer(pipe);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return UuidUtils.get() + "";
    }


    @Override
    public String query(String srcAddr, ContractInfo contractInfo, ExProps config,
                        String methodName, List<String> params) throws Exception {

        String hex = getInvokeHexData(contractInfo.getAbi(), methodName, params);

        Transaction tc = Transaction.createEthCallTransaction(srcAddr, contractInfo.getContractAddr(), hex);

        EthCall ethCall = null;
        try {
            ethCall = getClient().ethCall(tc, DefaultBlockParameterName.LATEST).send();
        } catch (IOException e) {
            throw ObccException.create(EExceptionCode.IO_EXCEPTION, "");
        }
        String value = ethCall.getValue();
        if (StringUtils.isNullOrEmpty(value)) {
            throw ObccException.create(EExceptionCode.RETURN_NULL_OR_EMPTY, "chain3j get erc20 balance return null or empty.");
        }
        return value;
    }
}



