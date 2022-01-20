package com.simplejcode.commons.misc;

import com.simplejcode.commons.misc.util.ExceptionUtils;

import java.nio.charset.StandardCharsets;
import java.util.zip.*;

public class ZlibDecoder {

    private final byte[] buffer;
    private final StringBuilder decompressed;

    public ZlibDecoder(int bufferSize) {
        buffer = new byte[bufferSize];
        decompressed = new StringBuilder();
    }

    public String decode(byte[] data) {
        try {
            decompressed.setLength(0);

            Inflater inflater = new Inflater(true);
            inflater.setInput(data);
            for (int read; (read = inflater.inflate(buffer)) > 0; ) {
                decompressed.append(new String(buffer, 0, read, StandardCharsets.UTF_8));
            }
            inflater.end();

            return decompressed.toString();
        } catch (DataFormatException e) {
            throw ExceptionUtils.wrap(e);
        }
    }

}
