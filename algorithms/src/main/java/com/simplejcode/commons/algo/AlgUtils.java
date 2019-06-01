package com.simplejcode.commons.algo;

import java.math.*;
import java.util.*;

@SuppressWarnings({"StatementWithEmptyBody"})
public final class AlgUtils {

    private AlgUtils() {
    }

    //-----------------------------------------------------------------------------------

    public static double harmonicSum(long n) {
        if (n < 100) {
            double res = 0;
            for (int i = 1; i <= n; i++) {
                res += 1d / i;
            }
            return res;
        }
        double a = 0.57721566490153286060;
        double n2 = (double) n * n, n4 = n2 * n2;
        return Math.log(n) + a + 1d / (2 * n) - 1d / (12 * n2) + 1d / (120 * n4);
    }

    public static long fact(int n) {
        long p = 1;
        while (n > 1) {
            p *= n--;
        }
        return p;
    }

    public static long comb(int n, int m) {
        return fact(n) / fact(m) / fact(n - m);
    }

    public static int gcd(int n, int m) {
        int big, little;
        if (n < m) {
            big = m;
            little = n;
        } else {
            big = n;
            little = m;
        }
        while (little != 0) {
            int temp = little;
            little = big % little;
            big = temp;
        }
        return big;
    }

    public static long gcd(long n, long m) {
        long big, little;
        if (n < m) {
            big = m;
            little = n;
        } else {
            big = n;
            little = m;
        }
        while (little != 0) {
            long temp = little;
            little = big % little;
            big = temp;
        }
        return big;
    }

    public static int getPower(long n, long p) {
        int r = 0;
        for (long pow = p; pow <= n; r += n / pow, pow *= p) ;
        return r;
    }

    public static long pow(long a, long b, long m) {
        if (b == 0) {
            return 1;
        }
        long t = pow(a * a % m, b >> 1, m);
        if ((b & 1) == 1) {
            t = t * a % m;
        }
        return t;
    }

    public static int[] getDigits(int n) {
        char[] chars = String.valueOf(n).toCharArray();
        int[] r = new int[chars.length];
        for (int i = 0; i < r.length; i++) {
            r[i] = chars[i] - '0';
        }
        return r;
    }

    public static int getNumber(int[] digits) {
        int r = 0;
        for (int digit : digits) {
            r *= 10;
            r += digit;
        }
        return r;
    }

    public static int[] getPrimes(int max) {
        double log = Math.log(max);
        int[] p = new int[(int) (max / log * (1 + 1 / log + 2.51 / log / log))];
        p[0] = 2;
        int size = 1;
        int[] sieve = getSieve(max);
        for (int i = 3; i <= max; i += 2) {
            if (check(sieve, i)) {
                p[size++] = i;
            }
        }
        int[] r = new int[size];
        System.arraycopy(p, 0, r, 0, size);
        return r;
    }

    public static int[] getSieve(int max) {
        int[] r = new int[max + 64 >> 6];
        r[0] = 1;
        for (int x = 3; x * x <= max; x += 2) {
            if (check(r, x)) {
                for (int y = x * x; y <= max; y += x << 1) {
                    r[y >> 6] |= 1 << (y >> 1 & 31);
                }
            }
        }
        return r;
    }

    private static boolean check(int[] sieve, int x) {
        return (sieve[x >> 6] & 1 << (x >> 1 & 31)) == 0;
    }

    public static boolean isPrime(int[] sieve, int x) {
        return x == 2 || (x & 1) == 1 && check(sieve, x);
    }

    public static long getInverse(long A, long p) {
        long a = p;
        long b = A;
        long x = 1;
        long y = 0;
        while (b != 0) {
            long t = b;
            long q = a / t;
            b = a - q * t;
            a = t;
            t = x;
            x = y - q * t;
            y = t;
        }
        return y < 0 ? y + p : y;
    }

    public static List<Long> getFactorization(long n, int[] p) {
        List<Long> r = new ArrayList<>();
        for (int i = 0; n != 1 && i < p.length; i++) {
            while (n % p[i] == 0) {
                r.add(1L * p[i]);
                n /= p[i];
            }
        }
        if (n > 1) {
            r.add(n);
        }
        return r;
    }

