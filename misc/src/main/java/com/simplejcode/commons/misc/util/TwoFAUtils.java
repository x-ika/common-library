package com.simplejcode.commons.misc.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.warrenstrange.googleauth.*;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.*;
import java.util.*;

public final class TwoFAUtils {

    private static String generateKeyUri(String issuer, String account, String key) throws URISyntaxException {
        URI uri = new URI("otpauth", "totp", "/" + issuer + ":" + account, "secret=" + key + "&issuer=" + issuer, null);
        return uri.toASCIIString();
    }

    private static Map<String, Object> qrCodeGeneration(String issuer, String account, String secretKey) throws URISyntaxException, WriterException, IOException {
        String charset = "UTF-8";
        Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new EnumMap<>(EncodeHintType.class);
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        String qrCodeData = generateKeyUri(issuer, account, secretKey);
        Map<String, Object> map = new HashMap<>();
        map.put("key", secretKey);
        map.put("image", createQRCode(qrCodeData, charset, hintMap));
        return map;
    }

    private static String getUserSecretKey() {
        final GoogleAuthenticator gAuth = new GoogleAuthenticator();
        final GoogleAuthenticatorKey googleAuthkey = gAuth.createCredentials();
        return googleAuthkey.getKey();
    }

    @SuppressWarnings("unchecked")
    private static byte[] createQRCode(String qrCodeData, String charset, @SuppressWarnings("rawtypes") Map hintMap) throws WriterException, IOException {

        BitMatrix matrix = new MultiFormatWriter().encode(new String(qrCodeData.getBytes(charset), charset),
                BarcodeFormat.QR_CODE, 200, 200, hintMap);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(MatrixToImageWriter.toBufferedImage(matrix), "png", baos);
        baos.flush();
        byte[] imageInByte = baos.toByteArray();
        baos.close();
        return imageInByte;
    }

    public static boolean authenticate(String key, int verificationCode) {
        GoogleAuthenticator gauth = new GoogleAuthenticator();
        return gauth.authorize(key, verificationCode);
    }

    public static Map<String, Object> getUserSecurityData(String issuer, String accountIdentifier, String secretKey) {
        try {
            return qrCodeGeneration(issuer, accountIdentifier, secretKey);
        } catch (URISyntaxException | WriterException | IOException e) {
            return null;
        }
    }

}
