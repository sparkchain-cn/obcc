package cn.obcc.driver.eth.module;

import cn.obcc.config.ExConfig;
import cn.obcc.contract.solc.utils.AbiFuncUtils;
import cn.obcc.driver.tech.base.BaseContractHandler;

import cn.obcc.driver.eth.module.contract.FuncEncoder;
import cn.obcc.driver.eth.module.contract.FuncDecoder;
import cn.obcc.driver.eth.module.contract.InputTypeEncoder;
import cn.obcc.driver.eth.module.contract.MethodIdEncoder;
import cn.obcc.driver.handler.IContractHandler;
import cn.obcc.listener.IStateListener;
import cn.obcc.vo.driver.ChainPipe;
import cn.obcc.vo.contract.ContractRec;
import cn.obcc.vo.driver.FromAccount;
import cn.obcc.exception.ObccException;
import cn.obcc.enums.EExceptionCode;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.vo.KeyValue;
import cn.obcc.vo.contract.FuncVo;
import cn.obcc.vo.db.ContractInfo;
import lombok.NonNull;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EthBaseContractHandler extends BaseContractHandler<Web3j> implements IContractHandler<Web3j> {


    @Override
    public ContractRec parseTxInfo(ContractInfo contractInfo, String input) throws Exception {

        //methodId->methodName
        Map<String, String> idNameMap = contractInfo.getMethodIdNameMap();
        String methodName = idNameMap.get(input.substring(0, 12));

        ContractRec rec = new ContractRec();
        rec.setMethod(methodName);

        FuncVo vo = AbiFuncUtils.getFuncVo(contractInfo.getAbi(), methodName);

        List<String> inputNames = AbiFuncUtils.getInputNames(vo);
        List<String> inputTypes = AbiFuncUtils.getInputTypes(vo);

//        List<String> inputNames = AbiFormatParser.getFunctionInputNames(contractInfo.getAbi(), methodName);
//        List<String> inputTypes = AbiFormatParser.getFunctionInputTypes(contractInfo.getAbi(), methodName);
        //name->values

        List<Class<? extends Type>> typeClsList = InputTypeEncoder.createInputTypeClzList(inputTypes);
        List<KeyValue> values = FuncDecoder.decodeParameters(input.substring(12), inputNames, typeClsList);

        rec.setParams(values);
        return rec;
    }

    @Override
    public String encodeConstructor(String abi, List<Object> params) throws Exception {
        FuncVo vo = AbiFuncUtils.getArgsVo(abi);
        List<String> types = AbiFuncUtils.getInputTypes(vo);
        // List<String> types = AbiFormatParser.getConstructorInputTypes(abi);
        //List<Type> deployParams = ContractEncoder.createInputParams(types, params);
        List<Type> deployParams = InputTypeEncoder.createInputTypeList(types, params);
        String hex = FunctionEncoder.encodeConstructor(deployParams);
        return hex;
    }


    @Override
    public String getInvokeHexData(@NonNull String abi, @NonNull String fnName, List<Object> inputValues) throws Exception {

        FuncVo vo = AbiFuncUtils.getFuncVo(abi, fnName);
        List<String> inputTypes = AbiFuncUtils.getInputTypes(vo);
        List<String> outputTypes = AbiFuncUtils.getOutPutTypes(vo);
        //  String hexData = ContractEncoder.getFnEncodeData(fnName, inputTypes, inputValues, outputTypes);
        String hexData = FuncEncoder.encodeFunc(abi, fnName, inputValues);
        return hexData;
    }


    @Override
    protected Map<String, String> buildMethodIdNameMap(String abi) {

        List<FuncVo> funcList = AbiFuncUtils.getExecFuncList(abi);
        Map<String, String> map = new HashMap<>();
        for (FuncVo vo : funcList) {
            List<String> inputTypes = AbiFuncUtils.getInputTypes(vo);
            //List<String> inputTypes = AbiFormatParser.getFunctionInputTypes(abi, vo.getName());
            String methodId = MethodIdEncoder.buildMethodId(vo.getName(), inputTypes);
            map.put(methodId, vo.getName());
        }
        return map;

    }

    @Override
    public String invoke(String bizId, ContractInfo contractInfo, FromAccount fromAccount,
                         String methodName, List<Object> params, IStateListener fn, ExConfig config) throws Exception {

        String hex = getInvokeHexData(contractInfo.getAbi(), methodName, params);
        fromAccount.setMemos(hex);
        config.setNeedHandleMemo(false);
        config.setNeedSplit(false);
        //config.setUpchainType(EUpchainType.ContractInvoke);

        ChainPipe pipe = new ChainPipe();
        pipe.getBizState().setChainCode(getDriver().getConfig().getChain().getName());
        //  pipe.setChainTxType(EChainTxType.Contract);
        pipe.getBizState().setBizId(bizId);
        pipe.setFromAccount(fromAccount);
        pipe.setAmount("0");
        pipe.setExConfig(config);
        pipe.setDestAddr(contractInfo.getContractAddr());
        pipe.setStateListener(fn);

        return getDriver().getAccountHandler().transfer(pipe);

    }


    @Override
    public String query(@NonNull String srcAddr, @NonNull String abi, @NonNull String contractAddr,
                        @NonNull String funcName, List<Object> params, ExConfig config) throws Exception {

        String hex = getInvokeHexData(abi, funcName, params);
        Transaction tc = Transaction.createEthCallTransaction(srcAddr, contractAddr, hex);
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



