package com.simplejcode.commons.misc.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

public final class QRCodeUtils {

    private QRCodeUtils() {
    }

    //-----------------------------------------------------------------------------------

    public static String getQRCodeBase64(String value, int width, int height) {
        return generateQRCodeBase64(value, width, height);
    }

    private static String generateQRCodeBase64(String text, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

            return Base64.getEncoder().encodeToString(pngOutputStream.toByteArray());
        } catch (Exception e) {
            throw convert(e);
        }
    }

    //-----------------------------------------------------------------------------------

    private static RuntimeException convert(Exception e) {
        return ExceptionUtils.wrap(e);
    }

}