    public static List<Long> getFactors(List<Long> primeDivisors) {
        List<Long> res = new ArrayList<>();
        go(1, 0, primeDivisors, res);
        Collections.sort(res);
        return res;
    }

    private static void go(long p, int i, List<Long> primeDivisors, List<Long> res) {
        if (i == primeDivisors.size()) {
            res.add(p);
            return;
        }
        long v = primeDivisors.get(i);
        int j = i;
        while (j < primeDivisors.size() && v == primeDivisors.get(j)) {
            j++;
        }
        while (i++ <= j) {
            go(p, j, primeDivisors, res);
            p *= v;
        }
    }

    public static long fi(long n, int[] p) {
        Set<Long> factors = new HashSet<>(getFactorization(n, p));
        long ret = n;
        for (long f : factors) {
            ret /= f;
            ret *= f - 1;
        }
        return ret;
    }

    public static double toDouble(BigDecimal p, BigDecimal q, double a, double b) {
        double c = (a + b) / 2;
        BigDecimal d = p.subtract(new BigDecimal(c).multiply(q));
        return d.abs().compareTo(q.multiply(new BigDecimal(1e-12))) < 0 ? c :
                d.signum() > 0 ? toDouble(p, q, c, b) : toDouble(p, q, a, c);
    }

    public static BigInteger[][] getCombs(int n) {
        BigInteger[][] r = new BigInteger[n][n];
        for (int i = 1; i < n; i++) {
            r[0][0] = r[i][0] = r[i][i] = BigInteger.ONE;
            for (int j = 1; j < i; j++) {
                r[i][j] = r[i - 1][j].add(r[i - 1][j - 1]);
            }
        }
        return r;
    }

    public static int[][] getCombsMod(int n, int m) {
        int[][] r = new int[n][n];
        for (int i = 0; i < n; i++) {
            r[i][0] = r[i][i] = 1;
            for (int j = 1; j < i; j++) {
                if ((r[i][j] = r[i - 1][j] + r[i - 1][j - 1]) >= m) {
                    r[i][j] -= m;
                }
            }
        }
        return r;
    }

    //--------------------------------- Permutation -------------------------------------

    public static boolean nextPermutation(int[] a) {
        if (a.length <= 1) {
            return false;
        }
        int next = a.length - 1;
        while (true) {
            int next1 = next;
            if (a[--next] < a[next1]) {
                int middle = a.length;
                while (a[--middle] < a[next]) ;
                swap(a, next, middle);
                reverse(a, next1, a.length);
                return (true);
            }
            if (next == 0) {
                reverse(a, 0, a.length);
                return false;
            }
        }
    }

    private static void reverse(int[] a, int first, int last) {
        while (first != last && first != --last) {
            swap(a, first++, last);
        }
    }

    public static void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    public static long indexOf(int[] a) {
        int n = a.length;
        long r = 0, fact = fact(n);
        for (int i = 0; i < n; i++) {
            fact /= n - i;
            for (int j = i + 1; j < n; j++) {
                if (a[i] > a[j]) {
                    r += fact;
                }
            }
        }
        return r;
    }

    public static int[] getPermutation(int n, long k) {
        long[] f = new long[16];
        f[0] = 1;
        for (int i = 1; i < f.length; i++) {
            f[i] = i * f[i - 1];
        }
        boolean[] b = new boolean[n];
        int[] r = new int[n];
        for (int i = 0; i < n; i++) {
            int x = (int) (k / f[n - i - 1]);
            for (r[i] = 0; ; r[i]++) {
                if (!b[r[i]] && x-- == 0) {
                    b[r[i]] = true;
                    break;
                }
            }
            k %= f[n - i - 1];
        }
        return r;
    }

    public static List<Integer> getCycles(int[] a) {
        List<Integer> r = new ArrayList<>();
        boolean[] b = new boolean[a.length];
        for (int e : a) {
            int z = 0;
            for (int x = e; !b[x]; b[x] = true, x = a[x], z++) ;
            if (z > 0) {
                r.add(z);
            }
        }
        return r;
    }

}
