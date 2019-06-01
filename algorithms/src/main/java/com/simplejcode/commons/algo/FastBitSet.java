package com.simplejcode.commons.algo;

public final class FastBitSet {

    private FastBitSet() {
    }

    private static final int ADDRESS_BITS_PER_WORD = 5;
    private static final int BITS_PER_WORD = 1 << ADDRESS_BITS_PER_WORD;
    private static final int BIT_INDEX_MASK = BITS_PER_WORD - 1;
    private static final int WORD_MASK = 0xffffffff;
    private static final int UWORD_MASK = 0x7fffffff;

    public static int calcSize(int nBits) {
        return (nBits - 1 >> ADDRESS_BITS_PER_WORD) + 1;
    }

    //-----------------------------------------------------------------------------------
    /*
    Int
     */

    private static int getLast(int x) {
        return x >>> BIT_INDEX_MASK & 1;
    }

    public static int cardinality(int[] s) {
        int sum = 0, n = s.length;
        for (int i = 0; i < n; i++) {
            sum += Integer.bitCount(s[i]);
        }
        return sum;
    }

    public static int extract(int[] s, int i, int j) {
        int swi = i >> ADDRESS_BITS_PER_WORD;
        int ewi = j - 1 >> ADDRESS_BITS_PER_WORD;
        int msk = WORD_MASK >>> -j;
        if (swi == ewi) {
            return (s[swi] & msk) >>> i;
        }
        return s[swi] >>> i | (s[ewi] & msk) << BITS_PER_WORD - i;
    }

    public static void clear(int[] s, int i) {
        s[i >> ADDRESS_BITS_PER_WORD] &= ~(1 << i);
    }

    public static void clear(int[] s, int i, int j) {
        int swi = i >> ADDRESS_BITS_PER_WORD;
        int ewi = j - 1 >> ADDRESS_BITS_PER_WORD;
        int fwm = WORD_MASK << i;
        int lwm = WORD_MASK >>> -j;
        if (swi == ewi) {
            s[swi] &= ~(fwm & lwm);
        } else {
            s[swi] &= ~fwm;
            for (int k = swi + 1; k < ewi; k++) {
                s[k] = 0;
            }
            s[ewi] &= ~lwm;
        }
    }

    public static void set(int[] s, int i) {
        s[i >> ADDRESS_BITS_PER_WORD] |= 1 << (i & BIT_INDEX_MASK);
    }

    public static void set(int[] s, int i, int j) {
        int swi = i >> ADDRESS_BITS_PER_WORD;
        int ewi = j - 1 >> ADDRESS_BITS_PER_WORD;
        int fwm = WORD_MASK << i;
        int lwm = WORD_MASK >>> -j;
        if (swi == ewi) {
            s[swi] |= fwm & lwm;
        } else {
            s[swi] |= fwm;
            for (int k = swi + 1; k < ewi; k++) {
                s[k] = WORD_MASK;
            }
            s[ewi] |= lwm;
        }
    }

    public static boolean get(int[] s, int i) {
        return (s[i >> ADDRESS_BITS_PER_WORD] & 1 << (i & BIT_INDEX_MASK)) != 0;
    }

