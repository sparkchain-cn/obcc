package cn.obcc.utils;

public class HexUtils {


    public static String str2HexStr(String origin) {
        if (origin == null) {
            return "";
        }
        byte[] bytes = origin.getBytes();
        String hex = bytesToHexString(bytes);
        return hex;
    }

    private static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 主要针对可能是Hex的数据，提高性能
     *
     * @param origin
     * @return
     */
    public static String strOrHexStr2HexStr(String origin) {
        if (!StringPackageUtils.isBase64(origin)) {
            return HexUtils.str2HexStr(origin);
        }
        return origin;
    }


    /**
     * 获取16进制的长度
     *
     * @param origin
     * @return
     */
    public static long hexLength(String origin) {
        String hex = strOrHexStr2HexStr(origin);
        if (hex == null) {
            return 0;
        }
        return hex.length();
    }


    public static String hexStr2Str(String hex) {
        byte[] bb = hexStringToBytes(hex);
        String rr = new String(bb).trim();
        return rr;
    }


    private static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("") || hexString.equalsIgnoreCase("0x")) {
            return new byte[2];
        }
        if (hexString.startsWith("0x"))
            hexString = hexString.replace("0x", "").trim();
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

//	    /**
//	     * Convert char to byte
//	     *
//	     * @param c char
//	     * @return byte
//	     */
//	    private static byte charToByte(char c) {
//	        return (byte) "0123456789ABCDEF".indexOf(c);
//	    }

//    /**
//     * 16进制直接转换成为字符串(无需Unicode解码)
//     * @param hexString
//     * @return
//     */
//    public static String hexStr2Str(String hexString) {
//        if (hexString == null || hexString.equals("") || hexString.equalsIgnoreCase("0x")) {
//            return "";
//        }
//        if(hexString.startsWith("0x"))
//            hexString = hexString.replace("0x","").trim();
//        hexString = hexString.toUpperCase();
//        int length = hexString.length() / 2;
//        char[] hexChars = hexString.toCharArray();
//        byte[] d = new byte[length];
//        for (int i = 0; i < length; i++) {
//            int pos = i * 2;
//            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
//        }
//        return new String(d);
//    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    /**
     * 字符串转化成为16进制字符串
     *
     * @param s
     * @return
     */
    public static String strTo16(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    /**
     * unicode 转字符串
     */
    public static String unicode2String(String unicode) {
        StringBuffer string = new StringBuffer();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);
            // 追加成string
            string.append((char) data);
        }
        return string.toString();
    }


    /**
     * 字节数组转16进制
     *
     * @param bytes 需要转换的byte数组
     * @return 转换后的Hex字符串
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();

    }


    /**
     * hex字符串转byte数组
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte数组结果
     */
    public static byte[] hexToByteArray(String inHex) {
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1) {
            //奇数
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            //偶数
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = hexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    /**
     * Hex字符串转byte
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte
     */
    public static byte hexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }
}
