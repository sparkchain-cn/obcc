package cn.obcc.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import cn.obcc.exception.enums.EExceptionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;

public class RetData<T> implements Serializable {

	public final static Logger logger = LoggerFactory.getLogger(RetData.class);

	private String code;
	private boolean success;

	private String message;
	private T data;

	public String getCode() {
		return code;
	}

	public RetData setCode(String code) {
		this.code = code;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public RetData<T> setMessage(String message) {
		this.message = message;
		return this;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getData() {
		return data;
	}

	public RetData<T> setData(T data) {
		this.data = data;
		return this;
	}

	public static RetData succuess() {
		RetData data = new RetData();
		data.setSuccess(true);
		data.setCode(200 + "");
		return data;
	}

	public static RetData succuess(KeyValue... kvs) {
		RetData ret = new RetData();
		Map<String, Object> map = new HashMap<>();
		for (KeyValue kv : kvs) {
			map.put(kv.getKey(), kv.getVal());
		}
		ret.setCode(200 + "");
		ret.setSuccess(true);
		ret.setData(map);
		return ret;
	}

	public static RetData succuess(Object data) {
		RetData retData = new RetData();
		retData.setCode(200 + "");
		retData.setSuccess(true);
		if (data instanceof KeyValue) {
			Map<String, Object> map = new HashMap<>();
			KeyValue kv = (KeyValue) data;
			map.put(kv.getKey(), kv.getVal());
			retData.setData(map);
		} else {
			retData.setData(data);
		}
		return retData;
	}

	public static RetData succuess(Object data, String msg) {
		RetData retData = new RetData();
		retData.setCode(200 + "");
		retData.setSuccess(true);
		retData.setMessage(msg);
		if (data instanceof KeyValue) {
			Map<String, Object> map = new HashMap<>();
			KeyValue kv = (KeyValue) data;
			map.put(kv.getKey(), kv.getVal());
			retData.setData(map);
		} else {
			retData.setData(data);
		}
		return retData;
	}

	// public static RetData succuess(int code, Object data) {
	// RetData retData = new RetData();
	// retData.setCode(code);
	// retData.setData(data);
	// return retData;
	// }

	public static RetData error(String msg) {
		RetData data = new RetData();
		data.setSuccess(false);
		data.setCode(505 + "");
		data.setMessage(msg);

		logger.error(msg);
		return data;
	}

	public static <T> RetData error(String code, String msg, T data) {
		RetData retData = new RetData<T>();
		retData.setSuccess(false);
		retData.setCode(code);
		retData.setMessage(msg);
		retData.setData(data);
		logger.error(msg);
		return retData;
	}

	public static RetData error(String code, String msg) {
		RetData retData = new RetData();
		retData.setSuccess(false);
		retData.setCode(code);
		retData.setMessage(msg);
		// retData.setData(data);
		logger.error(msg);
		return retData;
	}

	public static<T> RetData error(EExceptionCode code, T data){
		RetData retData = new RetData();
		retData.setSuccess(false);
		retData.setCode(code.getName());
		retData.setMessage(code.getDescr());
		retData.setData(data);
		return retData;
	}


	// @Override
	// public String toString() {
	// return JSON.toJSONString(this);
	// }
}
