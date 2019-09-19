package cn.obcc.utils.retry;

public class RetryUtils {

	public static <T> T retry(int size, long time, IRetryFn<T> fn) {
		if (size < 1) {
			return null;
		}
		for (int i = 0; i < size; i++) {
			try {
				return fn.exec();
			} catch (Exception e) {
				try {
					Thread.sleep(time * (i + 1));
				} catch (Exception e1) {

				}
			}
		}

		return null;
	}

	public static <T> T retry(int size, long time, IRetryParamsFn<T> fn) {
		if (size < 1) {
			return null;
		}
		for (int i = 0; i < size; i++) {
			try {
				T obj = (T) fn.exec(size, i, time);
				if (fn.check(obj, size, i, time)) {
					return obj;
				}
				sleep(size, time, i,fn);
			} catch (Exception e) {
				sleep(size, time, i,fn);
				fn.exception(size, i, time, e);
			}
		}

		return null;
	}

	private static <T> void sleep(int size, long time, int i, IRetryParamsFn<T> fn) {
		try {
			Thread.sleep(fn.time(size, i, time));
		} catch (Exception e1) {

		}

	
	}
}
