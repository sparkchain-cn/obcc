package cn.obcc.driver.vo;

import java.util.HashMap;
import java.util.Map;


public class Account {
	
	private String address;
	private String secret;
	private String publicKey;
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	
	
//
//	Map<String, String> data = new HashMap<>();
//	data.put("addr", wallet.getAddress());
//	data.put("privateKey", wallet.getSecret());
//	// data.put("publicKey", wallet.getPublicKey());
//	data.put("publicKey", wallet.getAddress());
}
