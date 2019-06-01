package com.simplejcode.commons.misc.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

public final class CryptoUtils {

    private CryptoUtils() {
    }

    //-----------------------------------------------------------------------------------

    public static String md5(String content) {
        return getDigest("MD5", content.getBytes());
    }

    public static String sha256(String content) {
        return getDigest("SHA-256", content.getBytes());
    }

    public static String hmacSha256(String key, String data) {
        return encryptUsing("HmacSHA256", key, data);
    }

    public static String hmacSha512(String key, String data) {
        return encryptUsing("HmacSHA512", key, data);
    }

    public static String encryptUsing(String algorithm, String key, String data) {
        try {
            Mac mac = Mac.getInstance(algorithm);
            mac.init(new SecretKeySpec(key.getBytes(), algorithm));
            return toHex(mac.doFinal(data.getBytes()));
        } catch (Exception e) {
            throw convert(e);
        }
    }

    public static String getDigest(String algorithm, byte[] content) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            StringBuilder str = new StringBuilder(32);
            for (int b : md.digest(content)) {
                if (b < 0) {
                    b += 256;
                }
                str.append((char) hexValue(b >> 4)).append((char) hexValue(b & 15));
            }
            return str.toString();
        } catch (Exception e) {
            throw convert(e);
        }
    }

    public static String toHex(String s) {
        return toHex(s.getBytes());
    }

    public static String toHex(byte[] s) {
        char[] value = new char[2 * s.length];
        for (int i = 0, j = 0; i < s.length; i++) {
            int x = s[i] & 0xFF;
            value[j++] = (char) hexValue(x >>> 4);
            value[j++] = (char) hexValue(x & 15);
        }
        return new String(value);
    }

    public static int hexValue(int t) {
        return t < 10 ? '0' + t : 'A' + t - 10;
    }

    //-----------------------------------------------------------------------------------

    private static RuntimeException convert(Exception e) {
        return ExceptionUtils.wrap(e);
    }

}
