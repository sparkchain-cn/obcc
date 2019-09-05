package cn.obcc.driver.eth.module;

import cn.obcc.config.ReqConfig;
import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.module.fn.ITransferFn;
import cn.obcc.driver.tech.ITxSignatureHandler;
import cn.obcc.driver.vo.*;
import cn.obcc.driver.vo.params.SignTxParams;
import cn.obcc.driver.vo.params.TxParams;
import cn.obcc.vo.RetData;
import org.web3j.protocol.Web3j;


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