    public static boolean get(int[] s, int i, int j) {
        int swi = i >> ADDRESS_BITS_PER_WORD;
        int ewi = j - 1 >> ADDRESS_BITS_PER_WORD;
        int fwm = WORD_MASK << i;
        int lwm = WORD_MASK >>> -j;
        if (swi == ewi) {
            return (s[swi] & fwm & lwm) != 0;
        } else {
            if ((s[swi] & fwm) != 0 || (s[ewi] & lwm) != 0) {
                return true;
            }
            for (int k = swi + 1; k < ewi; k++) {
                if (s[k] != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int[] not(int[] s) {
        for (int i = 0; i < s.length; i++) {
            s[i] = ~s[i];
        }
        return s;
    }

    public boolean secs(int[] s1, int[] s2) {
        for (int i = 0; i < s1.length; i++) {
            if ((s1[i] & s2[i]) != 0) {
                return true;
            }
        }
        return false;
    }

    public int[] assign(int[] s1, int[] s2) {
        System.arraycopy(s2, 0, s1, 0, s1.length);
        return s1;
    }

    public static int[] or(int[] s1, int[] s2) {
        for (int i = 0; i < s1.length; i++) {
            s1[i] |= s2[i];
        }
        return s1;
    }

    public static int[] and(int[] s1, int[] s2) {
        for (int i = 0; i < s1.length; i++) {
            s1[i] &= s2[i];
        }
        return s1;
    }

    public static int[] xor(int[] s1, int[] s2) {
        for (int i = 0; i < s1.length; i++) {
            s1[i] ^= s2[i];
        }
        return s1;
    }

    public static int[] add(int[] s1, int[] s2) {
        int t = 0;
        for (int i = 0; i < s1.length; i++) {
            s1[i] += t;
            int x = getLast(s1[i]) + getLast(s2[i]) + getLast((s1[i] & UWORD_MASK) + (s2[i] & UWORD_MASK));
            s1[i] += s2[i];
            t = x > 1 ? 1 : 0;
        }
        return s1;
    }

    public static int[] andNot(int[] s1, int[] s2) {
        for (int i = 0; i < s1.length; i++) {
            s1[i] &= ~s2[i];
        }
        return s1;
    }

    public static int[] shiftLeft(int[] s, int shift) {
        int msk = (1 << shift) - 1;
        int rsh = BITS_PER_WORD - shift;
        for (int i = s.length; i-- > 1; ) {
            s[i] = s[i] << shift | s[i - 1] >>> rsh & msk;
        }
        s[0] <<= shift;
        return s;
    }

    public static int[] shiftRight(int[] s, int shift) {
        int lsh = BITS_PER_WORD - shift;
        int last = s.length - 1;
        for (int i = 0; i < last; i++) {
            s[i] = s[i] >>> shift | s[i + 1] << lsh;
        }
        s[last] >>>= shift;
        return s;
    }

    public static int nextSetBit(int[] s, int i) {
        int j = i >> ADDRESS_BITS_PER_WORD;
        if (j >= s.length) return -1;

        int w = s[j] & (WORD_MASK << i);
        while (true) {
            if (w != 0)
                return (j * BITS_PER_WORD) + Integer.numberOfTrailingZeros(w);
            if (++j == s.length) return -1;
            w = s[j];
        }
    }

    public static String toString(int[] s) {
        StringBuilder ret = new StringBuilder(s.length * BITS_PER_WORD);
        for (int i = ret.capacity(); i-- > 0; ) {
            ret.append(get(s, i) ? '1' : '0');
        }
        return ret.toString();
    }

    public static int compare(int[] s1, int[] s2) {
        int n = s1.length, d = n - s2.length;
        if (d != 0) {
            return d;
        }
        for (int i = 0; i < n; i++) {
            int x = s1[i], y = s2[i];
            if (x != y) {
                return x < y ? -1 : 1;
            }
        }
        return 0;
    }

    //-----------------------------------------------------------------------------------
    /*
    Long
     */

    private static long getLast(long x) {
        return x >>> BIT_INDEX_MASK & 1L;
    }

    public static int cardinality(long[] s) {
        int sum = 0, n = s.length;
        for (int i = 0; i < n; i++) {
            sum += Long.bitCount(s[i]);
        }
        return sum;
    }

    public static long extract(long[] s, int i, int j) {
        int swi = i >> ADDRESS_BITS_PER_WORD;
        int ewi = j - 1 >> ADDRESS_BITS_PER_WORD;
        long msk = WORD_MASK >>> -j;
        if (swi == ewi) {
            return (s[swi] & msk) >>> i;
        }
        return s[swi] >>> i | (s[ewi] & msk) << BITS_PER_WORD - i;
    }

    public static void clear(long[] s, int i) {
        s[i >> ADDRESS_BITS_PER_WORD] &= ~(1L << i);
    }

    public static void clear(long[] s, int i, int j) {
        int swi = i >> ADDRESS_BITS_PER_WORD;
        int ewi = j - 1 >> ADDRESS_BITS_PER_WORD;
        long fwm = WORD_MASK << i;
        long lwm = WORD_MASK >>> -j;
        if (swi == ewi) {
            s[swi] &= ~(fwm & lwm);
        } else {
            s[swi] &= ~fwm;
            for (int k = swi + 1; k < ewi; k++) {
                s[k] = 0;
            }
            s[ewi] &= ~lwm;
        }
    }

    public static void set(long[] s, int i) {
        s[i >> ADDRESS_BITS_PER_WORD] |= 1L << (i & BIT_INDEX_MASK);
    }

    public static void set(long[] s, int i, int j) {
        int swi = i >> ADDRESS_BITS_PER_WORD;
        int ewi = j - 1 >> ADDRESS_BITS_PER_WORD;
        long fwm = WORD_MASK << i;
        long lwm = WORD_MASK >>> -j;
        if (swi == ewi) {
            s[swi] |= fwm & lwm;
        } else {
            s[swi] |= fwm;
            for (int k = swi + 1; k < ewi; k++) {
                s[k] = WORD_MASK;
            }
            s[ewi] |= lwm;
        }
    }

    public static boolean get(long[] s, int i) {
        return (s[i >> ADDRESS_BITS_PER_WORD] & 1L << (i & BIT_INDEX_MASK)) != 0;
    }

    public static boolean get(long[] s, int i, int j) {
        int swi = i >> ADDRESS_BITS_PER_WORD;
        int ewi = j - 1 >> ADDRESS_BITS_PER_WORD;
        long fwm = WORD_MASK << i;
        long lwm = WORD_MASK >>> -j;
        if (swi == ewi) {
            return (s[swi] & fwm & lwm) != 0;
        } else {
            if ((s[swi] & fwm) != 0 || (s[ewi] & lwm) != 0) {
                return true;
            }
            for (int k = swi + 1; k < ewi; k++) {
                if (s[k] != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public static long[] not(long[] s) {
        for (int i = 0; i < s.length; i++) {
            s[i] = ~s[i];
        }
        return s;
    }

    public boolean secs(long[] s1, long[] s2) {
        for (int i = 0; i < s1.length; i++) {
            if ((s1[i] & s2[i]) != 0) {
                return true;
            }
        }
        return false;
    }

    public long[] assign(long[] s1, long[] s2) {
        System.arraycopy(s2, 0, s1, 0, s1.length);
        return s1;
    }

    public static long[] or(long[] s1, long[] s2) {
        for (int i = 0; i < s1.length; i++) {
            s1[i] |= s2[i];
        }
        return s1;
    }

    public static long[] and(long[] s1, long[] s2) {
        for (int i = 0; i < s1.length; i++) {
            s1[i] &= s2[i];
        }
        return s1;
    }

    public static long[] xor(long[] s1, long[] s2) {
        for (int i = 0; i < s1.length; i++) {
            s1[i] ^= s2[i];
        }
        return s1;
    }

    public static long[] add(long[] s1, long[] s2) {
        long t = 0;
        for (int i = 0; i < s1.length; i++) {
            s1[i] += t;
            long x = getLast(s1[i]) + getLast(s2[i]) + getLast((s1[i] & UWORD_MASK) + (s2[i] & UWORD_MASK));
            s1[i] += s2[i];
            t = x > 1 ? 1 : 0;
        }
        return s1;
    }

    public static long[] andNot(long[] s1, long[] s2) {
        for (int i = 0; i < s1.length; i++) {
            s1[i] &= ~s2[i];
        }
        return s1;
    }

    public static long[] shiftLeft(long[] s, int shift) {
        long msk = (1L << shift) - 1;
        int rsh = BITS_PER_WORD - shift;
        for (int i = s.length; i-- > 1; ) {
            s[i] = s[i] << shift | s[i - 1] >>> rsh & msk;
        }
        s[0] <<= shift;
        return s;
    }

    public static long[] shiftRight(long[] s, int shift) {
        int lsh = BITS_PER_WORD - shift;
        int last = s.length - 1;
        for (int i = 0; i < last; i++) {
            s[i] = s[i] >>> shift | s[i + 1] << lsh;
        }
        s[last] >>>= shift;
        return s;
    }

    public int nextSetBit(long[] s, int i) {
        int j = i >> ADDRESS_BITS_PER_WORD;
        if (j >= s.length) return -1;

        long w = s[j] & (WORD_MASK << i);
        while (true) {
            if (w != 0)
                return (j * BITS_PER_WORD) + Long.numberOfTrailingZeros(w);
            if (++j == s.length) return -1;
            w = s[j];
        }
    }

    public static String toString(long[] s) {
        StringBuilder ret = new StringBuilder(s.length * BITS_PER_WORD);
        for (int i = ret.capacity(); i-- > 0; ) {
            ret.append(get(s, i) ? '1' : '0');
        }
        return ret.toString();
    }

    public static int compare(long[] s1, long[] s2) {
        int n = s1.length, d = n - s2.length;
        if (d != 0) {
            return d;
        }
        for (int i = 0; i < n; i++) {
            long x = s1[i], y = s2[i];
            if (x != y) {
                return x < y ? -1 : 1;
            }
        }
        return 0;
    }

}
