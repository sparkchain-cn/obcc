package cn.obcc.driver.vo.params;

public class SignTxParams {

	private String nonce;
	private String sourceAddress;
	private String gasPrice;
	private String gasLimit;
	public String getNonce() {
		return nonce;
	}
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	public String getSourceAddress() {
		return sourceAddress;
	}
	public void setSourceAddress(String sourceAddress) {
		this.sourceAddress = sourceAddress;
	}
	public String getGasPrice() {
		return gasPrice;
	}
	public void setGasPrice(String gasPrice) {
		this.gasPrice = gasPrice;
	}
	public String getGasLimit() {
		return gasLimit;
	}
	public void setGasLimit(String gasLimit) {
		this.gasLimit = gasLimit;
	}

	
}
