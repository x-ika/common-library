package com.simplejcode.commons.misc.structures;

import java.util.Arrays;

public class LongBitSet {

    private static final int ADDRESS_BITS_PER_WORD = 6;
    private static final int BITS_PER_WORD = 1 << ADDRESS_BITS_PER_WORD;
    private static final int BIT_INDEX_MASK = BITS_PER_WORD - 1;
    private static final long WORD_MASK = 0xffffffffffffffffL;
    private static final long UWORD_MASK = 0x7fffffffffffffffL;

    private final long[] word;

    public LongBitSet(int nBits) {
        word = new long[calcSize(nBits)];
    }

    public int cardinality() {
        int sum = 0;
        for (long x : word) {
            sum += Long.bitCount(x);
        }
        return sum;
    }

    public void set(int i) {
        word[i >> ADDRESS_BITS_PER_WORD] |= 1L << (i & BIT_INDEX_MASK);
    }

    public boolean get(int i) {
        return (word[i >> ADDRESS_BITS_PER_WORD] & 1L << (i & BIT_INDEX_MASK)) != 0;
    }

    public LongBitSet init(long msk) {
        Arrays.fill(word, msk);
        return this;
    }

    public LongBitSet init(LongBitSet set) {
        System.arraycopy(set.word, 0, word, 0, word.length);
        return this;
    }

    public LongBitSet shiftLeft(int shift) {
        long msk = (1L << shift) - 1;
        int rsh = BITS_PER_WORD - shift;
        for (int i = word.length; i-- > 1; ) {
            word[i] = word[i] << shift | word[i - 1] >>> rsh & msk;
        }
        word[0] <<= shift;
        return this;
    }

    public LongBitSet shiftRight(int shift) {
        int lsh = BITS_PER_WORD - shift;
        int last = word.length - 1;
        for (int i = 0; i < last; i++) {
            word[i] = word[i] >>> shift | word[i + 1] << lsh;
        }
        word[last] >>>= shift;
        return this;
    }

    public LongBitSet not() {
        for (int i = 0; i < word.length; i++) {
            word[i] = ~word[i];
        }
        return this;
    }

    public LongBitSet or(LongBitSet set) {
        for (int i = 0; i < word.length; i++) {
            word[i] |= set.word[i];
        }
        return this;
    }

    public LongBitSet and(LongBitSet set) {
        for (int i = 0; i < word.length; i++) {
            word[i] &= set.word[i];
        }
        return this;
    }

    public LongBitSet xor(LongBitSet set) {
        for (int i = 0; i < word.length; i++) {
            word[i] ^= set.word[i];
        }
        return this;
    }

    public LongBitSet add(LongBitSet set) {
        long t = 0;
        for (int i = 0; i < word.length; i++) {
            long w = set.word[i];
            word[i] += t;
            long x = getLast(word[i]) + getLast(w) + getLast((word[i] & UWORD_MASK) + (w & UWORD_MASK));
            word[i] += w;
            t = x > 1 ? 1 : 0;
        }
        return this;
    }

    public LongBitSet andNot(LongBitSet set) {
        for (int i = 0; i < word.length; i++) {
            word[i] ^= set.word[i];
        }
        return this;
    }

    public int nextSetBit(int i) {
        int j = i >> ADDRESS_BITS_PER_WORD;
        if (j >= word.length) return -1;

        long w = word[j] & (WORD_MASK << i);
        while (true) {
            if (w != 0)
                return (j * BITS_PER_WORD) + Long.numberOfTrailingZeros(w);
            if (++j == word.length) return -1;
            w = word[j];
        }
    }

    public String toString() {
        StringBuilder ret = new StringBuilder(word.length * BITS_PER_WORD);
        for (int i = ret.capacity(); i-- > 0; ) {
            ret.append(get(i) ? '1' : '0');
        }
        return ret.toString();
    }

    private static int calcSize(int nBits) {
        return (nBits - 1 >> ADDRESS_BITS_PER_WORD) + 1;
    }

    private static long getLast(long x) {
        return x >>> BIT_INDEX_MASK & 1L;
    }

}
