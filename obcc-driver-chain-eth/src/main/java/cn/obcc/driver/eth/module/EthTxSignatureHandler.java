package cn.obcc.driver.eth.module;

import cn.obcc.config.ExConfig;
import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.module.fn.IStateListener;
import cn.obcc.driver.tech.ITxSignatureHandler;
import cn.obcc.driver.vo.*;
import cn.obcc.driver.vo.params.SignTxParams;
import cn.obcc.driver.vo.params.TxParams;
import org.web3j.protocol.Web3j;


public class EthTxSignatureHandler extends BaseHandler<Web3j> implements ITxSignatureHandler<Web3j> {

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
