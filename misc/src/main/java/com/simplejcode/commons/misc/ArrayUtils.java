package com.simplejcode.commons.misc;

import java.util.*;

import static java.lang.Math.min;

@SuppressWarnings("unchecked")
public final class ArrayUtils {

    private ArrayUtils() {
    }

    //-----------------------------------------------------------------------------------

    private static int[] T_INT;
    private static double[] T_DOUBLE;
    private static Object[] T_OBJECT;

    // constant array i -> i
    public static int[] ORDER;
    // P[i] element moved to i
    public static int[] P;
    // inverse of a P
    public static int[] PI;

    public static void init(int n) {
        T_INT = new int[n];
        T_DOUBLE = new double[n];
        T_OBJECT = new Object[n];
        ORDER = new int[n];
        for (int i = 0; i < n; i++) {
            ORDER[i] = i;
        }
        P = new int[n];
        PI = new int[n];
    }

    //-----------------------------------------------------------------------------------

    public static void restore(int a, int b, int[]... x) {
        int[] p = P;
        int[] t = T_INT;
        for (int[] y : x) {
            for (int i = a; i < b; i++) {
                t[p[i]] = y[i];
            }
            System.arraycopy(t, a, y, a, b - a);
        }
    }

    public static void restore(int a, int b, double[]... x) {
        int[] p = P;
        double[] t = T_DOUBLE;
        for (double[] y : x) {
            for (int i = a; i < b; i++) {
                t[p[i]] = y[i];
            }
            System.arraycopy(t, a, y, a, b - a);
        }
    }

    public static void sortBy(int a, int b, int[] p, int[]... x) {
        int[] t = T_INT;
        for (int[] y : x) {
            for (int i = a; i < b; i++) {
                t[i] = y[p[i]];
            }
            System.arraycopy(t, a, y, a, b - a);
        }
    }

    public static void sortBy(int a, int b, int[] p, double[]... x) {
        double[] t = T_DOUBLE;
        for (double[] y : x) {
            for (int i = a; i < b; i++) {
                t[i] = y[p[i]];
            }
            System.arraycopy(t, a, y, a, b - a);
        }
    }

    public static void sortBy(int a, int b, int[] p, Object[]... x) {
        Object[] t = T_OBJECT;
        for (Object[] y : x) {
            for (int i = a; i < b; i++) {
                t[i] = y[p[i]];
            }
            System.arraycopy(t, a, y, a, b - a);
        }
    }

    public static void transformBy(int a, int b, int[] p, int[] x) {
        for (int i = a; i < b; i++) {
            x[i] = p[x[i]];
        }
    }

    //-----------------------------------------------------------------------------------

    public static void quicksort(int a, int b, int[]... x) {
        int[] p = P, pi = PI;
        System.arraycopy(ORDER, a, p, a, b - a);
        quicksort(x[0], p, a, b);
        for (int i = a; i < b; i++) {
            pi[p[i]] = i;
        }
        sortBy(a, b, p, x);
    }

    public static void quicksort(int a, int b, double[]... x) {
        int[] p = P, pi = PI;
        System.arraycopy(ORDER, a, p, a, b - a);
        quicksort(x[0], p, a, b);
        for (int i = a; i < b; i++) {
            pi[p[i]] = i;
        }
        sortBy(a, b, p, x);
    }

    //-----------------------------------------------------------------------------------

    public static void quicksort(int[] x, int[] p, int a, int b) {
        int len = b - a;
        if (len < 7) {
            for (int i = a; i < b; i++) {
                for (int j = i; j > a && x[p[j - 1]] > x[p[j]]; j--) {
                    swap(p, j, j - 1);
                }
            }
            return;
        }
        int m = a + (len >> 1);
        if (len > 7) {
            int l = a;
            int n = b - 1;
            if (len > 40) {
                int s = len >> 3;
                l = median(x, p, l, l + s, l + 2 * s);
                m = median(x, p, m - s, m, m + s);
                n = median(x, p, n - 2 * s, n - s, n);
            }
            m = median(x, p, l, m, n);
        }
        int s, i = a, j = i, k = b - 1, t = k;
        int v = x[p[m]];
        while (true) {
            for (; j <= k && x[p[j]] <= v; j++) {
                if (x[p[j]] == v) {
                    swap(p, i++, j);
                }
            }
            for (; k >= j && x[p[k]] >= v; k--) {
                if (x[p[k]] == v) {
                    swap(p, k, t--);
                }
            }
            if (j > k) {
                break;
            }
            swap(p, j++, k--);
        }
        s = min(i - a, j - i);
        swap(p, a, j - s, s);
        s = min(t - k, b - t - 1);
        swap(p, j, b - s, s);
        if ((s = j - i) > 1) {
            quicksort(x, p, a, a + s);
        }
        if ((s = t - k) > 1) {
            quicksort(x, p, b - s, b);
        }
    }

