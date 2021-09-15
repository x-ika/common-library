package com.simplejcode.commons.misc.util;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

public final class CryptoUtils {

    private CryptoUtils() {
    }

    //-----------------------------------------------------------------------------------

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

    public static byte[] fromHex(String s) {
        int n = s.length();
        byte[] data = new byte[n >> 1];
        for (int i = 0; i < n; i += 2) {
            data[i >> 1] = byteValue(s.charAt(i), s.charAt(i + 1));
        }
        return data;
    }

    public static int hexValue(int t) {
        return t < 10 ? '0' + t : 'A' + t - 10;
    }

    public static byte byteValue(char h1, char h2) {
        return (byte) (intValue(h1) << 4 | intValue(h2));
    }

    public static int intValue(char hex) {
        return '0' <= hex && hex <= '9' ? hex - '0' : hex - 'A' + 10;
    }

    //-----------------------------------------------------------------------------------

    public static String md5(String data) {
        return toHex(md5(data.getBytes()));
    }

    public static byte[] md5(byte[] data) {
        return getDigest("MD5", data);
    }

    public static String sha256(String data) {
        return toHex(sha256(data.getBytes()));
    }

    public static byte[] sha256(byte[] data) {
        return getDigest("SHA-256", data);
    }

    public static String sha512(String data) {
        return toHex(sha512(data.getBytes()));
    }

    public static byte[] sha512(byte[] data) {
        return getDigest("SHA-512", data);
    }

    public static String hmacSha256(String key, String data) {
        return toHex(hmacSha256(key.getBytes(), data.getBytes()));
    }

    public static byte[] hmacSha256(byte[] key, byte[] data) {
        return encryptUsing("HmacSHA256", key, data);
    }

    public static String hmacSha384(String key, String data) {
        return toHex(hmacSha384(key.getBytes(), data.getBytes()));
    }

    public static byte[] hmacSha384(byte[] key, byte[] data) {
        return encryptUsing("HmacSHA384", key, data);
    }

    public static String hmacSha512(String key, String data) {
        return toHex(hmacSha512(key.getBytes(), data.getBytes()));
    }

    public static byte[] hmacSha512(byte[] key, byte[] data) {
        return encryptUsing("HmacSHA512", key, data);
    }


    public static byte[] encryptUsing(String algorithm, byte[] key, byte[] data) {
        try {
            Mac mac = Mac.getInstance(algorithm);
            mac.init(new SecretKeySpec(key, algorithm));
            return mac.doFinal(data);
        } catch (Exception e) {
            throw convert(e);
        }
    }

    public static byte[] getDigest(String algorithm, byte[] data) {
        try {
            return MessageDigest.getInstance(algorithm).digest(data);
        } catch (Exception e) {
            throw convert(e);
        }
    }

    //-----------------------------------------------------------------------------------

    public static String encryptWithAesEcb(String secret, String data) {
        try {
            SecretKeySpec secretKey = getKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw convert(e);
        }
    }

    public static String decryptWithAesEcb(String secret, String data) {
        try {
            SecretKeySpec secretKey = getKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(data)));
        } catch (Exception e) {
            throw convert(e);
        }
    }

    private static SecretKeySpec getKey(String secret) throws Exception {
        byte[] key = secret.getBytes(StandardCharsets.UTF_8);
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        return new SecretKeySpec(key, "AES");
    }

    //-----------------------------------------------------------------------------------

    private static RuntimeException convert(Exception e) {
        return ExceptionUtils.wrap(e);
    }

}
