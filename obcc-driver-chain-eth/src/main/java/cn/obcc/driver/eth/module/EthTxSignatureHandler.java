package cn.obcc.driver.eth.module;

import cn.obcc.config.ExProps;
import cn.obcc.driver.base.BaseHandler;
import cn.obcc.driver.module.fn.IUpchainFn;
import cn.obcc.driver.tech.ITxSignatureHandler;
import cn.obcc.driver.vo.*;
import cn.obcc.driver.vo.params.SignTxParams;
import cn.obcc.driver.vo.params.TxParams;
import cn.obcc.vo.RetData;
import cn.obcc.vo.driver.BlockTxInfo;
import org.web3j.protocol.Web3j;


public class EthTxSignatureHandler extends BaseHandler<Web3j> implements ITxSignatureHandler<Web3j> {

    @Override
    public SignTxParams getParams(String sourceAddress, String destinationAddress, String currency, String amount, ExProps config) throws Exception {
        return null;
    }

    @Override
    public SignTxData getData(TxParams transInfo, ExProps config) throws Exception {
        return null;
    }

    @Override
    public String sendData(String sourceAddress, String signedData, ExProps config) throws Exception {
        return null;
    }

    @Override
    public String sendData(String sourceAddress, String signedData, ExProps config, IUpchainFn<BlockTxInfo> callback) throws Exception {
        return null;
    }
}
