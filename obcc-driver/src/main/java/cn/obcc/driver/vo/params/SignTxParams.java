package cn.obcc.driver.vo.params;

import lombok.Data;

@Data
public class SignTxParams {

	private String nonce;
	private String sourceAddress;
	private String gasPrice;
	private String gasLimit;


	
}
