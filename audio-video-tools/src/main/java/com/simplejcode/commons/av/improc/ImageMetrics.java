package com.simplejcode.commons.av.improc;

import java.util.Arrays;

public final class ImageMetrics {
    private static final int N = 12, M = 6;
    //private static final double L = 0.657;

    private static int index, W, H;

    private static final double[] C = new double[M];
    private static final double[][] X = new double[N][M];
    private static final double[] Y = new double[N];

    public static double getHeight(double i, double j, double h) {
        return h * Math.sqrt(get(i, j));
    }

    public static void processImage(int w, int h, int[] image) {
        W = w;
        H = h;
        if (index == N) {
            return;
        }

        for (int i = 0; i < w * h; i++) {
            image[i] &= (1 << 24) - 1;
        }

        //ImageProcessor processor = new ImageProcessor(W, H);

        double oi = 0, oj = 0, oh = 0;

        M:
        for (int p = 0; p < w * h; p++) {
            if (isMarked(image, p)) {
                for (int q = 0; q < p; q++) {
                    if (isMarked(image, q)) {
                        //oh = Math.hypot(processor.I[p] - I[q], J[p] - J[q]);
                        //oi = I[p];
                        //oj = J[p];
                        break M;
                    }
                }
            }
        }

        System.out.printf("%d (%.2f %.2f) %.2f\n", index, oi, oj, oh);

        if (M == 3) {
            X[index][0] = oi;
            X[index][1] = oj;
            X[index][2] = 1;
        } else {
            X[index][0] = oi * oi;
            X[index][1] = oj * oj;
            X[index][2] = oi * oj;
            X[index][3] = oi;
            X[index][4] = oj;
            X[index][5] = 1;
            Y[index] = 1 / oh / oh;
        }

        if (++index == N) {

            for (int i = 0; i < 1 << N; i++) {
                if (Integer.bitCount(i) >= N) {

                    double[][] A = new double[Integer.bitCount(i)][M];
                    double[] B = new double[Integer.bitCount(i)];
                    for (int j = 0, k = 0; j < N; j++) {
                        if ((i & 1 << j) != 0) {
                            System.arraycopy(X[j], 0, A[k], 0, M);
                            B[k++] = Y[j];
                        }
                    }
                    Arrays.fill(C, 0);
                    leastSquares(A, B, C);
                    System.out.println(Integer.toBinaryString(i) + ": " + Arrays.toString(C));

                }
            }

            for (int i = 0; i < N; i++) {
                System.out.printf("%f %f \n", get(X[i]), Y[i]);
            }
            for (int p = 0; p < w * h; p++) {
                //System.out.println(get(I[p], J[p]));
            }

        }
    }


    private static double get(double i, double j) {
        return M == 3 ? get(i, j, 1) : get(i * i, j * j, i * j, i, j, 1);
    }

    private static double get(double... X) {
        double v = 0;
        for (int i = 0; i < M; i++) {
            v += X[i] * C[i];
        }
        return v;
    }

    private static boolean isMarked(int[] c, int p) {
        int v = 128 << 16 | 128;
        return c[p] == v && c[p + 1] == v && c[p + W] == v && c[p + W + 1] == v;
    }

    private static void leastSquares(double[][] X, double[] Y, double[] out) {

        int n = X.length, m = X[0].length;
        double[][] A = new double[m][m];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < n; k++) {
                    A[i][j] += X[k][i] * X[k][j];
                }
            }

            for (int j = 0; j < n; j++) {
                out[i] += Y[j] * X[j][i];
            }
        }

        gaussMethod(A, out);

    }

    private static void gaussMethod(double[][] A, double[] b) {
        for (int i = 0, n = A.length; i < n; i++) {
            double f = 1 / A[i][i];
            b[i] *= f;
            for (int j = 0; j < n; j++) {
                A[i][j] *= f;
            }

            for (int j = 0; j < n; j++) {
                if (i != j) {
                    f = A[j][i];
                    b[j] -= b[i] * f;
                    for (int k = 0; k < n; k++) {
                        A[j][k] -= A[i][k] * f;
                    }
                }
            }
        }
    }
}
