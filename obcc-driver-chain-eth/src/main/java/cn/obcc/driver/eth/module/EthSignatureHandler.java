package cn.obcc.driver.eth.module;

import cn.obcc.config.ExConfig;
import cn.obcc.driver.base.BaseDriverHandler;
import cn.obcc.listener.IStateListener;
import cn.obcc.driver.handler.ISignatureHandler;
import cn.obcc.vo.driver.SignTxParams;
import cn.obcc.vo.driver.TxParams;
import cn.obcc.vo.driver.SignTxData;
import org.web3j.protocol.Web3j;


public class EthSignatureHandler extends BaseDriverHandler<Web3j> implements ISignatureHandler<Web3j> {

    @Override
    public SignTxParams getParams(String sourceAddress, String destinationAddress, String currency, String amount, ExConfig config) throws Exception {
        return null;
    }

    @Override
    public SignTxData getData(TxParams transInfo, ExConfig config) throws Exception {
        return null;
    }

    @Override
    public String sendData(String sourceAddress, String signedData, ExConfig config) throws Exception {
        return null;
    }

    @Override
    public String sendData(String sourceAddress, String signedData, ExConfig config, IStateListener callback) throws Exception {
        return null;
    }
}
