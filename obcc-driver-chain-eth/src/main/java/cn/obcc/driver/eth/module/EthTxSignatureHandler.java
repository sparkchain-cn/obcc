package cn.obcc.driver.eth.module;

import cn.obcc.config.ReqConfig;
import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.eth.module.account.AccountTransfer;
import cn.obcc.driver.eth.module.tech.common.BlockTxInfoParser;
import cn.obcc.driver.module.IAccountHandler;
import cn.obcc.driver.module.base.AccountBaseHandler;
import cn.obcc.driver.module.fn.ITransferFn;
import cn.obcc.driver.tech.ITxSignatureHandler;
import cn.obcc.driver.utils.JunctionUtils;
import cn.obcc.driver.vo.*;
import cn.obcc.exception.ObccException;
import cn.obcc.exception.enums.EExceptionCode;
import cn.obcc.vo.RetData;
import cn.obcc.vo.driver.BlockTxInfo;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;


public class EthTxSignatureHandler extends BaseHandler<Web3j> implements ITxSignatureHandler<Web3j> {

    @Override
    public RetData<SignTxParams> getParams(String sourceAddress, String destinationAddress, String currency, String amount, ReqConfig<Web3j> config) throws Exception {
        return null;
    }

    @Override
    public RetData<SignTxData> getData(TxParams transInfo, ReqConfig config) throws Exception {
        return null;
    }

    @Override
    public RetData<String> sendData(String sourceAddress, String signedData, ReqConfig config) throws Exception {
        return null;
    }

    @Override
    public RetData<String> sendData(String sourceAddress, String signedData, ReqConfig config, ITransferFn callback) throws Exception {
        return null;
    }
}
