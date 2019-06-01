package com.simplejcode.commons.algo;

public final class ArraySort {

    private ArraySort() {
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
        ArrayUtils.quicksort(x[0], p, a, b);
        for (int i = a; i < b; i++) {
            pi[p[i]] = i;
        }
        sortBy(a, b, p, x);
    }

    public static void quicksort(int a, int b, double[]... x) {
        int[] p = P, pi = PI;
        System.arraycopy(ORDER, a, p, a, b - a);
        ArrayUtils.quicksort(x[0], p, a, b);
        for (int i = a; i < b; i++) {
            pi[p[i]] = i;
        }
        sortBy(a, b, p, x);
    }

}
