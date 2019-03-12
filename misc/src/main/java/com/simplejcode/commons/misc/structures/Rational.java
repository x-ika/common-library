package com.simplejcode.commons.misc.structures;

public class Rational {

    private long num;

    private long denum;

    public Rational(Rational r) {
        num = r.num;
        denum = r.denum;
    }

    public Rational(long numerator, long denumerator) {
        init(numerator, denumerator);
    }

    public Rational(double value) {
        String s = String.valueOf(value);
        int n = s.length() - s.indexOf('.') - 1;
        long q = (long) Math.pow(10, n);
        long p = (long) (q * value);
        init(p, q);
    }

    public Rational(long value) {
        num = value;
        denum = 1;
    }

    public Rational() {
        this(0);
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

    public static Rational sum(Rational r1, Rational r2) {
        return new Rational(r1.num * r2.denum + r1.denum * r2.num, r1.denum * r2.denum);
    }

    public static Rational diff(Rational r1, Rational r2) {
        return new Rational(r1.num * r2.denum - r1.denum * r2.num, r1.denum * r2.denum);
    }

    public static Rational product(Rational r1, Rational r2) {
        return new Rational(r1.num * r2.num, r1.denum * r2.denum);
    }

    public static Rational division(Rational r1, Rational r2) {
        return new Rational(r1.num * r2.denum, r1.denum * r2.num);
    }

    public static Rational pow(Rational r1, int n) {
        long num = 1;
        long denum = 1;
        while (n-- != 0) {
            num *= r1.num;
            denum *= r1.denum;
        }
        return new Rational(num, denum);
    }

    public void init(long numerator, long denumerator) {
        if (denumerator == 0) {
            throw new ArithmeticException("denumerator = 0");
        }
        long gcd = gcd(numerator, denumerator);
        if (Math.signum(gcd) != Math.signum(denumerator)) {
            gcd = -gcd;
        }
        num = numerator / gcd;
        denum = denumerator / gcd;
    }

    public boolean isZero() {
        return num == 0;
    }

    public boolean isEqual(Rational r) {
        return num * r.denum == denum * r.num;
    }

    public boolean isBigger(Rational r) {
        return num * r.denum > denum * r.num;
    }

    public boolean isLess(Rational r) {
        return num * r.denum < denum * r.num;
    }

    public boolean isBiggerOrEqual(Rational r) {
        return num * r.denum >= denum * r.num;
    }

    public boolean isLessOrEqual(Rational r) {
        return num * r.denum <= denum * r.num;
    }

    public long longValue() {
        return num / denum;
    }

    public double doubleValue() {
        return (double) num / denum;
    }

    public Rational minus() {
        num = -num;
        return this;
    }

    public Rational add(long p, long q) {
        long d = gcd(denum, q);
        num = (num * q + denum * p) / d;
        denum = denum * q / d;
        long gcd = gcd(num, denum);
        if (Math.signum(gcd) != Math.signum(denum)) {
            gcd = -gcd;
        }
        num /= gcd;
        denum /= gcd;
        return this;
    }

    public Rational subtract(long p, long q) {
        return add(-p, q);
    }

    public Rational multiply(long p, long q) {
        long d = gcd(num, q) * gcd(denum, p);
        num = num * p / d;
        denum = denum * q / d;
        return this;
    }

    public Rational divide(long p, long q) {
        return multiply(q, p);
    }

    public Rational add(Rational r) {
        return add(r.num, r.denum);
    }

    public Rational subtract(Rational r) {
        return add(-r.num, r.denum);
    }

    public Rational multiply(Rational r) {
        return multiply(r.num, r.denum);
    }

    public Rational divide(Rational r) {
        return multiply(r.denum, r.num);
    }

    public String toString() {
        if (Math.abs(denum) == 1) {
            return String.valueOf(longValue());
        }
        return "(" + num + "/" + denum + ")";
    }

}
