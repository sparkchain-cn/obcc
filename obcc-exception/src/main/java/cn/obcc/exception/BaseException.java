package cn.obcc.exception;

/**
 * @author 彭仁夔
 * @email 546711211@qq.com
 * @time 2017/8/21 14:00
 */
public class BaseException extends Exception {

	protected String errorCode;

	protected String msg;

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	protected Throwable cause;

	public BaseException(String errorCode, String message) {
		super(errorCode + ":" + message);
		this.errorCode = errorCode;
		this.msg = message;
	}

	public BaseException(String errorCode, String message, Throwable cause) {
		super(errorCode + ":" + message, cause);
		this.errorCode = errorCode;
		this.cause = cause;
		this.msg = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	@Override
	public Throwable getCause() {
		return cause;
	}

	public void setCause(Throwable cause) {
		this.cause = cause;
	}

}
