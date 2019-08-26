package cn.obcc.driver.eth.module.tech;

import org.web3j.protocol.Web3j;

import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.module.fn.ITransferFn;
import cn.obcc.driver.tech.ITxSignature;
import cn.obcc.driver.vo.SignTxParams;
import cn.obcc.driver.vo.TxParams;
import cn.obcc.driver.vo.SignTxData;
import cn.obcc.config.ReqConfig;
import cn.obcc.vo.RetData;

public class EthTxsignature extends BaseHandler<Web3j> implements ITxSignature<Web3j> {


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
