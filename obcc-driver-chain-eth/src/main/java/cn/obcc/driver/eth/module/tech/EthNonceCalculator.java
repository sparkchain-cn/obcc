package cn.obcc.driver.eth.module.tech;

import cn.obcc.config.ReqConfig;
import cn.obcc.driver.eth.utils.EthUtils;
import cn.obcc.driver.nonce.NonceCalculator;
import cn.obcc.driver.tech.INonceCalculator;
import cn.obcc.vo.RetData;
import org.web3j.protocol.Web3j;

import java.math.BigInteger;

public class EthNonceCalculator extends NonceCalculator<Web3j> implements INonceCalculator<Web3j> {
    @Override
    public RetData<Long> getNonceFromChain(String address) throws Exception {

        BigInteger nonce = EthUtils.getTransactionNonce(getClient(), address);
        if (nonce == null) {
            return RetData.error("get nonce is null!");
        }
        return RetData.succuess(nonce.longValue());
    }

}
