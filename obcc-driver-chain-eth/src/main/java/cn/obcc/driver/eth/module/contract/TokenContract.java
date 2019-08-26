package cn.obcc.driver.eth.module.contract;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.utils.Convert;

public class TokenContract {

//	以太坊普通交易参数
//	to //对方地址
//	gasLimit //gas上限
//	gasPrice //gas价格
//	value //eth转账数量(单位 wei)
//	data //0x 普通转账用不到这个字段
//
//	以太坊合约调用交易参数
//	to //合约地址
//	gasLimit //gas上限
//	gasPrice //gas价格
//	value //eth转账数量(单位 wei)
//	data //0x***  合约方法ABI及参数
//	--------------------- 

	// https://blog.csdn.net/u011181222/article/details/81281029

	// transfer(address _to, uint256 _value)
	public static String getContractTransferData(String destAddr, String amount) {
		// token转账参数即data字段
		String methodName = "transfer";

		List<Type> inputParameters = new ArrayList<>();

		// 第一个参数
		Address destAddress = new Address(destAddr);
		inputParameters.add(destAddress);

		// 第二个参数
		BigInteger amountBInt = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();
		Uint256 tokenValue = new Uint256(amountBInt);
		inputParameters.add(tokenValue);

		// 返回参数
		List<TypeReference<?>> outputParameters = new ArrayList<>();
		TypeReference<Bool> typeReference = new TypeReference<Bool>() {
		};
		outputParameters.add(typeReference);

		// 函数
		Function function = new Function(methodName, inputParameters, outputParameters);

		// 返回hexData
		String data = FunctionEncoder.encode(function);
		return data;

	}

	// transfer(address _to, uint256 _value)
	public static String getContractTransferData(String destAddr, String amount, String memos) {
		// token转账参数即data字段
		String methodName = "transfer";

		List<Type> inputParameters = new ArrayList<>();

		// 第一个参数
		Address destAddress = new Address(destAddr);
		inputParameters.add(destAddress);

		// 第二个参数
		BigInteger amountBInt = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();
		Uint256 tokenValue = new Uint256(amountBInt);
		inputParameters.add(tokenValue);

		// 第三个参数
		Utf8String memo = new Utf8String(memos);
		inputParameters.add(memo);

		// 返回参数
		List<TypeReference<?>> outputParameters = new ArrayList<>();
		TypeReference<Bool> typeReference = new TypeReference<Bool>() {
		};
		outputParameters.add(typeReference);

		// 函数
		Function function = new Function(methodName, inputParameters, outputParameters);

		// 返回hexData
		String data = FunctionEncoder.encode(function);
		return data;

	}

	// function balanceOf(address _owner) public view returns (uint256 balance)

	public static Function getContractBalanceData(String owner) {

		// token转账参数即data字段
		String methodName = "balanceOf";

		List<Type> inputParameters = new ArrayList<>();
		Address addr = new Address(owner);
		inputParameters.add(addr);

		// 返回参数
		List<TypeReference<?>> outputParameters = new ArrayList<>();
		TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
		};
		outputParameters.add(typeReference);

		// 函数
		Function function = new Function(methodName, inputParameters, outputParameters);

		return function;
		// 返回hexData
		// String data = FunctionEncoder.encode(function);
		// return data;

	}
//	public void invoke(){
//        //token转账参数即data字段
//        String methodName = "transfer";
//        List<Type> inputParameters = new ArrayList<>();
//        List<TypeReference<?>> outputParameters = new ArrayList<>();
//        Address tAddress = new Address(toAddress);
//        Uint256 tokenValue = new Uint256(BigDecimal.valueOf(amount).multiply(BigDecimal.TEN.pow(decimals)).toBigInteger());
//        inputParameters.add(tAddress);
//        inputParameters.add(tokenValue);
//        TypeReference<Bool> typeReference = new TypeReference<Bool>() {
//        };
//        outputParameters.add(typeReference);
//        Function function = new Function(methodName, inputParameters, outputParameters);
//        String data = FunctionEncoder.encode(function);
//
//
//        RawTransaction rawTransaction = RawTransaction.createTransaction(
//                nonce,
//                gasPrice,
//                gasLimit,
//                to,
//                value,
//                data);
//        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(privateKey, 16));
//        Credentials credentials = Credentials.create(ecKeyPair);
//        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials;
//        String hexValue = Numeric.toHexString(signedMessage);
//        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(signedData).send();
//        System.out.println(ethSendTransaction.getTransactionHash());

//    }

}
