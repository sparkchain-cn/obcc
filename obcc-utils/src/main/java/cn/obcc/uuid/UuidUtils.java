package cn.obcc.uuid;

/**
 * BaseUUid
 *
 * @author ecasona
 * @version 1.4
 * @date 2019/8/21 14:07
 * @details
 */
public class UuidUtils {

    public static Sequence sequence = new Sequence(0L, 0L);

    public UuidUtils() {
    }

    public static long get() {
        return sequence.nextId();
    }
}
