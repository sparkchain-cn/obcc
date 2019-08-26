package cn.obcc.exception;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.obcc.exception.enums.EExceptionCode;
import cn.obcc.exception.observer.SpcExceptionObserver;
import cn.obcc.utils.base.StringUtils;
import cn.obcc.vo.RetData;

/**
 * @athor pengrk
 * @create at 2019-02-28 14:51
 */

public class ObccException extends BizException {
	public static final Logger logger = LoggerFactory.getLogger(ObccException.class);

	private String moduleCode;
	private EExceptionCode errorCodeEnum;

	private boolean isOrign = false;

	private String orignCode;
	private String orignMsg;

	private ObccException(String errorCode, String message) {
		super(errorCode, message);
	}

	private ObccException(String moduleCode, EExceptionCode errorCode, String detailMsg) {
		super(errorCode.getName(), detailMsg);
		this.moduleCode = moduleCode;
		this.errorCodeEnum = errorCode;
		logger.error(
				"errorCode:" + this.getErrorCode() + ",msg:" + this.getMsg() + ",detailMsg:" + this.getDetailMsg());
	}

	private ObccException(EExceptionCode errorCode, String detailMsg, String orignCode, String orignMsg) {
		super(errorCode.getName(), detailMsg);
		this.moduleCode = "obcc";
		this.errorCodeEnum = errorCode;
		if (StringUtils.isNotNullOrEmpty(orignCode)) {
			this.isOrign = true;
			this.orignCode = orignCode;
			this.orignMsg = orignMsg;
		}
		logger.error(
				"errorCode:" + this.getErrorCode() + ",msg:" + this.getMsg() + ",detailMsg:" + this.getDetailMsg());

	}

	public EExceptionCode getErrorCodeEnum() {
		return errorCodeEnum;
	}

	public void setErrorCodeEnum(EExceptionCode errorCodeEnum) {
		this.errorCodeEnum = errorCodeEnum;
	}

	@Override
	public String getErrorCode() {
		if (isOrign) {
			return this.moduleCode + "-" + errorCodeEnum.getName() + "-" + orignCode;
		}
		return this.moduleCode + "-" + errorCodeEnum.getName();
	}

	public String getDetailMsg() {
		if (isOrign) {
			return this.getOrignMsg();
		}
		return this.msg;
	}

	@Override
	public String getMsg() {
		if (isOrign) {
			return this.errorCodeEnum.getDescr() + ":" + this.getOrignMsg();
		}
		return this.errorCodeEnum.getDescr();
	}

	public static ObccException create(String moduleCode, EExceptionCode codeEnum, Throwable throwable) {
		return new ObccException(moduleCode, codeEnum, exception(throwable));
	}

	public static ObccException create(EExceptionCode codeEnum, String msg) {
		return new ObccException("obcc", codeEnum, msg);
	}

	public static ObccException create(EExceptionCode codeEnum, Throwable throwable) {
		return create("obcc", codeEnum, throwable);
	}

	public static ObccException create(String orignCode, String orignMsg, String msg) {
		return new ObccException(EExceptionCode.ORIGN_CHAIN_ERROR, msg, orignCode, orignMsg);
	}

	public <T> RetData<T> toRetData(Class<T> cls) {
		RetData<T> rd = new RetData<>();
		rd.setSuccess(false);
		rd.setCode(getErrorCode());
		rd.setMessage(getMsg());
		return rd;
	}

	public RetData toRetData() {
		RetData rd = new RetData();
		rd.setSuccess(false);
		rd.setCode(this.getErrorCode());
		rd.setMessage(this.getMsg());
		return rd;
	}

	public RetData toRetExData(Object data) {
		RetData rd = new RetData();
		rd.setSuccess(false);
		rd.setCode(this.getErrorCode());
		rd.setMessage(this.getMsg());
		rd.setData(data);
		return rd;
	}

	public <T> RetData<T> toRetData(Class<T> cls, T data) {
		RetData<T> rd = new RetData<T>();
		rd.setSuccess(false);
		rd.setCode(getErrorCode());
		rd.setMessage(getMsg());
		rd.setData(data);
		return rd;
	}

	public RetData toDetailRetData() {
		RetData rd = new RetData<String>();
		rd.setSuccess(false);
		rd.setCode(getErrorCode());
		rd.setMessage(getMsg());
		rd.setData(getDetailMsg());
		return rd;
	}

	public RetData toDetailRetData1() {
		RetData rd = new RetData<String>();
		rd.setSuccess(false);
		rd.setCode(getErrorCode());
		rd.setMessage(getDetailMsg());
		return rd;
	}

	/**
	 * 将异常信息转化成字符串*
	 *
	 * @param t
	 * @return
	 * @throws IOException
	 */

	private static String exception(Throwable t) {
		try {
			if (t == null) {
				return null;
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				t.printStackTrace(new PrintStream(baos));
			} finally {
				baos.close();
			}
			return baos.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取异常的全部信息
	 *
	 * @param e
	 * @return
	 */
	private static String getFullErrorMessage(Throwable e) {
		StringBuffer buffer = new StringBuffer();
		StackTraceElement[] stacktrace = e.getStackTrace();
		buffer.append("Caused by: " + e + "\n");
		for (StackTraceElement tmp : stacktrace) {
			buffer.append("\tat " + tmp.toString() + "\n");
		}
		return buffer.toString();
	}

	public String getOrignCode() {
		return orignCode;
	}

	public void setOrignCode(String orignCode) {
		this.orignCode = orignCode;
	}

	public String getOrignMsg() {
		return orignMsg;
	}

	public void setOrignMsg(String orignMsg) {
		this.orignMsg = orignMsg;
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public static RetData handlerException(Throwable e, SpcExceptionObserver spcObserver) {

		if (e instanceof java.lang.reflect.InvocationTargetException) {
			e = ((InvocationTargetException) e).getTargetException();
		}
		return nonInvocation(e, spcObserver);
	}

	private static RetData nonInvocation(Throwable e, SpcExceptionObserver spcObserver) {

		if (e instanceof RuntimeException) {
			e = ObccException.create(EExceptionCode.RUNTIME_EXCEPTION, e);
		} else {
			e = ObccException.create(EExceptionCode.UNDEAL_ERROR, e);

		}

		return spcObserver.exec(((ObccException) e).errorCodeEnum, (ObccException) e);

	}
}
