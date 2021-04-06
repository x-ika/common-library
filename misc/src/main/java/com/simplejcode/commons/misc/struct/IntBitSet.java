package com.simplejcode.commons.misc.struct;

import java.util.Arrays;

public class IntBitSet {

    private static final int ADDRESS_BITS_PER_WORD = 5;
    private static final int BITS_PER_WORD = 1 << ADDRESS_BITS_PER_WORD;
    private static final int BIT_INDEX_MASK = BITS_PER_WORD - 1;
    private static final int WORD_MASK = 0xffffffff;
    private static final int UWORD_MASK = 0x7fffffff;


    private final int[] word;


    public IntBitSet(int nBits) {
        word = new int[calcSize(nBits)];
    }


    public int cardinality() {
        int sum = 0;
        for (int x : word) {
            sum += Integer.bitCount(x);
        }
        return sum;
    }

    public void set(int i) {
        word[i >> ADDRESS_BITS_PER_WORD] |= 1 << (i & BIT_INDEX_MASK);
    }

    public boolean get(int i) {
        return (word[i >> ADDRESS_BITS_PER_WORD] & 1 << (i & BIT_INDEX_MASK)) != 0;
    }

    public IntBitSet init(int msk) {
        Arrays.fill(word, msk);
        return this;
    }

    public IntBitSet init(IntBitSet set) {
        System.arraycopy(set.word, 0, word, 0, word.length);
        return this;
    }

    public IntBitSet shiftLeft(int shift) {
        int msk = (1 << shift) - 1;
        int rsh = BITS_PER_WORD - shift;
        for (int i = word.length; i-- > 1; ) {
            word[i] = word[i] << shift | word[i - 1] >>> rsh & msk;
        }
        word[0] <<= shift;
        return this;
    }

    public IntBitSet shiftRight(int shift) {
        int lsh = BITS_PER_WORD - shift;
        int last = word.length - 1;
        for (int i = 0; i < last; i++) {
            word[i] = word[i] >>> shift | word[i + 1] << lsh;
        }
        word[last] >>>= shift;
        return this;
    }

    public IntBitSet not() {
        for (int i = 0; i < word.length; i++) {
            word[i] = ~word[i];
        }
        return this;
    }

    public IntBitSet or(IntBitSet set) {
        for (int i = 0; i < word.length; i++) {
            word[i] |= set.word[i];
        }
        return this;
    }

    public IntBitSet and(IntBitSet set) {
        for (int i = 0; i < word.length; i++) {
            word[i] &= set.word[i];
        }
        return this;
    }

    public IntBitSet xor(IntBitSet set) {
        for (int i = 0; i < word.length; i++) {
            word[i] ^= set.word[i];
        }
        return this;
    }

    public IntBitSet add(IntBitSet set) {
        int t = 0;
        for (int i = 0; i < word.length; i++) {
            int w = set.word[i];
            word[i] += t;
            int x = getLast(word[i]) + getLast(w) + getLast((word[i] & UWORD_MASK) + (w & UWORD_MASK));
            word[i] += w;
            t = x > 1 ? 1 : 0;
        }
        return this;
    }

    public IntBitSet andNot(IntBitSet set) {
        for (int i = 0; i < word.length; i++) {
            word[i] ^= set.word[i];
        }
        return this;
    }

    public int nextSetBit(int i) {
        int j = i >> ADDRESS_BITS_PER_WORD;
        if (j >= word.length) return -1;

        int w = word[j] & (WORD_MASK << i);
        while (true) {
            if (w != 0)
                return (j * BITS_PER_WORD) + Integer.numberOfTrailingZeros(w);
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

    private static int getLast(int x) {
        return x >>> BIT_INDEX_MASK & 1;
    }

}
