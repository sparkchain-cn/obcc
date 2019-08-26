package cn.obcc.utils.retry;

public interface IRetryParamsFn<T> {

	/**
	 * 网关的系统级别前置通知，仅仅是通知，异步调用处理，不做任何保证
	 *
	 * @return
	 */
	public T exec(int size, int i, long time) throws Exception;

	public default boolean check(T retObj, int size, int i, long time) throws Exception {
		return true;
	}

	public default long time(int size, int i, long time) throws Exception {
		return time * (i + 1);
	}

	public default void exception(int size, int i, long time, Exception e) {

	}

}
