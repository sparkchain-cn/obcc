package cn.obcc.driver.eth.module.signature;

import cn.obcc.driver.vo.params.SignTxParams;
import cn.obcc.config.ExProps;
import cn.obcc.vo.RetData;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;

public class Signature {

    // region offline sign
    public RetData<SignTxParams> getSignParams(String address, ExProps config) throws Exception {
        SignTxParams params = new SignTxParams();
        params.setSourceAddress(address);

        StaticGasProvider gasProvider = createGasProvider(0L, 0L); // give out default gas price and limit
        params.setGasPrice(gasProvider.getGasPrice().toString());
        params.setGasLimit(gasProvider.getGasLimit().toString());

//	        BigInteger currentNonce = nonceService.computeOfflineNonce(address, config);
        Long currentNonce = null;// ((BcNonce) nonceService.getNonce(address, config).getData()).getChainNonce();
        params.setNonce(currentNonce.toString());
        return RetData.succuess(params);
    }


    public static StaticGasProvider createGasProvider(Long gasPrice, Long gasLimit) {
        if (gasLimit == null) {
            gasLimit = 0L;
        }
        if (gasPrice == null) {
            gasPrice = 0L;
        }

        BigInteger gasPriceBInt = BigInteger.valueOf(gasPrice);
        if (gasPrice <= 0) {
            gasPriceBInt = DefaultGasProvider.GAS_PRICE;

        }

        BigInteger gasLimitBInt = BigInteger.valueOf(gasLimit);
        if (gasLimit <= 0) {
            gasLimitBInt = DefaultGasProvider.GAS_LIMIT;

        }

        return new StaticGasProvider(gasPriceBInt, gasLimitBInt);

    }
    // endregion
}
