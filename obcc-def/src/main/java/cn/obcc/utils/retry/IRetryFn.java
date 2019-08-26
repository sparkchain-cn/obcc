package cn.obcc.utils.retry;

public interface IRetryFn<T> {


    /**
     * 网关的系统级别前置通知，仅仅是通知，异步调用处理，不做任何保证
     *
     * @return
     */
    public T exec() throws Exception;


}
