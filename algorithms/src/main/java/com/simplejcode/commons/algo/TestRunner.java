package com.simplejcode.commons.algo;

import com.simplejcode.commons.algo.d3.*;

import java.util.*;

public class TestRunner {

    public static void main(String[] args) {
        calcBonus();
        System.exit(0);
        loanCalculator(27600, 0.0864, 900);
        test1();
//        test2();
        test3();
        test4();
        test5();
        test6();
        test7();
        test8();
        testImageProcessor();
    }

    //-----------------------------------------------------------------------------------

    public static void calcBonus() {

        int bonus = 0;

        for (int i = 0; i < 10; i++) {

            // first year - 8 months
            if (i == 0) {
                bonus += 80_000 * 2 / 3;
            }
            // current year - subtract 3 * 40% = 120%
            if (i == 2) {
                bonus += 80_000 * 10.8 / 12;
            }
            // any other year
            if (i != 0 && i != 2) {
                bonus += 80_000;
            }

            int x = (int) Math.round(0.3 * bonus);
            System.out.println(x);
            bonus -= x;

        }
    }

    public static void loanCalculator(double base, double percent, double payment) {
        double totalPercent = 0, totalBase = 0;
        for (int i = 1; ; i++) {

            double p = base * percent / 12;
            double basePayed = payment - p;

            totalPercent += p;
            totalBase += basePayed;
            base -= basePayed;

            System.out.println("+-----+-----------+-----------+-----------+");
            System.out.printf("| %-3d | %-9.2f | %-9.2f | %-9.2f |\n", i, p, basePayed, base);

            if (base <= 0) {
                System.out.println("+-----+-----------+-----------+-----------+");
                System.out.println("N_MONTH:        " + i);
                System.out.println("LAST PAYMENT:   " + (base + payment));
                System.out.println("TOTAL PERCENT:  " + totalPercent);
                System.out.println("SUM PAYED:      " + (i * payment + base));
                break;
            }

        }
    }

    //-----------------------------------------------------------------------------------

    private static void test1() {
        int n = 100;
        Random r = new Random(1);

        int[][] c = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (r.nextInt(1000) == 0) {
                    c[i][j] = r.nextInt(1111 + i + j);
                }
            }
        }
        int u = 0;
        for (int i = 0; i < 200000; i++) {
            int v = r.nextInt(n);
            c[u][v] = r.nextInt(10000);
            u = v;
        }

        long startTime = System.nanoTime();
        int max1 = GraphUtils.fordFulkerson(c, 0, n - 1);
        System.out.println("fordFulkerson " + max1);
        System.out.println("Time: " + (System.nanoTime() - startTime) / 1e9);

        startTime = System.nanoTime();
        int max2 = GraphUtils.pushRelabel(c, 0, n - 1);
        System.out.println("pushRelabel   " + max2);
        System.out.println("Time: " + (System.nanoTime() - startTime) / 1e9);

        startTime = System.nanoTime();
        int max3 = GraphUtils.edmondsKarp(c, 0, n - 1);
        System.out.println("edmondsKarp   " + max3);
        System.out.println("Time: " + (System.nanoTime() - startTime) / 1e9);

        startTime = System.nanoTime();
        int max4 = GraphUtils.dinicFlow(c, 0, n - 1);
        System.out.println("dinicFlow     " + max4);
        System.out.println("Time: " + (System.nanoTime() - startTime) / 1e9);

        startTime = System.nanoTime();
        int max5 = GraphUtils.edmondsKarp(new Graph(c, 0).getUndirectedGraph(), 0, n - 1);
        System.out.println("graph karp    " + max5);
        System.out.println("Time: " + (System.nanoTime() - startTime) / 1e9);

        startTime = System.nanoTime();
        int max6 = GraphUtils.edmondsKarpM(c, 0, n - 1);
        System.out.println("massh karp    " + max6);
        System.out.println("Time: " + (System.nanoTime() - startTime) / 1e9);

        testEnd();
    }