    private static int median(int[] x, int[] p, int a, int b, int c) {
        return x[p[a]] < x[p[b]] ?
                x[p[b]] < x[p[c]] ? b : x[p[a]] < x[p[c]] ? c : a :
                x[p[b]] > x[p[c]] ? b : x[p[a]] > x[p[c]] ? c : a;
    }

    public static void quicksort(double[] x, int[] p, int a, int b) {
        int len = b - a;
        if (len < 7) {
            for (int i = a; i < b; i++) {
                for (int j = i; j > a && x[p[j - 1]] > x[p[j]]; j--) {
                    swap(p, j, j - 1);
                }
            }
            return;
        }
        int m = a + (len >> 1);
        if (len > 7) {
            int l = a;
            int n = b - 1;
            if (len > 40) {
                int s = len >> 3;
                l = median(x, p, l, l + s, l + 2 * s);
                m = median(x, p, m - s, m, m + s);
                n = median(x, p, n - 2 * s, n - s, n);
            }
            m = median(x, p, l, m, n);
        }
        int s, i = a, j = i, k = b - 1, t = k;
        double v = x[p[m]];
        while (true) {
            for (; j <= k && x[p[j]] <= v; j++) {
                if (x[p[j]] == v) {
                    swap(p, i++, j);
                }
            }
            for (; k >= j && x[p[k]] >= v; k--) {
                if (x[p[k]] == v) {
                    swap(p, k, t--);
                }
            }
            if (j > k) {
                break;
            }
            swap(p, j++, k--);
        }
        s = min(i - a, j - i);
        swap(p, a, j - s, s);
        s = min(t - k, b - t - 1);
        swap(p, j, b - s, s);
        if ((s = j - i) > 1) {
            quicksort(x, p, a, a + s);
        }
        if ((s = t - k) > 1) {
            quicksort(x, p, b - s, b);
        }
    }

    private static int median(double[] x, int[] p, int a, int b, int c) {
        return x[p[a]] < x[p[b]] ?
                x[p[b]] < x[p[c]] ? b : x[p[a]] < x[p[c]] ? c : a :
                x[p[b]] > x[p[c]] ? b : x[p[a]] > x[p[c]] ? c : a;
    }

    //-----------------------------------------------------------------------------------

    public static void shuffle(int[] a, int n, Random rnd) {
        for (int i = 0; i < n; i++) {
            swap(a, i, i + rnd.nextInt(n - i));
        }
    }

    public static <T> void shuffle(T[] a, int n, Random rnd) {
        for (int i = 0; i < n; i++) {
            swap(a, i, i + rnd.nextInt(n - i));
        }
    }

    public static <T> void shuffle(List<T> a, Random rnd) {
        for (int i = 0; i < a.size(); i++) {
            swap(a, i, rnd.nextInt(i + 1));
        }
    }

    //-----------------------------------------------------------------------------------

    public static void swap(int[] a, int i, int j, int n) {
        for (; n-- > 0; i++, j++) {
            swap(a, i, j);
        }
    }

    public static void swap(double[] a, int i, int j, int n) {
        for (; n-- > 0; i++, j++) {
            swap(a, i, j);
        }
    }

    //-----------------------------------------------------------------------------------

    public static void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    public static void swap(long[] a, int i, int j) {
        long t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    public static void swap(char[] a, int i, int j) {
        char t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    public static void swap(short[] a, int i, int j) {
        short t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    public static void swap(byte[] a, int i, int j) {
        byte t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    public static void swap(double[] a, int i, int j) {
        double t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    public static void swap(float[] a, int i, int j) {
        float t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    public static void swap(Object[] a, int i, int j) {
        Object t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    public static void swap(List a, int i, int j) {
        Object t = a.get(i);
        a.set(i, a.get(j));
        a.set(j, t);
    }

}
