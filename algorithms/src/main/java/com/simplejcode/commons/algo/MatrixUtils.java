package com.simplejcode.commons.algo;

import java.util.Arrays;

public final class MatrixUtils {

    private MatrixUtils() {
    }

    //-----------------------------------------------------------------------------------

    public static double det9(double... a) {

        // 0 1 2
        // 3 4 5
        // 6 7 8

        double v1 = a[0] * (a[4] * a[8] - a[5] * a[7]);
        double v2 = a[3] * (a[1] * a[8] - a[2] * a[7]);
        double v3 = a[6] * (a[1] * a[5] - a[2] * a[4]);

        return v1 - v2 + v3;

    }

    //-----------------------------------------------------------------------------------

    public static Rational[] getCopy(Rational[] src) {
        Rational[] d = new Rational[src.length];
        for (int i = 0; i < d.length; i++) {
            d[i] = new Rational(src[i]);
        }
        return d;
    }

    public static Rational[][] getCopy(Rational[][] src) {
        Rational[][] m = new Rational[src.length][src.length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m.length; j++) {
                m[i][j] = new Rational(src[i][j]);
            }
        }
        return m;
    }

    public static void normalize(Rational[][] matrix) {
        for (int col = 0; col < matrix.length; col++) {
            for (int row = col + 1; row < matrix.length; row++) {
                if (matrix[row][col].isZero()) {
                    continue;
                }
                Rational coef = Rational.division(
                        matrix[row][col], matrix[col][col]);
                for (int i = 0; i < matrix.length; i++) {
                    matrix[row][i].subtract(
                            Rational.product(coef, matrix[col][i]));
                }
            }
        }
    }

    public static void normalize(Rational[][] matrix, Rational[] data) {
        for (int col = 0; col < matrix.length; col++) {
            for (int line = col + 1; line < matrix.length; line++) {
                if (matrix[line][col].isZero()) {
                    continue;
                }
                Rational coef = Rational.division(
                        matrix[line][col], matrix[col][col]);
                for (int i = 0; i < matrix.length; i++) {
                    matrix[line][i].subtract(
                            Rational.product(coef, matrix[col][i]));
                }
                data[line].subtract(
                        Rational.product(coef, data[col]));
            }
        }
    }

    public static Rational diagProduct(Rational[][] matrix) {
        Rational product = new Rational(1);
        for (int i = 0; i < matrix.length; i++) {
            product.multiply(matrix[i][i]);
        }
        return product;
    }

    public static Rational det(Rational[][] matrix) {
        Rational[][] m = getCopy(matrix);
        normalize(m);
        return diagProduct(m);
    }

    public static Rational[] solution(Rational[][] matrix, Rational[] data) {
        Rational[][] m = getCopy(matrix);
        Rational[] sol = getCopy(data);
        normalize(m, sol);
        for (int col = sol.length; col-- > 0; ) {
            if (m[col][col].isZero()) {
                if (!sol[col].isZero()) {
                    throw new ArithmeticException("no solution");
                }
                continue;
            }
            sol[col].divide(m[col][col]);
            for (int line = col; line-- > 0; ) {
                sol[line].subtract(m[line][col].multiply(sol[col]));
            }
        }
        return sol;
    }

    public static Rational[] solve(int[][] matrix, int[] vector) {
        // copy O(n*n)
        int n = matrix.length;
        Rational[][] m = new Rational[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                m[i][j] = new Rational(matrix[i][j]);
            }
        }
        Rational[] sol = new Rational[n];
        for (int i = 0; i < n; i++) {
            sol[i] = new Rational(vector[i]);
        }
        // normalize O(n*n*n)
        for (int col = 0; col < n; col++) {
            for (int row = col + 1; row < n; row++) {
                if (m[row][col].isZero()) {
                    continue;
                }
                Rational coef = Rational.division(m[row][col], m[col][col]);
                for (int i = 0; i < n; i++) {
                    m[row][i].subtract(Rational.product(coef, m[col][i]));
                }
                sol[row].subtract(Rational.product(coef, sol[col]));
            }
        }
        // solve O(n*n)
        for (int col = n; col-- > 0; ) {
            if (m[col][col].isZero()) {
                return sol[col].isZero() ? new Rational[]{null} : null;
            }
            sol[col].divide(m[col][col]);
            for (int line = col; line-- > 0; ) {
                sol[line].subtract(m[line][col].multiply(sol[col]));
            }
        }
        return sol;
    }

    public static String toString(Rational[][] matrix, Rational[] data) {
        String str = "";
        for (int line = 0; line < matrix.length; line++) {
            for (Rational elem : matrix[line]) {
                str += elem + " ";
            }
            str += data[line] + "\n";
        }
        return str;
    }

    //-----------------------------------------------------------------------------------

    public static double[] iterative(double[][] a, double[] b, int its) {
        int n = a.length;
        double[] x = new double[n];
        Arrays.fill(x, 1);
        double[][] ld = new double[n][n];
        double[][] u = new double[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(a[i], 0, ld[i], 0, i + 1);
            System.arraycopy(a[i], i + 1, u[i], i + 1, n - i - 1);
        }
        ld = invert(ld);
        double[][] ldu = new double[n][n];
        double[] ldb = new double[n];
        mult(ld, u, ldu);
        mult(ld, b, ldb);
        while (its-- > 0) {
            mult(ldu, x, x);
            for (int i = 0; i < n; i++) {
                x[i] = ldb[i] - x[i];
            }
        }
        return x;
    }

    private static void mult(double[][] a, double[][] b, double[][] c) {
        int n = a.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double t = 0;
                for (int k = 0; k < n; k++) {
                    t += a[i][k] * b[k][j];
                }
                c[i][j] = t;
            }
        }
    }

    private static void mult(double[][] a, double[] b, double[] c) {
        int n = a.length;
        for (int i = 0; i < n; i++) {
            double t = 0;
            for (int k = 0; k < n; k++) {
                t += a[i][k] * b[k];
            }
            c[i] = t;
        }
    }

    private static double[][] invert(double[][] ld) {
        return new double[0][];
    }

    public static double[] solveXtXA(double[][] x, double[] a) {
        double[][] rt = cholesky(getAtA(x));
        double[][] r = transposition(rt);
        double[] xtb = toVector(mult(toRow(a), x));
        double[] z = solveLower(rt, xtb);
        return solveUpper(r, z);
    }

    public static void normalize(double[][] matrix, double[] data) {
        for (int col = 0; col < matrix.length; col++) {
            for (int line = col + 1; line < matrix.length; line++) {
                if (matrix[line][col] == 0) {
                    continue;
                }
                double coef = matrix[line][col] / matrix[col][col];
                for (int i = 0; i < matrix.length; i++) {
                    matrix[line][i] -= coef * matrix[col][i];
                }
                data[line] -= coef * data[col];
            }
        }
    }

    public static double[] solve(double[][] matrix, double[] data) {
        double[][] m = new double[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            m[i] = matrix[i].clone();
        }
        double[] sol = data.clone();
        normalize(m, sol);
        for (int col = sol.length; col-- > 0; ) {
            if (m[col][col] == 0) {
                if (sol[col] != 0) {
                    throw new ArithmeticException("no solution");
                }
                continue;
            }
            sol[col] /= m[col][col];
            for (int line = col; line-- > 0; ) {
                sol[line] -= m[line][col] * sol[col];
            }
        }
        return sol;
    }

    public static double[] solveLower(double[][] a, double[] b) {
        int n = a.length;
        if (a[0].length != n || b.length != n) {
            return null;
        }
        double[] x = new double[n];
        for (int i = 0; i < n; i++) {
            double sum = 0;
            for (int j = 0; j < i; j++) {
                sum += a[i][j] * x[j];
            }
            x[i] = (b[i] - sum) / a[i][i];
        }
        return x;
    }

    public static double[] solveUpper(double[][] a, double[] b) {
        int n = a.length;
        if (a[0].length != n || b.length != n) {
            return null;
        }
        double[] x = new double[n];
        for (int i = n; i-- > 0; ) {
            double sum = 0;
            for (int j = i + 1; j < n; j++) {
                sum += a[i][j] * x[j];
            }
            x[i] = (b[i] - sum) / a[i][i];
        }
        return x;
    }

    public static double[][] cholesky(double[][] a) {
        int n = a.length;
        double[][] b = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                double sum = 0, p[] = b[i], q[] = b[j];
                for (int k = 0; k < j; k++) {
                    sum += p[k] * q[k];
                }
                if (i == j) {
                    b[i][i] = Math.sqrt(a[i][i] - sum);
                } else {
                    b[i][j] = 1 / b[j][j] * (a[i][j] - sum);
                }
            }
            if (b[i][i] <= 0) {
//                b[i][i] = 1e-6;
                throw new RuntimeException("not positive");
            }
        }
        return b;
    }

    public static double[] add(double[] a, double[] b) {
        int n = a.length;
        if (b.length != n) {
            return null;
        }
        double[] c = new double[n];
        for (int i = 0; i < n; i++) {
            c[i] = a[i] + b[i];
        }
        return c;
    }

    public static double[][] add(double[][] a, double[][] b) {
        int n = a.length;
        int m = a[0].length;
        if (b.length != n || b[0].length != m) {
            return null;
        }
        double[][] c = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                c[i][j] = a[i][j] + b[i][j];
            }
        }
        return c;
    }

    public static void divide(double[] a, double b) {
        int n = a.length;
        for (int i = 0; i < n; i++) {
            a[i] /= b;
        }
    }

    public static double[][] mult(double[][] a, double[][] b) {
        int n = a.length;
        int m = b.length;
        int k = b[0].length;
        if (a[0].length != m) {
            return null;
        }
        double[][] c = new double[n][k];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                double v = 0, p[] = a[i];
                for (int ij = 0; ij < m; ij++) {
                    v += p[ij] * b[ij][j];
                }
                c[i][j] = v;
            }
        }
        return c;
    }

    public static double[][] getAtA(double[][] a) {
        int n = a.length;
        int m = a[0].length;
        double[][] b = new double[m][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j <= i; j++) {
                double v = 0;
                for (int k = 0; k < n; k++) {
                    v += a[k][i] * a[k][j];
                }
                b[i][j] = b[j][i] = v;
            }
        }
        return b;
    }

    public static double[][] getAAt(double[][] a) {
        int n = a.length;
        int m = a[0].length;
        double[][] b = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                double v = 0, p[] = a[i], q[] = a[j];
                for (int k = 0; k < m; k++) {
                    v += p[k] * q[k];
                }
                b[i][j] = b[j][i] = v;
            }
        }
        return b;
    }

    public static double[][] transposition(double[][] a) {
        int n = a.length;
        int m = a[0].length;
        double[][] b = new double[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                b[i][j] = a[j][i];
            }
        }
        return b;
    }

    public static double[][] toRow(double[] v) {
        return new double[][]{v};
    }

    public static double[][] toColumn(double[] v) {
        int n = v.length;
        double[][] a = new double[n][1];
        for (int i = 0; i < n; i++) {
            a[i][0] = v[i];
        }
        return a;
    }

    public static double[] toVector(double[][] a) {
        int n = a.length;
        if (n == 1) {
            return a[0];
        }
        double[] v = new double[n];
        for (int i = 0; i < n; i++) {
            v[i] = a[i][0];
        }
        return v;
    }

    public static double[][] createIMatrix(int n) {
        final double[][] m = new double[n][n];
        for (int i = 0; i < n; ++i) {
            m[i][i] = 1;
        }
        return m;
    }

    public static double[][] copy(double[][] x) {
        double[][] a = new double[x.length][];
        for (int i = 0; i < x.length; i++) {
            a[i] = x[i].clone();
        }
        return a;
    }

    public static void print(double[][] a) {
        for (double[] b : a) {
            for (double v : b) {
                System.out.printf("%12.3f", v);
            }
            System.out.println();
        }
    }

    //-----------------------------------------------------------------------------------

    public static long[][] mult(long[][] a, long[][] b, long mod) {
        int n = a.length;
        int m = b.length;
        int k = b[0].length;

        long[][] c = new long[n][k];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                for (int ij = 0; ij < m; ij++) {
                    c[i][j] += a[i][ij] * b[ij][j];
                }
                c[i][j] %= mod;
            }
        }
        return c;
    }

    public static long[][] pow(long[][] a, long p, long mod) {
        if (p == 1) {
            return a;
        }
        if ((p & 1) == 1) {
            return mult(a, pow(a, p - 1, mod), mod);
        }
        return pow(mult(a, a, mod), p >> 1, mod);
    }

    public static void transpose(double[][] a) {
        int n = a.length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                double t = a[i][j];
                a[i][j] = a[j][i];
                a[j][i] = t;
            }
        }
    }

    private static double pythag(double a, double b) {
        double absa = Math.abs(a), absb = Math.abs(b);
        return absa > absb ? absa * Math.sqrt(1 + Math.pow(absb / absa, 2)) :
                absb == 0 ? 0 : absb * Math.sqrt(1 + Math.pow(absa / absb, 2));
    }

}
