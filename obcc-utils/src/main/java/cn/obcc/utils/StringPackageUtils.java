package cn.obcc.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import cn.obcc.utils.base.StringUtils;

/**
 * StringPackageUtils
 *
 * @author ecasona
 * @version 1.4
 * @date 2018/11/19 9:42
 */
public class StringPackageUtils {


    public static String backString(String origin) {
        return HexStringUtils.hexStr2Str(origin);
    }

    /**
     * 把原始字符串分割成指定长度的字符串列表
     *
     * @param inputString 原始字符串
     * @param length      指定长度
     * @return
     */
    public static List<String> getStrList(String inputString, int length) {
        int size = inputString.length() / length;
        if (inputString.length() % length != 0) {
            size += 1;
        }
        return getStrList(inputString, length, size);
    }

    /**
     * 把原始字符串分割成指定长度的字符串列表
     *
     * @param inputString 原始字符串
     * @param length      指定长度
     * @param size        指定列表大小
     * @return
     */
    public static List<String> getStrList(String inputString, int length, int size) {
        List<String> list = new ArrayList<String>();
        for (int index = 0; index < size; index++) {
            String childStr = substring(inputString, index * length,
                    (index + 1) * length);
            list.add(childStr);
        }
        return list;
    }

    /**
     * 分割字符串，如果开始位置大于字符串长度，返回空
     *
     * @param str 原始字符串
     * @param f   开始位置
     * @param t   结束位置
     * @return
     */
    public static String substring(String str, int f, int t) {
        if (f > str.length()) {
            return null;
        }
        if (t > str.length()) {
            return str.substring(f, str.length());
        } else {
            return str.substring(f, t);
        }
    }

    public static boolean isBase64(String str) {
        String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        return Pattern.matches(base64Pattern, str);
    }

    public static List<String> divideString(String data, String pat) {
        if (data.contains(pat)) {
            return Arrays.stream(data.split(pat))
                    .filter(StringUtils::isNotNullOrEmpty)
                    .collect(Collectors.toList());
        } else {
            return Collections.singletonList(data);
        }
    }

    public static String getRandomString(int length) {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        System.out.println(str.length());
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(36);// [0,51)
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }


    /**
     * memo 拼接
     *
     * @param hashes list 需要拼接的 hash
     * @param memos  每个 hash 对应 memo
     * @return 拼接完成后转义的memo
     */
    public static String backMemos(List<String> hashes, Map<String, String> memos) {
        //原始数据不完整，无法拼接
        if (null == hashes || hashes.size() == 0) {
            return null;
        }
        //拼接数据不完整，无法拼接
        if (null == memos || memos.size() == 0 || memos.size() < hashes.size()) {
            return null;
        }
        //数据拼接
        String collect = hashes.stream().map(memos::get).collect(Collectors.joining(""));
        //base64 反转
        return StringPackageUtils.backString(collect);
    }


    public static void main(String[] args) {
        System.out.println(getRandomString(8));
    }


}
