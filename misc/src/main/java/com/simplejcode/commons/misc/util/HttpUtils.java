package com.simplejcode.commons.misc.util;

import com.google.gson.*;

import java.io.*;
import java.net.*;
import java.util.*;

public final class HttpUtils {

    private HttpUtils() {
    }

    //-----------------------------------------------------------------------------------

    public static final String JSON_CONTENT_TYPE = "application/json";

    private static final String UTF8 = "UTF-8";

    private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();

    //-----------------------------------------------------------------------------------
    /*
    Helper Methods
     */

    public static String concatHttpParameters(String[] keys, Object... values) {
        int n = keys.length;
        if (n == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(1 << 7);
        for (int i = 0; i < n; i++) {
            if (values[i] != null) {
                sb.append('&').append(keys[i]).append('=').append(encode(values[i]));
            }
        }
        return sb.length() == 0 ? "" : sb.substring(1);
    }

    public static String concatHttpParameters(Object... keyValues) {
        int n = keyValues.length;
        if (n == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(1 << 7);
        for (int i = 0; i < n; i += 2) {
            if (keyValues[i + 1] != null) {
                sb.append('&').append(keyValues[i]).append('=').append(encode(keyValues[i + 1]));
            }
        }
        return sb.length() == 0 ? "" : sb.substring(1);
    }

    public static String concatHttpParameters(Map<Object, Object> params) {
        int n = params.size();
        if (n == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(1 << 7);
        for (Object key : params.keySet()) {
            sb.append('&').append(key).append('=').append(encode(params.get(key)));
        }
        return sb.length() == 0 ? "" : sb.substring(1);
    }

    private static String encode(Object value) {
        try {
            return URLEncoder.encode(value.toString(), UTF8);
        } catch (UnsupportedEncodingException e) {
            throw convert(e);
        }
    }

    public static String getResponseData(HttpResponse response) {
        if (response.getError() != null) {
            throw new RuntimeException(response.getError());
        }
        return response.getResponse();
    }

    //-----------------------------------------------------------------------------------
    /*
    GET POST PUT DELETE ...
     */

    public static HttpResponse get(String uri) {
        return get(uri, UTF8);
    }

    public static HttpResponse get(String uri, String charsetName) {
        return get(uri, null, charsetName);
    }

    public static HttpResponse get(String uri, Map<String, String> headers) {
        return get(uri, headers, UTF8);
    }

    public static HttpResponse get(String uri, Map<String, String> headers, String charsetName) {
        return request(uri, "GET", headers, null, charsetName);
    }


    public static HttpResponse post(String uri, String data) {
        return post(uri, null, data, UTF8);
    }

    public static HttpResponse post(String uri) {
        return post(uri, null, null, UTF8);
    }

    public static HttpResponse post(String uri, Map<String, String> headers, String data) {
        return post(uri, headers, data, UTF8);
    }

    public static HttpResponse post(String uri, Object requestBodyObject) {
        return post(uri, new Hashtable<>(), requestBodyObject);
    }

    public static HttpResponse post(String uri, Map<String, String> headers, Object requestBodyObject) {
        headers.put("Content-Type", "application/json");
        return request(uri, "POST", headers, GSON.toJson(requestBodyObject), UTF8);
    }

    public static HttpResponse post(String uri, Map<String, String> headers, String data, String charsetName) {
        return request(uri, "POST", headers, data, charsetName);
    }


    public static HttpResponse put(String uri, Map<String, String> headers, String data) {
        return request(uri, "PUT", headers, data, UTF8);
    }


    public static HttpResponse delete(String uri) {
        return delete(uri, null, null);
    }

    public static HttpResponse delete(String uri, Map<String, String> headers) {
        return delete(uri, headers, null);
    }

    public static HttpResponse delete(String uri, Map<String, String> headers, String data) {
        return request(uri, "DELETE", headers, data, UTF8);
    }

    public static HttpResponse delete(String uri, Map<String, String> headers, String data, String charsetName) {
        return request(uri, "DELETE", headers, data, charsetName);
    }

    //-----------------------------------------------------------------------------------
    /*
    General HTTP Request
     */

    public static HttpResponse request(String uri, String httpMethod, Map<String, String> headers, String data) {
        return request(uri, httpMethod, headers, data, UTF8);
    }

    public static HttpResponse request(String uri, String httpMethod, Map<String, String> headers, String data, String charsetName) {
        try {

            HttpURLConnection conn = (HttpURLConnection) new URL(uri).openConnection();

            conn.setRequestMethod(httpMethod);

            if (headers != null) {
                setHeaders(conn, headers);
            }

            if (data != null) {

                setData(conn, data.getBytes(charsetName));

            }

            int responseCode = conn.getResponseCode();

            int minOk = HttpURLConnection.HTTP_OK;
            int maxOk = 299;
            if (responseCode < minOk || responseCode > maxOk) {
                return new HttpResponse(responseCode, FileSystemUtils.read(conn.getErrorStream(), charsetName));
            }

            return new HttpResponse(responseCode, FileSystemUtils.read(conn.getInputStream(), charsetName), true);

        } catch (IOException e) {
            throw convert(e);
        }
    }

    private static void setHeaders(HttpURLConnection conn, Map<String, String> headers) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            conn.setRequestProperty(entry.getKey(), entry.getValue());
        }
    }

    private static void setData(HttpURLConnection conn, byte[] data) throws IOException {
        conn.setDoOutput(true);

        conn.setRequestProperty("Content-Length", "" + data.length);

        OutputStream outputStream = conn.getOutputStream();
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
    }

    //-----------------------------------------------------------------------------------

    private static RuntimeException convert(IOException e) {
        return ExceptionUtils.wrap(e);
    }

}
