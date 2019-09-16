package cn.obcc.utils;

/**
 * @author pengrk
 * @version 1.0
 * @ClassName FileSafeUtils
 * @desc TODO
 * @date 2019/9/16 0016  9:46
 **/

import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;

public class FileSafeUtils {
    /**
     * 计算大文件 md5获取getMD5(); SHA1获取getSha1() CRC32获取 getCRC32()
     */
    protected static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static MessageDigest messagedigest = null;

    /**
     * 对一个文件获取md5值
     *
     * @return md5串
     * @throws NoSuchAlgorithmException
     */
    public static String getMd5(File file) throws IOException,
            NoSuchAlgorithmException {

        messagedigest = MessageDigest.getInstance("MD5");
        FileInputStream in = new FileInputStream(file);
        FileChannel ch = in.getChannel();
        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0,
                file.length());
        messagedigest.update(byteBuffer);
        return bufferToHex(messagedigest.digest());
    }

    /**
     * @param target 字符串 求一个字符串的md5值
     * @return md5 value
     */
    public static String strMd5(String target) {
        return DigestUtils.md5Hex(target);
    }

    /***
     * 计算SHA1码
     *
     * @return String 适用于上G大的文件
     * @throws NoSuchAlgorithmException
     * */
    public static String getSha1(File file) throws OutOfMemoryError,
            IOException, NoSuchAlgorithmException {
        messagedigest = MessageDigest.getInstance("SHA-1");
        FileInputStream in = new FileInputStream(file);
        FileChannel ch = in.getChannel();
        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0,
                file.length());
        messagedigest.update(byteBuffer);
        return bufferToHex(messagedigest.digest());
    }

    public static String getSha1(InputStream inputStream) throws OutOfMemoryError,
            Exception {
        MessageDigest sha1;
        sha1 = MessageDigest.getInstance("SHA1");
        sha1.update(FileUtils.inputStreamToBytes(inputStream)); //先更新摘要
        byte[] digest = sha1.digest(); //再通过执行诸如填充之类的最终操作完成哈希计算。在调用此方法之后，摘要被重置。

        String hex = toHex(digest);
        System.out.println("SHA1摘要:" + hex);
        return hex;
    }

    /**
     * SHA1加密 使用消息摘要MessageDigest 处理
     *
     * @throws Exception
     */
    public static String encodeBySha(String str) throws Exception {
        MessageDigest sha1;
        sha1 = MessageDigest.getInstance("SHA1");
        //以下三种不可用
// sha1 = MessageDigest.getInstance("SHA256");
// sha1 = MessageDigest.getInstance("SHA384");
// sha1 = MessageDigest.getInstance("SHA512");

        sha1.update(str.getBytes()); //先更新摘要
        byte[] digest = sha1.digest(); //再通过执行诸如填充之类的最终操作完成哈希计算。在调用此方法之后，摘要被重置。

        /*
         * 使用指定的 byte 数组对摘要进行最后更新，然后完成摘要计算。
         * 也就是说，此方法首先调用 update(input)，
         * 向 update 方法传递 input 数组，然后调用 digest()。
         */
// byte[] digest = sha1.digest(str.getBytes());

        String hex = toHex(digest);
        System.out.println("SHA1摘要:" + hex);
        return hex;
    }


    /**
     * 获取文件CRC32码
     *
     * @return String
     */
    public static String getCrc32(File file) {
        CRC32 crc32 = new CRC32();
        // MessageDigest.get
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                crc32.update(buffer, 0, length);
            }
            return crc32.getValue() + "";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getMd5Str(String s) {
        return getMd5Str(s.getBytes());
    }

    public static String getMd5Str(byte[] bytes) {
        messagedigest.update(bytes);
        return bufferToHex(messagedigest.digest());
    }

    /**
     * @return String
     * @Description 计算二进制数据
     */
    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    public static boolean checkPassword(String password, String md5PwdStr) {
        String s = getMd5Str(password);
        return s.equals(md5PwdStr);
    }


    /**
     * sha1 摘要转16进制
     *
     * @param digest
     * @return
     */
    private static String toHex(byte[] digest) {
        StringBuilder sb = new StringBuilder();
        int len = digest.length;

        String out = null;
        for (int i = 0; i < len; i++) {
//  out = Integer.toHexString(0xFF & digest[i] + 0xABCDEF); //加任意 salt
            out = Integer.toHexString(0xFF & digest[i]);//原始方法
            if (out.length() == 1) {
                sb.append("0");//如果为1位 前面补个0
            }
            sb.append(out);
        }
        return sb.toString();
    }
}