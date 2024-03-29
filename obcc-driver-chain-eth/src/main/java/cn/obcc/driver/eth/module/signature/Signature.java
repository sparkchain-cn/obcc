package cn.obcc.driver.eth.module.signature;

import cn.obcc.vo.driver.SignTxParams;
import cn.obcc.config.ExConfig;
import cn.obcc.vo.RetData;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;

public class Signature {

    // region offline sign
    public RetData<SignTxParams> getSignParams(String address, ExConfig config) throws Exception {
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

        BigInteger gasPriceBint = BigInteger.valueOf(gasPrice);
        if (gasPrice <= 0) {
            gasPriceBint = DefaultGasProvider.GAS_PRICE;

        }

        BigInteger gasLimitBint = BigInteger.valueOf(gasLimit);
        if (gasLimit <= 0) {
            gasLimitBint = DefaultGasProvider.GAS_LIMIT;

        }

        return new StaticGasProvider(gasPriceBint, gasLimitBint);

    }
    // endregion
}
