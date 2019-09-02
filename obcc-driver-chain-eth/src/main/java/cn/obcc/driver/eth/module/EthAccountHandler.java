package cn.obcc.driver.eth.module;

import cn.obcc.driver.eth.module.account.AccountTransfer;
import cn.obcc.driver.eth.utils.EthUtils;
import cn.obcc.driver.module.base.AccountBaseHandler;
import cn.obcc.driver.module.fn.ITransferInfoFn;
import cn.obcc.driver.utils.JunctionUtils;
import cn.obcc.driver.vo.BizTransactionInfo;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.exception.ObccException;
import cn.obcc.exception.enums.EExceptionCode;
import cn.obcc.utils.base.StringUtils;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.protocol.Web3j;

import cn.obcc.driver.module.fn.ITransferFn;
import cn.obcc.driver.module.IAccountHandler;
import cn.obcc.driver.vo.Account;
import cn.obcc.driver.vo.TransactionInfo;
import cn.obcc.config.ReqConfig;
import cn.obcc.vo.RetData;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;


public class EthAccountHandler extends AccountBaseHandler<Web3j> implements IAccountHandler<Web3j> {

    @Override
    public RetData<Account> createAccount() throws Exception {
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
            return RetData.succuess(spcAccount);
        } catch (InvalidAlgorithmParameterException e) {
            throw ObccException.create(EExceptionCode.CREATE_ACCOUNT_FAIL, "InvalidAlgorithmParameter." + e);
        } catch (NoSuchAlgorithmException e) {
            throw ObccException.create(EExceptionCode.CREATE_ACCOUNT_FAIL, "NoSuchAlgorithm." + e);
        } catch (NoSuchProviderException e) {
            throw ObccException.create(EExceptionCode.CREATE_ACCOUNT_FAIL, "NoSuchProvider" + e);
        }
    }

    @Override
    public Long[] calGas(SrcAccount account, String amount, String destAddress, ReqConfig<Web3j> config) throws Exception {

        Web3j web3j = config.getClient();
        if (account.getGasLimit() == null) {
            account.setGasLimit(0L);
        }
        if (account.getGasPrice() == null) {
            account.setGasPrice(0L);
        }

        BigInteger gasPriceBInt = BigInteger.valueOf(account.getGasPrice());
        if (account.getGasPrice() <= 0) {
            gasPriceBInt = DefaultGasProvider.GAS_PRICE;
            account.setGasPrice(gasPriceBInt.longValue());

        }

        BigInteger gasLimitBInt = BigInteger.valueOf(account.getGasLimit());
        if (account.getGasLimit() <= 0) {
            gasLimitBInt = DefaultGasProvider.GAS_LIMIT;
            account.setGasLimit(gasLimitBInt.longValue());
        }

//        if (!EthUtils.isNgDigit(gasTwo.getGasPrice()) || !EthUtils.isNgDigit(gasTwo.getGasLimit())) {
//            Transaction tran = new Transaction(account.getAccount(), null,
//                    null, null, destAddress, BigInteger.valueOf(0), account.getMemos());
//
//            // 不是必须的 可以使用默认值
//            BigInteger gasLimit = EthUtils.getGasLimit(web3j, tran);
//            gasLimit = BigInteger.valueOf(gasLimit.longValue() * EthConstants.SafeFactor);
//
//            BigInteger gasPrice = EthUtils.getGasPrice(web3j);
//            return new StaticGasProvider(gasPrice, gasLimit);
//
//        }
        return new Long[]{
                gasPriceBInt.longValue(), gasLimitBInt.longValue()
        };

    }

    public boolean checkAccount(SrcAccount account, ReqConfig<Web3j> config) throws Exception {
        Credentials credentials = Credentials.create(account.getSecret());
        if (account.getAccount().equalsIgnoreCase(credentials.getAddress())) {
            return true;
        }
        return false;

    }

    @Override
    public RetData<String> onTransfer(String bizId, SrcAccount account, String amount, String destAddress,
                                      ReqConfig<Web3j> config, ITransferFn callback) throws Exception {

        Web3j web3j = config.getClient();
        // 把十进制的转换成ETH的Wei, 1ETH = 10^18 Wei
        BigInteger amountBInt = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();
        EthSendTransaction est = AccountTransfer.trySendTx(
                account, amountBInt, destAddress, driver.getNonceCalculator(), config);

        Response.Error err = est.getError();
        if (err != null) {
            throw ObccException.create(err.getCode() + "",
                    err.getMessage() + "", err.getData() + "");
        }

        return RetData.succuess(est.getTransactionHash());
    }

    @Override
    public RetData<BizTransactionInfo> getTransaction(String hash, ReqConfig<Web3j> config) throws Exception {
        return null;
    }

    @Override
    public RetData<String> getTransaction(String bizId, ReqConfig<Web3j> config, ITransferInfoFn fn) throws Exception {
        return null;
    }

    @Override
    public RetData<TransactionInfo> getTransactionByHash(String hash, ReqConfig<Web3j> config) throws Exception {
        return null;
    }

    @Override
    public RetData<String> getBalance(String addr, ReqConfig<Web3j> config) throws Exception {
        try {
            addr = JunctionUtils.hexAddrress(addr);
            Web3j web3j = config.getClient();
            EthGetBalance ethGetBalance = web3j.ethGetBalance(addr, DefaultBlockParameterName.LATEST).send();
            BigInteger balance = ethGetBalance.getBalance();
            BigDecimal bDecimal = Convert.fromWei(balance + "", Convert.Unit.ETHER);
            System.out.println("address " + addr + " balance " + balance + "wei");
            return RetData.succuess(bDecimal.toPlainString());
        } catch (IOException e) {
            throw ObccException.create(EExceptionCode.IO_EXCEPTION, " web3j ethGetBalance io exception." + e);

        }
    }
}
