package com.simplejcode.commons.algo;

import java.util.*;

@SuppressWarnings("unchecked")
public final class ArrayUtils {

    private ArrayUtils() {
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
        s = Math.min(i - a, j - i);
        swap(p, a, j - s, s);
        s = Math.min(t - k, b - t - 1);
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
        s = Math.min(i - a, j - i);
        swap(p, a, j - s, s);
        s = Math.min(t - k, b - t - 1);
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
