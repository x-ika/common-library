package com.simplejcode.commons.misc.util;

import javax.servlet.http.HttpServletRequest;

public final class HttpServletRequestParser {

    private HttpServletRequestParser() {
    }

    //-----------------------------------------------------------------------------------

    public static String getClientIpAddr(HttpServletRequest request) {
        String[] ipHeaderNames = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR",
        };
        for (String name : ipHeaderNames) {
            String ip = checkHeader(request, name);
            if (ip != null) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

    private static String checkHeader(HttpServletRequest request, String name) {
        String header = request.getHeader(name);
        return StringUtils.isBlank(header) || header.equalsIgnoreCase("unknown") ? null : header;
    }

    public static String getClientOS(HttpServletRequest request) {
        String userAgent = getUserAgent(request).toLowerCase();
        if (userAgent.contains("windows")) {
            return "Windows";
        }
        if (userAgent.contains("mac")) {
            return "Mac";
        }
        if (userAgent.contains("x11")) {
            return "Unix";
        }
        if (userAgent.contains("android")) {
            return "Android";
        }
        if (userAgent.contains("iphone")) {
            return "IPhone";
        }
        return "UnKnown, More-Info: " + userAgent;
    }

    public static String getClientBrowser(HttpServletRequest request) {

        final String userAgent = getUserAgent(request);
        final String lower = userAgent.toLowerCase();

        // Edge
        if (lower.contains("msie")) {
            String[] s = fromOccurrenceSplitTwice(userAgent, "MSIE", ";", " ");
            return s[0].replace("MSIE", "IE") + "-" + s[1];
        }

        // Safari
        if (lower.contains("safari") && lower.contains("version")) {
            return fromOccurrenceSplitTwice(userAgent, "Safari", " ", "/")[0] + "-" + fromOccurrenceSplitTwice(userAgent, "Version", " ", "/")[1];
        }

        // Opera
        if (lower.contains("opera")) {
            return fromOccurrenceSplitTwice(userAgent, "Opera", " ", "/")[0] + "-" + fromOccurrenceSplitTwice(userAgent, "Version", " ", "/")[1];
        }
        if (lower.contains("opr")) {
            return fromOccurrenceSplitBy(userAgent, "OPR", " ")[0].replace("/", "-").replace("OPR", "Opera");
        }

        // Chrome
        if (lower.contains("chrome")) {
            return fromOccurrenceSplitBy(userAgent, "Chrome", " ")[0].replace("/", "-");
        }

        // Netscape
        if ((lower.contains("mozilla/7.0")) || (lower.contains("netscape6")) || (lower.contains("mozilla/4.7"))
                || (lower.contains("mozilla/4.78")) || (lower.contains("mozilla/4.08")) || (lower.contains("mozilla/3")))
        {
            return "Netscape-?";
        }

        // Firefox
        if (lower.contains("firefox")) {
            return fromOccurrenceSplitBy(userAgent, "Firefox", " ")[0].replace("/", "-");
        }

        // IE
        if (lower.contains("rv")) {
            return "IE";
        }

        return "UnKnown, More-Info: " + userAgent;
    }

    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    //-----------------------------------------------------------------------------------

    private static String[] fromOccurrenceSplitTwice(String s, String substring, String regex1, String regex2) {
        return fromOccurrence(s, substring).split(regex1)[0].split(regex2);
    }

    private static String[] fromOccurrenceSplitBy(String s, String substring, String regex) {
        return fromOccurrence(s, substring).split(regex);
    }

    private static String fromOccurrence(String s, String substring) {
        return s.substring(s.indexOf(substring));
    }

}
