package cn.obcc.driver.eth.module;

import cn.obcc.config.ExConfig;
import cn.obcc.driver.eth.module.account.AccountTransfer;
import cn.obcc.driver.eth.module.tech.common.BlockTxInfoParser;
import cn.obcc.driver.module.IAccountHandler;
import cn.obcc.driver.module.base.BaseAccountHandler;
import cn.obcc.driver.utils.JunctionUtils;
import cn.obcc.driver.vo.Account;
import cn.obcc.driver.vo.ChainPipe;
import cn.obcc.driver.vo.FromAccount;
import cn.obcc.exception.ObccException;
import cn.obcc.exception.enums.EExceptionCode;
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


public class EthAccountHandler extends BaseAccountHandler<Web3j> implements IAccountHandler<Web3j> {

    @Override
    public Account createAccount() throws Exception {
        try {
            ECKeyPair ecKeyPair = Keys.createEcKeyPair();
            String addr = Keys.getAddress(ecKeyPair);
            Account spcAccount = new Account();
            addr = JunctionUtils.hexAddrress(addr);
            spcAccount.setAddress(addr);

            String hexPrivateKey = String.format("%040x", ecKeyPair.getPrivateKey());
            spcAccount.setSecret(hexPrivateKey);
            String hexPublicKey = String.format("%040x", ecKeyPair.getPublicKey());
            spcAccount.setPublicKey(hexPublicKey);
            return spcAccount;
        } catch (InvalidAlgorithmParameterException e) {
            throw ObccException.create(EExceptionCode.CREATE_ACCOUNT_FAIL, "InvalidAlgorithmParameter." + e);
        } catch (NoSuchAlgorithmException e) {
            throw ObccException.create(EExceptionCode.CREATE_ACCOUNT_FAIL, "NoSuchAlgorithm." + e);
        } catch (NoSuchProviderException e) {
            throw ObccException.create(EExceptionCode.CREATE_ACCOUNT_FAIL, "NoSuchProvider" + e);
        }
    }


    @Override
    public Long[] calGas(FromAccount account, String amount, String destAddress, ExConfig config) throws Exception {

        Web3j web3j = getClient();
        if (account.getGasLimit() == null) {
            account.setGasLimit(0L);
        }
        if (account.getGasPrice() == null) {
            account.setGasPrice(0L);
        }
        BigInteger gasPrice = BigInteger.valueOf(account.getGasPrice());
        if (account.getGasPrice() <= 0) {
            gasPrice = DefaultGasProvider.GAS_PRICE;
            account.setGasPrice(gasPrice.longValue());
        }

        BigInteger gasLimit = BigInteger.valueOf(account.getGasLimit());
        if (account.getGasLimit() <= 0) {
            gasLimit = DefaultGasProvider.GAS_LIMIT;
            account.setGasLimit(gasLimit.longValue());
        }

        //todo:估算gas
        return new Long[]{
                gasPrice.longValue(), gasLimit.longValue()
        };

    }

    @Override
    public boolean checkAccount(FromAccount account, ExConfig config) throws Exception {
        Credentials credentials = Credentials.create(account.getSecret());
        if (account.getSrcAddr().equalsIgnoreCase(credentials.getAddress())) {
            return true;
        }
        return false;

    }

    @Override
    public String onTransfer(ChainPipe pipe) throws Exception {
        // 把十进制的转换成ETH的Wei, 1ETH = 10^18 Wei
        BigInteger amount = Convert.toWei(pipe.getAmount(), Convert.Unit.ETHER).toBigInteger();
        EthSendTransaction est = AccountTransfer.trySendTx(pipe.getFromAccount(), amount,
                pipe.getDestAddr(), driver.getNonceCalculator(), pipe.getExConfig(), getClient());

        Response.Error err = est.getError();
        if (err != null) {
            throw ObccException.create(err.getCode() + "",
                    err.getMessage() + "", err.getData() + "");
        }

        return est.getTransactionHash();
    }


    @Override
    public BlockTxInfo getTxByHash(String hash, ExConfig config) throws Exception {
        try {
            Web3j web3j = getClient();
            Transaction tx;
            String chaincode = getObccConfig().getChain().getName();

            tx = web3j.ethGetTransactionByHash(hash).send().getTransaction().get();
            BlockTxInfo txInfo = BlockTxInfoParser.parseTxInfo(web3j, chaincode, getDriver(), tx);
            if (txInfo != null) {
                return txInfo;
            }
        } catch (Exception e) {
            throw ObccException.create(EExceptionCode.FETCH_TX_FAIL, e.getMessage());
        }
        return null;
    }


    @Override
    public String getBalance(String addr, ExConfig config) throws Exception {
        try {
            addr = JunctionUtils.hexAddrress(addr);
            Web3j web3j = getClient();
            EthGetBalance ethGetBalance = web3j.ethGetBalance(addr, DefaultBlockParameterName.LATEST).send();
            BigInteger balance = ethGetBalance.getBalance();
            BigDecimal bDecimal = Convert.fromWei(balance + "", Convert.Unit.ETHER);
            System.out.println("address " + addr + " balance " + balance + "wei");
            return bDecimal.toPlainString();
        } catch (IOException e) {
            throw ObccException.create(EExceptionCode.IO_EXCEPTION, " web3j ethGetBalance io exception." + e);

        }
    }
}
