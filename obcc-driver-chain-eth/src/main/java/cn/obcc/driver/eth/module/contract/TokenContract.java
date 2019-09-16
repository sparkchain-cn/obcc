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

import static cn.obcc.driver.eth.module.contract.ContractEncoder.genFnEncodeData;

public class TokenContract {

//	// transfer(address _to, uint256 _value)
//	public static String getContractTransferData_2(String destAddr, String amount) {
//		// token转账参数即data字段
//		String methodName = "transfer";
//
//		List<Type> inputParameters = new ArrayList<>();
//
//		// 第一个参数
//		Address destAddress = new Address(destAddr);
//		inputParameters.add(destAddress);
//
//		// 第二个参数
//		BigInteger amountBInt = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();
//		Uint256 tokenValue = new Uint256(amountBInt);
//		inputParameters.add(tokenValue);
//
//		// 返回参数
//		List<TypeReference<?>> outputParameters = new ArrayList<>();
//		TypeReference<Bool> typeReference = new TypeReference<Bool>() {
//		};
//		outputParameters.add(typeReference);
//
//		// 函数
//		Function function = new Function(methodName, inputParameters, outputParameters);
//
//		// 返回hexData
//		String data = FunctionEncoder.encode(function);
//		return data;
//
//	}
//
//	// transfer(address _to, uint256 _value)
//	public static String getContractTransferData_2(String destAddr, String amount, String memos) {
//		// token转账参数即data字段
//		String methodName = "transfer";
//
//		List<Type> inputParameters = new ArrayList<>();
//
//		// 第一个参数
//		Address destAddress = new Address(destAddr);
//		inputParameters.add(destAddress);
//
//		// 第二个参数
//		BigInteger amountBInt = Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger();
//		Uint256 tokenValue = new Uint256(amountBInt);
//		inputParameters.add(tokenValue);
//
//		// 第三个参数
//		Utf8String memo = new Utf8String(memos);
//		inputParameters.add(memo);
//
//		// 返回参数
//		List<TypeReference<?>> outputParameters = new ArrayList<>();
//		TypeReference<Bool> typeReference = new TypeReference<Bool>() {
//		};
//		outputParameters.add(typeReference);
//
//		// 函数
//		Function function = new Function(methodName, inputParameters, outputParameters);
//
//		// 返回hexData
//		String data = FunctionEncoder.encode(function);
//		return data;
//
//	}
//
//	// function balanceOf(address _owner) public view returns (uint256 balance)
//
//	public static Function getContractBalanceData_2(String owner) {
//
//		// token转账参数即data字段
//		String methodName = "balanceOf";
//
//		List<Type> inputParameters = new ArrayList<>();
//		Address addr = new Address(owner);
//		inputParameters.add(addr);
//
//		// 返回参数
//		List<TypeReference<?>> outputParameters = new ArrayList<>();
//		TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
//		};
//		outputParameters.add(typeReference);
//
//		// 函数
//		Function function = new Function(methodName, inputParameters, outputParameters);
//
//		return function;
//		// 返回hexData
//		// String data = FunctionEncoder.encode(function);
//		// return data;
//
//	}
//	//endregion

	//region new methods
	public static String getContractTransferData(String destAddr, String amount) {
		List<String> inputTypes = new ArrayList<>();
		List<String> inputValues = new ArrayList<>();
		inputTypes.add("address");
		inputValues.add(destAddr);
		inputTypes.add("uint256");
		inputValues.add(Convert.toWei(amount, Convert.Unit.ETHER).toPlainString());

		List<String> outputTypes = new ArrayList<>();
		outputTypes.add("bool");

		Function function = genFnEncodeData("transfer", inputTypes, inputValues, outputTypes);
		return FunctionEncoder.encode(function);
	}

	public static String getContractTransferData(String destAddr, String amount, String memos) {
		List<String> inputTypes = new ArrayList<>();
		List<String> inputValues = new ArrayList<>();
		inputTypes.add("address");
		inputValues.add(destAddr);
		inputTypes.add("uint256");
		inputValues.add(Convert.toWei(amount, Convert.Unit.ETHER).toPlainString());
		inputTypes.add("string");
		inputValues.add(memos);

		List<String> outputTypes = new ArrayList<>();
		outputTypes.add("bool");

		Function function = genFnEncodeData("transfer", inputTypes, inputValues, outputTypes);
		return FunctionEncoder.encode(function);
	}

	public static String getContractBalanceData(String owner) {
		List<String> inputTypes = new ArrayList<>();
		List<String> inputValues = new ArrayList<>();
		inputTypes.add("address");
		inputValues.add(owner);

		List<String> outputTypes = new ArrayList<>();
		outputTypes.add("uint256");

		Function function = genFnEncodeData("balanceOf", inputTypes, inputValues, outputTypes);
		return FunctionEncoder.encode(function);
	}
	//endregion
}
