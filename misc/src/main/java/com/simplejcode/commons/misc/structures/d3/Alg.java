package com.simplejcode.commons.misc.structures.d3;

public class Alg {

    public static double det(double... a) {

        // 0 1 2
        // 3 4 5
        // 6 7 8

        double v1 = a[0] * (a[4] * a[8] - a[5] * a[7]);
        double v2 = a[3] * (a[1] * a[8] - a[2] * a[7]);
        double v3 = a[6] * (a[1] * a[5] - a[2] * a[4]);

        return v1 - v2 + v3;

    }

}
