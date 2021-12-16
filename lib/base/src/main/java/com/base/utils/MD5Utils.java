package com.base.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具类
 * </br>
 * Date: 2018/10/25 10:40
 *
 * @author hemin
 */
public class MD5Utils {
    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};
    public static final String CHARSET_NAME = "UTF-8";

    /**
     * 转换字节数组为16进制字串
     *
     * @param b 字节数组
     * @return 16进制字串
     */
    protected static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    /**
     * 转换字节数组为高位字符串
     *
     * @param b 字节数组
     * @return
     */
    protected static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * MD5 摘要计算(byte[]).
     *
     * @param src byte[]
     * @return String
     * @throws Exception
     */
    public static String sign(byte[] src) {
        MessageDigest alg;
        try {
            // MD5 is 32 bit message digest
            alg = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return byteArrayToHexString(alg.digest(src));
    }

    public static String sign(String content) {
        try {
            return sign(content.getBytes(CHARSET_NAME));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sign(String content, String md5Key) {
        content = content + "&key=" + md5Key;
        try {
            return sign(content.getBytes(CHARSET_NAME));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断content加密后是否为md5Value
     *
     * @param content
     * @param md5Value
     * @param md5Key
     * @return ture 是的，false 不是
     */
    public static boolean verify(String content, String md5Value, String md5Key) {
        content = content + "&key=" + md5Key;
        try {
            return sign(content.getBytes(CHARSET_NAME)).equals(md5Value);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断content加密后是否为md5Value
     *
     * @param content  字符串
     * @param md5Value 加密后结果
     * @return ture 是的，false 不是
     */
    public static boolean verify(String content, String md5Value) {
        try {
            return sign(content.getBytes(CHARSET_NAME)).equals(md5Value);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }
}
