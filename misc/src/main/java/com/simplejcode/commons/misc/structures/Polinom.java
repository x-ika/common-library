package com.simplejcode.commons.misc.structures;

public class Polinom {

    private static final String VAR = "x";

    private Rational[] coefs;

    public Polinom(int deg) {
        coefs = new Rational[deg + 1];
    }

    public Polinom(Rational... coefs) {
        this(coefs.length - 1);
        for (int i = 0; i < coefs.length; i++) {
            this.coefs[i] = new Rational(coefs[i]);
        }
    }

    public Polinom(double... coefs) {
        this(coefs.length - 1);
        for (int i = 0; i < coefs.length; i++) {
            this.coefs[i] = new Rational(coefs[i]);
        }
    }

    public static Polinom getPolinom(Rational firstCoef, Rational... roots) {
        Polinom p = new Polinom(firstCoef);
        for (Rational root : roots) {
            p.multiply(new Polinom(root.minus(), new Rational(1)));
        }
        return p;
    }

    public static Polinom sum(Polinom... polinoms) {
        int length = 0;
        for (Polinom p : polinoms) {
            length = Math.max(length, p.coefs.length);
        }
        Polinom sum = new Polinom(length - 1);
        for (int i = 0; i < length; i++) {
            sum.coefs[i] = new Rational(1);
        }
        for (Polinom p : polinoms) {
            for (int i = 0; i < p.coefs.length; i++) {
                sum.coefs[i].add(p.coefs[i]);
            }
        }
        return sum;
    }

    public Rational value(Rational r) {
        Rational value = new Rational();
        for (int i = 0; i < coefs.length; i++) {
            value.add(Rational.product(coefs[i], Rational.pow(r, i)));
        }
        return value;
    }

    public Polinom add(Polinom p) {
        coefs = sum(this, p).coefs;
        return this;
    }

    public Polinom multiply(Polinom p) {
        Rational[] newCoefs = new Rational[coefs.length + p.coefs.length - 1];
        for (int i = 0; i < newCoefs.length; i++) {
            newCoefs[i] = new Rational();
        }
        for (int i = 0; i < coefs.length; i++) {
            for (int j = 0; j < p.coefs.length; j++) {
                newCoefs[i + j].add(Rational.product(coefs[i], p.coefs[j]));
            }
        }
        coefs = newCoefs;
        return this;
    }

    public Polinom divide(Polinom p) {
        if (coefs.length < p.coefs.length) {
            return new Polinom(new Rational());
        }
        Rational[] newCoefs = new Rational[coefs.length - p.coefs.length + 1];
        Polinom temp = new Polinom(coefs);
        for (int i = newCoefs.length; --i >= 0; ) {
            newCoefs[i] = Rational.division(
                    temp.coefs[p.coefs.length + i - 1],
                    p.coefs[p.coefs.length - 1]);
            for (int j = 0; j < p.coefs.length; j++) {
                temp.coefs[i + j].subtract(
                        Rational.product(newCoefs[i], p.coefs[j]));
            }
        }
        coefs = newCoefs;
        return this;
    }

    public Rational getRoot() {
        return new Rational();
    }

    @Deprecated
    public Rational[] getRoots() {
        Polinom temp = new Polinom(coefs);
        Rational root;
        while ((root = temp.getRoot()) != null) {
            System.out.println(root);
            temp.divide(getPolinom(new Rational(1), root));
        }
        return null;
    }

    public String toString() {
        String line = coefs[0].toString();
        for (int i = 1; i < coefs.length; i++) {
            line += " + " + coefs[i] + "*" + VAR + "^" + i;
        }
        return line;
    }

}
