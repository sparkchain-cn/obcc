//package cn.obcc.driver.eth.module.account;
//
//import java.io.IOException;
//
//import org.web3j.protocol.Web3j;
//import org.web3j.protocol.core.methods.response.EthSendTransaction;
//
//import cn.obcc.config.constant.EthConstants;
//import cn.obcc.driver.eth.utils.EthUtils;
//import cn.obcc.driver.eth.utils.TransactionType;
//import cn.obcc.driver.vo.TxParams;
//import cn.obcc.driver.vo.SignTxData;
//import cn.obcc.exception.ObccException;
//import cn.obcc.exception.enums.EExceptionCode;
//import cn.obcc.utils.base.StringUtils;
//import cn.obcc.config.ReqConfig;
//import cn.obcc.vo.RetData;
//
//public class Transfer {
//
//	public RetData<String> transfer(TxParams transInfo, ReqConfig<Web3j> config) throws Exception {
//		preProcessTransInfo(transInfo, config);
//		return call(transInfo, config);
//	}
//
//	protected void preProcessTransInfo(TxParams transInfo, ReqConfig<Web3j> config) throws Exception {
//		String token = transInfo.getToken();
//		if (token.equalsIgnoreCase(EthConstants.ETH_TOKEN)) {
//			transInfo.getParams().put(EthConstants.PARAM_TRANSACTION_TYPE, TransactionType.MAINCHAIN_TRANSFER);
//			transInfo.setPrecision(EthConstants.DEFAULT_PRECISION);
//		} else {
//			SpcTokenConfig tokenConfig = EthPoolClient.getTokenConfig(config.getChainCode(), token);
//			if (tokenConfig != null) {
//				transInfo.getParams().put(EthConstants.PARAM_TRANSACTION_TYPE, TransactionType.MAINCHAIN_CALL);
//				String contractAddress = tokenConfig.getContractAddress();
//				int decimal = tokenConfig.getPrecisions();
//				transInfo.setIssuer(contractAddress);
//				transInfo.setPrecision(decimal);
//			} else {
//				String msg = "Token " + token + " doesn't exist!";
//				throw ObccException.create(EExceptionCode.IO_EXCEPTION, msg);
//			}
//		}
//		transInfo.getParams().put(EthConstants.CONFIG_CONTRACT_METHOD, EthConstants.CONTRACT_METHOD_TRANSFER);
//	}
//
//	public RetData<String> call(TxParams transInfo, ReqConfig<Web3j> config) throws Exception {
//		Web3j chain3j = config.getClient();
//		String signedTx = createSignedTx(transInfo, config, chain3j).getSignData();
//		EthSendTransaction tx = sendTx(chain3j, signedTx);
//
//		if (tx == null) {
//			String msg = "The return McSendTransaction object is null";
//			throw ObccException.create(EExceptionCode.RETURN_NULL_OR_EMPTY, msg);
//		}
//		if (tx.getError() != null) {
//			String msg = "Transfer error: " + tx.getError().getMessage();
//			throw ObccException.create(EExceptionCode.UNDEAL_ERROR, msg);
//		}
//		String hash = tx.getTransactionHash();
//		if (StringUtils.isNullOrEmpty(hash)) {
//			String msg = "The return hash is null";
//			throw ObccException.create(EExceptionCode.RETURN_NULL_OR_EMPTY, msg);
//		}
//
//		BcHash spcHash = new BcHash();
//		spcHash.setHash(hash);
//		return RetData.succuess(spcHash);
//	}
//	// endregion
//
//	// region sign tx
//	public SignTxData createSignedTx(TxParams transInfo, ReqConfig<Web3j> config) throws Exception {
//		preProcessTransInfo(transInfo, config);
//		Web3j chain3j = EthPoolClient.getClient(config);
//		return createSignedTx(transInfo, config, chain3j);
//	}
//
//	protected SignTxData createSignedTx(TxParams transInfo, ReqConfig<Web3j> config, Web3j chain3j)
//			throws Exception {
//		EthTxParams params = createTxParams(transInfo);
//		params.processParams(balanceService, nonceService, chain3j, config);
//		String signedTx = EthUtils.signTx(params, params.getSecret());
//
//		SignTxData spcSignTxData = new SignTxData();
//		spcSignTxData.setSignData(signedTx);
//		spcSignTxData.setNonce(params.getNonce());
//		return spcSignTxData;
//	}
//
//	protected EthTxParams createTxParams(TxParams transInfo) throws Exception {
//		TransactionType type = (TransactionType) transInfo.getParams().get(EthConstants.PARAM_TRANSACTION_TYPE);
//		EthTxParams params = null;
//		switch (type) {
//		case MAINCHAIN_TRANSFER:
//			params = new EthTxParams(transInfo);
//			break;
//		case MAINCHAIN_CALL:
//			params = new Erc20TxParams(transInfo);
//			break;
//		case MAINCHAIN_DEPOLY:
//			params = new Erc20DeployParams(transInfo);
//			break;
//		default:
//			params = null;
//			break;
//		}
//		return params;
//	}
//	// endregion
//
//	// region send tx
//	public String sendTx(String signedTransactionData, ReqConfig<Web3j> config) throws Exception {
//		return sendTx(config.getClient(), signedTransactionData).getTransactionHash();
//	}
//
//	public EthSendTransaction sendTx(Web3j chain3j, String signedTransactionData) throws Exception {
//		try {
//			EthSendTransaction tx = chain3j.ethSendRawTransaction(signedTransactionData).send();
//			return tx;
//		} catch (IOException e) {
//			String msg = "Chain3j.mcSendRawTransaction error.";
//			throw ObccException.create(EExceptionCode.IO_EXCEPTION, msg);
//		}
//	}
//	// endregion
//
//
//}
