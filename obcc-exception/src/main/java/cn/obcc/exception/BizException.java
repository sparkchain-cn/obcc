package cn.obcc.exception;

public class BizException extends Exception {
    protected String errorCode;
    protected String msg;
    protected Throwable cause;

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public BizException(String errorCode, String message) {
        super(errorCode + ":" + message);
        this.errorCode = errorCode;
        this.msg = message;
    }

    public BizException(String errorCode, String message, Throwable cause) {
        super(errorCode + ":" + message, cause);
        this.errorCode = errorCode;
        this.cause = cause;
        this.msg = message;
        //RetData<Integer>rd= SPCException.create(ExceptionCodeEnum.ORIGN_CHAIN_ERROR,"dd").toDetailRetData();
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public Throwable getCause() {
        return this.cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }
}
