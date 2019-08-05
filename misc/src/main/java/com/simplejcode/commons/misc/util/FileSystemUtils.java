package com.simplejcode.commons.misc.util;

import java.io.*;
import java.net.*;
import java.util.Arrays;

public final class FileSystemUtils {

    private FileSystemUtils() {
    }

    //-----------------------------------------------------------------------------------

    private static final int DEFAULT_BUFFER_SIZE = 1 << 16;

    private static final String UTF8 = "UTF-8";

    //-----------------------------------------------------------------------------------

    public static byte[] readBytes(InputStream is) {
        try {
            byte[] result = new byte[DEFAULT_BUFFER_SIZE];
            byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
            int read = 0;
            for (int length; (length = is.read(buf)) != -1; read += length) {
                if (result.length < read + length) {
                    result = Arrays.copyOf(result, result.length << 1);
                }
                System.arraycopy(buf, 0, result, read, length);
            }
            is.close();
            return Arrays.copyOf(result, read);
        } catch (IOException e) {
            throw convert(e);
        }
    }

    public static String read(InputStream is, String charset) {
        try {
            InputStreamReader reader = new InputStreamReader(is, charset);
            StringBuilder buffer = new StringBuilder(DEFAULT_BUFFER_SIZE);
            char[] buf = new char[DEFAULT_BUFFER_SIZE];
            for (int length; (length = reader.read(buf, 0, buf.length)) != -1; ) {
                buffer.append(buf, 0, length);
            }
            reader.close();
            return buffer.toString();
        } catch (IOException e) {
            throw convert(e);
        }
    }

    public static String read(InputStream is) {
        return read(is, UTF8);
    }

    //-----------------------------------------------------------------------------------

    public static byte[] readBytes(File file) {
        try {
            return readBytes(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw convert(e);
        }
    }

    public static String read(File file) {
        try {
            return read(new FileInputStream(file));
        } catch (IOException e) {
            throw convert(e);
        }
    }

    public static String read(String fileName) {
        return read(new File(fileName));
    }

    //-----------------------------------------------------------------------------------

    public static void write(String fileName, String text) {
        write(fileName, UTF8, text);
    }

    public static void write(String fileName, String charsetName, String text) {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(
                    new FileOutputStream(fileName), charsetName);
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            throw convert(e);
        }
    }

    public static void writeBytes(String fileName, byte[] buf) {
        try {
            FileOutputStream outputStream = new FileOutputStream(fileName);
            outputStream.write(buf);
            outputStream.close();
        } catch (IOException e) {
            throw convert(e);
        }
    }

    //-----------------------------------------------------------------------------------

    public static void copy(File input, File output) {
        try {
            byte[] data = readBytes(input);
            OutputStream out = new FileOutputStream(output);
            out.write(data);
            out.close();
        } catch (IOException e) {
            throw convert(e);
        }
    }

    public static void delete(File file, boolean contentOnly) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                delete(f, false);
            }
        }
        if (!contentOnly) {
            file.delete();
        }
    }

    //-----------------------------------------------------------------------------------

    public static InputStream getResource(String path) {
        try {
            return new FileInputStream(path);
        } catch (IOException e) {
            InputStream stream = FileSystemUtils.class.getResourceAsStream("/" + path);
            if (stream != null) {
                return stream;
            }
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!! CTX " + path);
            return Thread.currentThread().getContextClassLoader().getResourceAsStream("/" + path);
        }
    }

    public static URL getFileURL(String path) {
        File file = new File(path);
        if (file.exists()) {
            try {
                return file.toURI().toURL();
            } catch (MalformedURLException e) {
                // continue
            }
        }
        URL url = FileSystemUtils.class.getResource("/" + path);
        if (url != null) {
            return url;
        }
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!! CTX " + path);
        return Thread.currentThread().getContextClassLoader().getResource("/" + path);
    }

    //-----------------------------------------------------------------------------------

    private static RuntimeException convert(IOException e) {
        return ExceptionUtils.wrap(e);
    }

}