//    private static void test2() {
//        int n = 1000, m = 1000000;
//        int[] a = new int[n];
//        Random r = new Random(1);
//        for (int iter = 0; iter < 10; iter++) {
//
//            for (int i = 0; i < n; i++) {
//                a[i] = r.nextInt(m);
//            }
//
//            int res1 = 0;
//            for (int i = 0; i < a.length; i++) {
//                for (int j = 0; j < i; j++) {
//                    if (a[i] < a[j]) {
//                        res1++;
//                    }
//                }
//            }
//            System.out.println("inversions = " + res1);
//            long startTime = System.nanoTime();
//            long res2 = 0;
//            FenwickTree tree = new FenwickTree(m);
//            for (int i = n; i-- > 0; ) {
//                res2 += tree.sum(a[i] - 1);
//                tree.add(a[i], 1);
//            }
//            System.out.println("inversions = " + res2);
//            System.out.println("Time: " + (System.nanoTime() - startTime) / 1e9);
//            System.out.println("");
//        }
//        testEnd();
//    }

    private static void test3() {
        System.out.println("Should print {{0, 1} {2}}");
        boolean[][] b = new boolean[][]{
                {false, true, false},
                {true, false, false},
                {false, false, false},
        };
        System.out.println(new Graph(b).getComponents());
        testEnd();
    }

    private static void test4() {
        long startTime = System.nanoTime();
        Random r = new Random(1);
        int[] primes = AlgUtils.getPrimes(100);
        System.out.println(Arrays.toString(primes));
        for (int i = 1; i <= 10000; i += r.nextInt(200)) {
            System.out.println(i + " " + AlgUtils.getFactorization(i, primes));
        }
        System.out.println("Time: " + (System.nanoTime() - startTime) / 1e9);
        testEnd();
    }

    private static void test5() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    if (i != j && i != k && j != k) {
                        long x = AlgUtils.indexOf(new int[]{i, j, k});
                        System.out.printf("index of (%d, %d, %d) is %d\n", i, j, k, x);
                    }
                }
            }
        }
        testEnd();
    }

    private static void test6() {
        Random r = new Random(1);
        String p = "";
        for (int j = 0; j < 1000; j++) {
            p += (char) ('a' + r.nextInt(2));
        }
        StringBuilder sb = new StringBuilder(3000);
        for (int i = 0; i < 10; i++) {
            sb.setLength(0);
            for (int j = 0; j < 1000; j++) {
                sb.append((char) ('a' + r.nextInt(2)));
            }
            String q = sb.toString();
            long startTime = System.nanoTime();
            System.out.println("LD: " + LevenshteinDistance.getLevenshteinDistancce(p, q));
            System.out.println("Time: " + (System.nanoTime() - startTime) / 1e9);
            startTime = System.nanoTime();
            LevenshteinDistance.prepare(p);
            System.out.println("LD: " + LevenshteinDistance.distanceTo(q));
            System.out.println("Time: " + (System.nanoTime() - startTime) / 1e9);
            System.out.println("");
        }

        testEnd();
    }

    private static void test7() {
        Random r = new Random(1);


        for (int n = 10; n <= 20; n++) {

            long t1 = 0, t2 = 0, t3 = 0;

            for (int iter = 0; iter < 1; iter++) {

                int[] v = new int[n];
                int[] p = new int[n];
                for (int i = 0; i < n; i++) {
                    v[i] = r.nextInt(100000);
                    p[i] = r.nextInt(100);
                }

                int c = 1000000 + r.nextInt(1000000);

                long startTime = System.nanoTime();
                int x = Knapsack.dpkp(v, p, c);
                t1 += System.nanoTime() - startTime;

                startTime = System.nanoTime();
                int y = Knapsack.expkp(v, p, c);
                t2 += System.nanoTime() - startTime;

                startTime = System.nanoTime();
                int z = Knapsack.generickp(v, p, c);
                t3 += System.nanoTime() - startTime;

                if (x != y || x != z) {
                    System.out.println(x);
                    System.out.println(y);
                    System.out.println(z);
                    System.exit(0);
                }

            }

            System.out.println(n);
            System.out.println(t1 / 1e9);
            System.out.println(t2 / 1e9);
            System.out.println(t3 / 1e9);
            System.out.println("");

        }

        testEnd();

    }

    private static void test8() {

        System.out.println(GeometryUtils.getVolume(getCube(), 0, 0, 0.5, Math.atan(0.5), 0));
        System.out.println(GeometryUtils.getVolume(getPyramid(), 0, 0, 0.5, 0, 0));

        testEnd();
    }

    private static Polyhedron getCube() {
        int n = 8;
        Point3D[] ps = new Point3D[n];
        for (int i = 0; i < n; i++) {
            ps[i] = new Point3D(i & 1, i >> 1 & 1, i >> 2 & 1);
        }
        return new Polyhedron(ps);
    }

    private static Polyhedron getPyramid() {
        return new Polyhedron(new Point3D[]{
                new Point3D(0, 0, 0),
                new Point3D(1, 0, 0),
                new Point3D(0, 1, 0),
                new Point3D(-453, 123, -36),
        });
    }

    private static void testImageProcessor() {
        ImageProcessor img = new ImageProcessor(4, 6);

        int[] a = {
                1, 2, 3, 4,
                0, 0, 2, 5,
                0, 0, 0, 0,
                1, 1, 1, 1,
                9, 8, 7, 6,
                5, 5, 5, 5,
        };

        img.calculateRowColumnSums(a);

        System.out.println(img.getSum(0, 11)); // 17
        System.out.println(img.getSum(12, 23)); // 54
        System.out.println();

        System.out.println(img.getRowSum(0, 0, 3)); // 10
        System.out.println(img.getRowSum(0, 2, 2)); // 3
        System.out.println(img.getRowSum(4, 0, 1)); // 17
        System.out.println(img.getRowSum(4, 2, 2)); // 7
        System.out.println();

        System.out.println(img.getColSum(0, 0, 5)); // 16
        System.out.println(img.getColSum(0, 2, 2)); // 0
        System.out.println(img.getColSum(3, 5, 5)); // 5
        System.out.println(img.getColSum(3, 4, 5)); // 11
        System.out.println();
    }

    private static void testEnd() {
        System.out.println("--------------------------------------------------\n");
        System.out.println("--------------------------------------------------\n");
    }

}
