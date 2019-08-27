package cn.obcc.driver.eth.module;

import cn.obcc.driver.module.base.AccountBaseHandler;
import cn.obcc.driver.module.fn.ITransferInfoFn;
import cn.obcc.driver.utils.JunctionUtils;
import cn.obcc.driver.vo.BizTransactionInfo;
import cn.obcc.driver.vo.SrcAccount;
import cn.obcc.exception.ObccException;
import cn.obcc.exception.enums.EExceptionCode;
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
import org.web3j.protocol.core.methods.response.EthGetBalance;
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
    public RetData<String> innerTranfer(String bizId, SrcAccount account, BigInteger amount, String destAddress, ReqConfig<Web3j> config, ITransferFn callback) throws Exception {
        return null;
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
