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

    public static int hexValue(int t) {
        return t < 10 ? '0' + t : 'A' + t - 10;
    }

    //-----------------------------------------------------------------------------------

    public static String md5(String data) {
        return getDigest("MD5", data.getBytes());
    }

    public static String sha256(String data) {
        return getDigest("SHA-256", data.getBytes());
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

    public static String getDigest(String algorithm, byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            StringBuilder str = new StringBuilder(32);
            for (int b : md.digest(data)) {
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
