package com.simplejcode.commons.algo;

import java.util.Arrays;

public final class LevenshteinDistance {

    private LevenshteinDistance() {
    }

    //-----------------------------------------------------------------------------------

    private static int length, endID, curID;
    private static int[] intValue = new int[Character.MAX_VALUE + 1];
    private static long[][] set;

    public static void prepare(String original) {
        Arrays.fill(intValue, -1);
        curID = 0;
        length = original.length();
        set = new long[26][];
        for (int i = 0; i < set.length; i++) {
            set[i] = new long[FastBitSet.calcSize(length)];
        }
        for (int i = 0; i < length; i++) {
            char c = original.charAt(i);
            if (intValue[c] == -1) {
                intValue[c] = curID++;
            }
            FastBitSet.set(set[intValue[c]], i);
        }
        endID = curID;
    }

    public static int distanceToX(String s) {
        int tmp = curID;
        int n = s.length(), dist = length, shift = length - 1;
        long vn = 0, vp = -1, d0, hp, hn;
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            if (intValue[c] == -1 || endID <= intValue[c] && intValue[c] < tmp) {
                intValue[c] = curID++ - tmp + endID;
            }
            long msk = set[intValue[c]][0];
            d0 = (((msk & vp) + vp) ^ vp) | msk | vn;
            hp = vn | ~(d0 | vp);
            hn = d0 & vp;
            dist += ((hp >>> shift) & 1) - ((hn >>> shift) & 1);
            hp = (hp << 1) | 1;
            vp = (hn << 1) | ~(d0 | hp);
            vn = d0 & hp;
        }
        return dist;
    }

    public static int distanceTo(String s) {
        int tmp = curID;
        int n = s.length(), dist = length, shift = length - 1;
        long[] vn = new long[FastBitSet.calcSize(length)];
        long[] vp = new long[FastBitSet.calcSize(length)];
        Arrays.fill(vp, -1);
        long[] d0 = new long[FastBitSet.calcSize(length)];
        long[] hp = new long[FastBitSet.calcSize(length)];
        long[] hn = new long[FastBitSet.calcSize(length)];
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            if (intValue[c] == -1 || endID <= intValue[c] && intValue[c] < tmp) {
                intValue[c] = curID++ - tmp + endID;
            }
            long[] msk = set[intValue[c]];
            //d0 = (((msk & vp) + vp) ^ vp) | msk | vn;
//            d0.init(msk).and(vp).add(vp).xor(vp).or(msk).or(vn);
            //hp = vn | ~(d0 | vp);
//            hp.init(d0).or(vp).not().or(vn);
            //hn = d0 & vp;
//            hn.init(d0).and(vp);
            //dist += ((hp >>> shift) & 1) - ((hn >>> shift) & 1);
//            dist += (hp.get(shift) ? 1 : 0) - (hn.get(shift) ? 1 : 0);
            //hp = (hp << 1) | 1;
//            hp.shiftLeft(1).set(0);
            //vp = (hn << 1) | ~(d0 | hp);
//            vp.init(d0).or(hp).not().or(hn.shiftLeft(1));
            //vn = d0 & hp;
//            vn.init(d0).and(hp);
        }
        return dist;
    }

    public static int getLevenshteinDistancce(String p, String q) {
        int n = p.length();
        int m = q.length();

        int[][] dist = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) {
            dist[i][0] = i;
        }
        for (int i = 0; i <= m; i++) {
            dist[0][i] = i;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                // insertion
                int ins = dist[i][j - 1] + 1;
                // deletion
                int del = dist[i - 1][j] + 1;
                //substitution
                int cost = p.charAt(i - 1) == q.charAt(j - 1) ? 0 : 1;
                int subst = dist[i - 1][j - 1] + cost;

                dist[i][j] = Math.min(Math.min(ins, del), subst);
            }
        }

        return dist[n][m];
    }

}
