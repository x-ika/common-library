package com.simplejcode.commons.misc;

import java.lang.reflect.Method;
import java.util.ArrayList;

public final class MMTester {

    private MMTester() {
    }

    public static final int ABSOLUTE_SCORING = 0;
    public static final int RELATIVE_KEEP_MAX = 1;
    public static final int RELATIVE_KEEP_MIN = 2;

    public static void test(Method test, String file, int type,
                            String[] args, int s, Class... classes) throws Exception
    {
        ArrayList<Long> seeds = new ArrayList<>();
        if (args[s].equals("a")) {
            for (long i = Long.parseLong(args[s + 1]); i < Long.parseLong(args[s + 2]); i++) {
                seeds.add(i);
            }
        } else {
            for (int i = s; i < args.length; i++) {
                seeds.add(Long.parseLong(args[i]));
            }
        }
        int n = classes.length;
        double[] sum = new double[n + 1];
        DynamicMap<Long> map = new DynamicMap<>(type == RELATIVE_KEEP_MIN, file);
        for (long seed : seeds) {
            double[] sc = new double[n + 1];
            sc[n] = map.get(seed);
            for (int i = 0; i < n; i++) {
                map.update(seed, sc[i] = (Double) test.invoke(null, seed, classes[i]));
            }
            System.out.print("Seed: " + seed + "\n                       ");
            for (int i = 0; i <= n; i++) {
                sum[i] += sc[i] = calc(sc[i], map.get(seed), type);
                printAligned(String.valueOf(sc[i]));
            }
            System.out.println('\n');
        }
        map.close();
        System.out.print("                       ");
        for (double score : sum) {
            printAligned(String.valueOf(score));
        }
        System.out.println();
    }

    public static double calc(double score, double best, int type) {
        return type == ABSOLUTE_SCORING ? score :
                type == RELATIVE_KEEP_MAX ? best == 0 ? 1 : score / best : score == 0 ? 1 : best / score;
    }

    private static void printAligned(String s) {
        System.out.print(s + "                        ".substring(s.length()));
    }

}
