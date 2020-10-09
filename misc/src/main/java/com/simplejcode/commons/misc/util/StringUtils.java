package com.simplejcode.commons.misc.util;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class StringUtils {

    private StringUtils() {
    }

    //-----------------------------------------------------------------------------------

    public static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    //-----------------------------------------------------------------------------------
    /*
    Efficient Constructing-Parsing
     */

    public static String concat(char delimiter, List<?> list) {
        if (list.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Object t : list) {
            sb.append(delimiter).append(t == null ? "" : t.toString());
        }
        return sb.substring(1);
    }

    public static String concat(char delimiter, String... s) {
        if (s.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String p : s) {
            sb.append(delimiter).append(p == null ? "" : p);
        }
        return sb.substring(1);
    }

    public static String repeat(String s, int n) {
        if (n == 0) {
            return "";
        }
        int k = s.length();
        char[] c = s.toCharArray();
        char[] result = new char[k * n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(c, 0, result, i * k, k);
        }
        return new String(result);
    }

    public static String[] parse(String s, char delimiter, char brace) {
        List<String> args = new ArrayList<>();
        int last = 0;
        boolean inside = false;
        for (int i = 0; i <= s.length(); i++) {
            if (!inside && (i == s.length() || s.charAt(i) == delimiter)) {
                if (last < i) {
                    args.add(s.substring(last, i));
                }
                last = i + 1;
                continue;
            }
            if (s.charAt(i) == brace) {
                if (inside) {
                    args.add(s.substring(last, i));
                }
                last = i + 1;
                inside = !inside;
            }
        }

        return args.toArray(new String[0]);
    }

    //-----------------------------------------------------------------------------------

    public static String encode(Object t) {
        return URLEncoder.encode(t.toString(), StandardCharsets.UTF_8);
    }

    public static String decode(String s) {
        return URLDecoder.decode(s, StandardCharsets.UTF_8);
    }

    //-----------------------------------------------------------------------------------
    /*
    Longest Increasing/Common Subsequence
     */

    public static int lis(int[] a, int[][] dp) {
        int[] b = a.clone();
        Arrays.sort(a);
        return lcs(a, b, dp);
    }

    public static int lcs(int[] a, int[] b, int[][] dp) {
        int n = a.length;
        int m = b.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (a[i] == b[j]) {
                    dp[i + 1][j + 1] = dp[i][j] + 1;
                } else {
                    dp[i + 1][j + 1] = Math.max(dp[i + 1][j], dp[i][j + 1]);
                }
            }
        }
        return dp[n][m];
    }

    public static int lcs(int n, int m, boolean[][] eq, int[][] dp) {
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (eq[i - 1][j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[n][m];
    }

    public static void restoreLCS(int n, int m, int[][] dp, boolean[] p, boolean[] q) {
        while (n != 0 && m != 0) {
            int x = dp[n][m];
            if (x == dp[n - 1][m]) {
                n--;
            } else if (x == dp[n][m - 1]) {
                m--;
            } else {
                p[--n] = q[--m] = true;
            }
        }
    }

}
