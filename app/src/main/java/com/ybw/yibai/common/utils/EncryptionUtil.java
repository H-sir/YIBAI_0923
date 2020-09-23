package com.ybw.yibai.common.utils;

import android.support.annotation.NonNull;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 各种加密方法
 *
 * @author sjl
 */
public class EncryptionUtil {

    /**
     * MD5加密
     *
     * @param content 要加密的内容
     * @return 加密后内容
     */
    public static String md5(String content) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = content.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * sha1安全哈希算法
     */
    public static String sha1(@NonNull String text) {
        MessageDigest md;
        String outString;
        try {
            md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(text.getBytes());
            outString = byteToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return outString;
    }

    /**
     * Base64解码字符串
     *
     * @param encodeStr 需要解码的字符串
     * @return 解码后的字符串
     */
    public static String base64DecodeString(@NonNull String encodeStr) {
        Base64 base64 = new Base64();
        byte[] b = encodeStr.getBytes();
        b = base64.decode(b);
        return new String(b);
    }

    /**
     * byte转字符串
     */
    private static String byteToString(@NonNull byte[] digest) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            String tempStr = Integer.toHexString(digest[i] & 0xff);
            if (tempStr.length() == 1) {
                buf.append("0").append(tempStr);
            } else {
                buf.append(tempStr);
            }
        }
        return buf.toString().toLowerCase();
    }
}
