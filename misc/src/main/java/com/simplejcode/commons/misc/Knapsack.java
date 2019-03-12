package com.simplejcode.commons.misc;

import java.util.*;

@SuppressWarnings({"StatementWithEmptyBody"})
public final class Knapsack {

    private Knapsack() {
    }

    //-----------------------------------------------------------------------------------

    public static int dpkp(int[] v, int[] p, int c) {
        int n = v.length;
        int[] F = new int[c];
        Arrays.fill(F, (int) -1e9);
        F[0] = 0;
        for (int i = 0; i < n; i++) {
            int vi = v[i], pi = p[i];
            for (int j = c, cur; j-- > vi; ) {
                if (F[j] < (cur = F[j - vi] + pi)) {
                    F[j] = cur;
                }
            }
        }
        int res = 0;
        for (int i = 0; i < c; i++) {
            if (res < F[i]) {
                res = F[i];
            }
        }
        return res;
    }

    public static int expkp(int[] v, int[] p, int c) {
        return knapsack(0, v.length, v, p, c, new int[1 << v.length], new int[1 << v.length], null);
    }

    public static int generickp(int[] v, int[] p, int c) {
        int n = v.length;
        int[] F = new int[c];
        int[] G = new int[c];
        int[] V = new int[1 << (n + 1) / 2];
        int[] P = new int[1 << (n + 1) / 2];
        knapsack(0, n / 2, v, p, c, V, P, F);
        knapsack(n / 2, n, v, p, c, V, P, G);
        int res = 0;
        for (int i = 0, u = 0, t; i < c; i++) {
            if (u < G[i]) {
                u = G[i];
            }
            if (res < (t = F[c - i - 1] + u)) {
                res = t;
            }
        }
        return res;
    }

    private static int knapsack(int a, int b, int[] v, int[] p, int c, int[] A, int[] B, int[] F) {
        int res = 0;
        int n = b - a;
        for (int i = 0; i < n; A[1 << i] = v[i + a], B[1 << i] = p[i + a], i++) ;
        for (int i = 0; i < 1 << n; i++) {
            int cv = A[i] = A[i & -i] + A[i & i - 1];
            int cp = B[i] = B[i & -i] + B[i & i - 1];
            if (cv < c) {
                res = Math.max(res, cp);
                if (F != null) {
                    F[cv] = Math.max(F[cv], cp);
                }
            }
        }
        return res;
    }

    public static long subsetsum(long[] v, long c) {
        int n = v.length;
        Map<Long, Long> F = new HashMap<>();
        Map<Long, Long> G = new HashMap<>();
        long[] V = new long[1 << (n + 1) / 2];
        fill(0, n / 2, v, c, V, F);
        fill(n / 2, n, v, c, V, G);
        for (Map.Entry<Long, Long> entry : F.entrySet()) {
            Long val = G.get(c - entry.getKey() - 1);
            if (val != null) {
                return val << (n / 2) | entry.getValue();
            }
        }
        return -1;
    }

    private static void fill(int a, int b, long[] v, long c, long[] V, Map<Long, Long> F) {
        int n = b - a;
        for (int i = 0; i < n; V[1 << i] = v[i++ + a]) ;
        for (int i = 0; i < 1 << n; i++) {
            long cv = V[i] = V[i & -i] + V[i & i - 1];
            if (cv < c) {
                F.put(cv, (long) i);
            }
        }
    }

}
