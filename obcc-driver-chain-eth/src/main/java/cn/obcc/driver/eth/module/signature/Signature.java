package cn.obcc.driver.eth.module.signature;

import cn.obcc.driver.eth.utils.EthUtils;
import cn.obcc.driver.vo.SignTxParams;
import cn.obcc.config.ReqConfig;
import cn.obcc.vo.RetData;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.StaticGasProvider;

public class Signature {

    // region offline sign
    public RetData<SignTxParams> getSignParams(String address, ReqConfig<Web3j> config) throws Exception {
        SignTxParams params = new SignTxParams();
        params.setSourceAddress(address);

        StaticGasProvider gasProvider = EthUtils.createGasProvider(0L, 0L); // give out default gas price and limit
        params.setGasPrice(gasProvider.getGasPrice().toString());
        params.setGasLimit(gasProvider.getGasLimit().toString());

//	        BigInteger currentNonce = nonceService.computeOfflineNonce(address, config);
        Long currentNonce = null;// ((BcNonce) nonceService.getNonce(address, config).getData()).getChainNonce();
        params.setNonce(currentNonce.toString());
        return RetData.succuess(params);
    }
    // endregion
}
