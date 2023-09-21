package com.simplejcode.commons.misc.improc;

import java.awt.image.BufferedImage;
import java.util.List;

import static java.lang.Math.*;

/**
 * No comments in this class, it was hard to write so it should be hard to read.
 * You are not meant to understand this. (C)
 */
public class ImageProcessor {

    public static void rotate(Object[] x) {
        Object temp = x[x.length - 1];
        System.arraycopy(x, 0, x, 1, x.length - 1);
        x[0] = temp;
    }

    public static int sqr(int x) {
        return x * x;
    }

    public static int mx(int a, int b, int c) {
        return max(max(a, b), c);
    }

    public static int mn(int a, int b, int c) {
        return min(min(a, b), c);
    }

    //--------------------------------- Color Utils -------------------------------------

    public static int dist(int c1, int c2) {
        return sqr((c1 >> 16) - (c2 >> 16)) +
                sqr((c1 >> 8 & 255) - (c2 >> 8 & 255)) +
                sqr((c1 & 255) - (c2 & 255));
    }

    public static int dif(int c1, int c2) {
        return abs((c1 >> 16) - (c2 >> 16)) << 16 |
                abs((c1 >> 8 & 255) - (c2 >> 8 & 255)) << 8 |
                abs((c1 & 255) - (c2 & 255));
    }

    public static int color(int x) {
        return x << 16 | x << 8 | x;
    }

    public static int projection(int c, int x) {
        return (c >> 16) / x * x << 16 | (c >> 8 & 255) / x * x << 8 | (c & 255) / x * x;
    }

    public static float getBrightness(int c) {
        int r = c >>> 16 & 255;
        int g = c >>> 8 & 255;
        int b = c & 255;
        return (r + g + b) / 256f / 3;
    }

    /**
     * Converts rgb color to binary based on brightness
     *
     * @param c rgb color
     * @return 1/0 - black/white
     */
    public static int getBinaryColor(int c) {
        return 1 - round(getBrightness(c));
    }

    public static int[] getBlackWhiteImage(BufferedImage image) {
        return getBlackWhiteImage(image, 0, 0, image.getWidth(), image.getHeight());
    }

    public static float[] getBrightnessMap(BufferedImage image) {
        return getBrightnessMap(image, 0, 0, image.getWidth(), image.getHeight());
    }

    public static int[] getBlackWhiteImage(BufferedImage image, int x, int y, int w, int h) {
        int[] pixelData = image.getRGB(x, y, w, h, new int[w * h], 0, w);
        int[] c = new int[w * h];
        for (int i = 0; i < w * h; i++) {
            c[i] = getBinaryColor(pixelData[i]);
        }
        return c;
    }

    public static float[] getBrightnessMap(BufferedImage image, int x, int y, int w, int h) {
        int[] pixelData = image.getRGB(x, y, w, h, new int[w * h], 0, w);
        float[] c = new float[w * h];
        for (int i = 0; i < w * h; i++) {
            c[i] = getBrightness(pixelData[i]);
        }
        return c;
    }

    //--------------------------------- RGB to HSV --------------------------------------

    public static int getH(int rgb) {
        return getH(rgb >> 16, rgb >> 8 & 255, rgb & 255);
    }

    public static double getS(int rgb) {
        return getS(rgb >> 16, rgb >> 8 & 255, rgb & 255);
    }

    public static double getV(int rgb) {
        return getV(rgb >> 16, rgb >> 8 & 255, rgb & 255);
    }

    public static int getH(int r, int g, int b) {
        int mx = mx(r, g, b), mn = mn(r, g, b);
        double d = mx - mn;
        if (d == 0) {
            return 0;
        }
        double x = 0;
        if (mx == r) {
            x = (g - b) / d;
        }
        if (mx == g) {
            x = 2 + (b - r) / d;
        }
        if (mx == b) {
            x = 4 + (r - g) / d;
        }
        return (int) (60 * (x > 0 ? x : 6 - x));
    }

    public static double getS(int r, int g, int b) {
        int mx = mx(r, g, b);
        return mx == 0 ? 0 : 1d * mn(r, g, b) / mx;
    }

    public static double getV(int r, int g, int b) {
        return mx(r, g, b) / 256d;
    }

    public static int getRGB(int h, double s, double v) {
        double c = v * s;
        double x = c * (1 - abs((h / 60d) % 2 - 1));
        double m = v - c;
        if (h < 60) {
            return f(c, x, 0, m);
        }
        if (h < 120) {
            return f(x, c, 0, m);
        }
        if (h < 180) {
            return f(0, c, x, m);
        }
        if (h < 240) {
            return f(0, x, c, m);
        }
        if (h < 300) {
            return f(x, 0, c, m);
        }
        if (h < 360) {
            return f(c, 0, x, m);
        }
        return -1;
    }

    public static int f(double r, double g, double b, double m) {
        return (int) ((r + m) * 256) << 16 | (int) ((g + m) * 256) << 8 | (int) ((b + m) * 256);
    }

    public static int hsv2rgb(int h, double s, double v) {
        double hh, p, q, t, ff;

        hh = h;
        if (hh >= 360.0) hh = 0.0;
        hh /= 60.0;
        int i = (int) hh;
        ff = hh - i;
        p = v * (1.0 - s);
        q = v * (1.0 - (s * ff));
        t = v * (1.0 - (s * (1.0 - ff)));

        switch (i) {
            case 0:
                return f(v, t, p, 0);
            case 1:
                return f(q, v, p, 0);
            case 2:
                return f(p, v, t, 0);
            case 3:
                return f(p, q, v, 0);
            case 4:
                return f(t, p, v, 0);
            case 5:
            default:
                return f(v, p, q, 0);
        }
    }

    //--------------------------------- For Removing Shadow -----------------------------

    private static final double[][] ATAN2 = new double[256][256];

    static {
        for (int i = 0; i < ATAN2.length; i++) {
            for (int j = 0; j < ATAN2.length; j++) {
                ATAN2[i][j] = atan2(i, j);
            }
        }
    }

    public static double getC1(int rgb) {
        return ATAN2[rgb >> 16][max(rgb >> 8 & 255, rgb & 255)];
    }

    public static double getC2(int rgb) {
        return ATAN2[rgb >> 8 & 255][max(rgb >> 16, rgb & 255)];
    }

    public static double getC3(int rgb) {
        return ATAN2[rgb & 255][max(rgb >> 16, rgb >> 8 & 255)];
    }

    public static double fdist(int c1, int c2) {
        return abs(getC1(c1) - getC1(c2)) +
                abs(getC2(c1) - getC2(c2)) +
                abs(getC3(c1) - getC3(c2));
    }

    //-----------------------------------------------------------------------------------
    /*
    
    Tiny image processing API

     * W      -  width of the image
     * H      -  height of the image
     * S      -  area of the image
     * int p  -  pixel
     * I[p]   -  row of the pixel
     * J[p]   -  column of the pixel
     * D[d]   -  "d neighbourhood" of pixel

     */

    public int W, H, S;
    private int[] ZERO, I, J, D[];

    public ImageProcessor(int w, int h) {
        D = new int[50][];
        for (int i = 0; i < D.length; i++) {
            int d = 2 * i + 1;
            D[i] = new int[d * d];
        }
        setDimensions(w, h);
    }

    public void setDimensions(int w, int h) {
        S = (W = w) * (H = h);
        if (ZERO == null || ZERO.length < S) {
            ZERO = new int[S];
            I = new int[S];
            J = new int[S];
        }
        for (int p = 0; p < S; p++) {
            I[p] = p / W;
            J[p] = p % W;
        }
        for (int i = 0; i < D.length; i++) {
            int d = 2 * i + 1;
            int[] t = D[i];
            for (int j = 0; j < d * d; j++) {
                t[j] = (j / d - i) * W + j % d - i;
            }
        }
    }

    //-----------------------------------------------------------------------------------
    /*
    SUM calculations
    All coordinates are inclusive
     */

    private int[][] S_ROW, S_COL, S_AREA;

    public void initSumCalculationArrays() {
        if (S_ROW != null && S_ROW.length >= S) {
            return;
        }
        S_ROW = new int[3][S];
        S_COL = new int[3][S];
        S_AREA = new int[3][S];
    }

    /**
     * Calculates row and area sums in the given array to allow quick
     * sum calculation in rectangles
     *
     * @param f arrays to be precalculated
     */
    public void calculateRowColumnSums(int[]... f) {
        for (int i = 0; i < f.length; i++) {
            calculateRowColumnSums(i, f[i]);
        }
    }

    public void calculateRowColumnSums(int id, int[] a) {
        int[] f = S_AREA[id], g = S_ROW[id], h = S_COL[id];
        System.arraycopy(ZERO, 0, f, 0, S);
        System.arraycopy(ZERO, 0, g, 0, S);
        System.arraycopy(ZERO, 0, h, 0, S);
        for (int p = 0; p < S; p++) {
            g[p] = a[p] + (J[p] == 0 ? 0 : g[p - 1]);
            h[p] = a[p] + (p < W ? 0 : h[p - W]);
        }
        for (int p = 0; p < S; p++) {
            f[p] = h[p] + (J[p] == 0 ? 0 : f[p - 1]);
        }
    }

    public int getSum(int p, int q) {
        return getSum(0, p, q);
    }

    public int getSum(int id, int p, int q) {
        int[] f = S_AREA[id];
        if (I[p] == 0 && J[p] == 0) {
            return f[q];
        }
        if (I[p] == 0) {
            p--;
            return f[q] - f[I[q] * W + J[p]];
        }
        if (J[p] == 0) {
            p -= W;
            return f[q] - f[I[p] * W + J[q]];
        }
        p -= W + 1;
        return f[p] + f[q] - f[I[p] * W + J[q]] - f[I[q] * W + J[p]];
    }

    public int getRowSum(int i, int j1, int j2) {
        return getRowSum(0, i, j1, j2);
    }

    public int getRowSum(int id, int i, int j1, int j2) {
        j1--;
        i *= W;
        int[] f = S_ROW[id];
        return f[i + j2] - (j1 < 0 ? 0 : f[i + j1]);
    }

    public int getColSum(int j, int i1, int i2) {
        return getColSum(0, j, i1, i2);
    }

    public int getColSum(int id, int j, int i1, int i2) {
        i1--;
        i1 *= W;
        i2 *= W;
        int[] f = S_COL[id];
        return f[j + i2] - (i1 < 0 ? 0 : f[j + i1]);
    }

    //-----------------------------------------------------------------------------------
    /*
    Components Analysis
     */

    private int minI, maxI, minJ, maxJ;

    private int[] B, Q, T;

    public void initComponentAnalysisArrays() {
        if (B != null && B.length >= S) {
            return;
        }
        B = new int[S];
        Q = new int[S];
        T = new int[S];
    }

    public void deleteSmallComponents(int[] c, int d, int v, int n) {
        System.arraycopy(ZERO, 0, B, 0, S);
        System.arraycopy(ZERO, 0, T, 0, S);
        for (int p = 0; p < S; p++) {
            if (B[p] == 0 && c[p] == v) {
                int size = bfs(c, p, d, v, 1);
                if (size < n) {
                    bfs(c, p, d, 0, 2);
                }
            }
        }
        System.arraycopy(T, 0, c, 0, S);
    }

    public void separate(int[] c, int d, int max) {
        System.arraycopy(ZERO, 0, B, 0, S);
        System.arraycopy(ZERO, 0, T, 0, S);
        int cc = 0;
        for (int p = 0; p < S; p++) {
            if (B[p] == 0) {
                cc++;
                bfs(c, p, d, max);
            }
        }
        System.out.println("cc = " + cc);
        System.arraycopy(T, 0, c, 0, S);
    }

    public void buildRects(int[] c, int d, int n, List<Rect> l) {
        System.arraycopy(ZERO, 0, B, 0, S);
        System.arraycopy(ZERO, 0, T, 0, S);
        for (int p = 0; p < S; p++) {
            if (B[p] == 0 && c[p] > 0) {
                minI = minJ = S;
                maxI = maxJ = 0;
                if (bfs(c, p, d, 1) > n) {
                    l.add(create(minI * W + minJ, maxI * W + maxJ));
                }
            }
        }
        System.arraycopy(T, 0, c, 0, S);
    }

    private Rect create(int p, int q) {
        return new Rect(I[p], J[p], I[q] + 1, J[q] + 1);
    }


    private int bfs(int[] c, int s, int d, int max) {
        push(s, 0, c[s]);
        int r = 0, w = 1;
        while (r < w) {
            int i = Q[r++];
            for (int j : D[d]) {
                int ii = i + j;
                if (ii >= 0 && ii < S && B[ii] == 0 && dist(c[s], c[ii]) < max) {
                    push(ii, w++, c[s]);
                }
            }
        }
        return r;
    }

    private int bfs(int[] c, int s, int d, int v, int level) {
        push(s, 0, v);
        int r = 0, w = 1;
        while (r < w) {
            int i = Q[r++];
            int cur = c[i];
            for (int j : D[d]) {
                int ii = i + j;
                if (ii >= 0 && ii < S && B[ii] < level && c[ii] == cur) {
                    push(ii, w++, v);
                }
            }
        }
        return r;
    }

    private void push(int p, int w, int v) {
        minI = min(minI, I[p]);
        maxI = max(maxI, I[p]);
        minJ = min(minJ, J[p]);
        maxJ = max(maxJ, J[p]);
        B[p]++;
        T[p] = v;
        Q[w] = p;
    }

    //-----------------------------------------------------------------------------------
    /*
    Various
     */

    private int[] CR, CG, CB;

    public void initRGBArrays() {
        if (CR != null && CR.length >= S) {
            return;
        }
        CR = new int[S];
        CG = new int[S];
        CB = new int[S];
    }

    public int count(int[] c, int p, int d) {
        int count = 0;
        for (int i : D[d]) {
            int q = p + i;
            if (q >= 0 && q < S && c[q] > 0) {
                count++;
            }
        }
        return count;
    }

    public void getContour(int[] c, int[] f) {
        for (int p = W; p < S - W; p++) {
            int cc = c[p];

            int d1 = dist(cc, c[p - 1]);
            int d2 = dist(cc, c[p + 1]);
            int d3 = dist(cc, c[p + W]);
            int d4 = dist(cc, c[p - W]);

            f[p] = max(max(d1, d2), max(d3, d4));
        }
    }

    public void filter(int[] c, int[] f, int w, int h, int n, int v) {
        calculateRowColumnSums(f);
        System.arraycopy(ZERO, 0, c, 0, S);
        for (int p = 0; p < S; p++) {
            if (f[p] > v) {
                if (J[p] > w && I[p] > h) {
                    int t = getSum(0, p - h * W - w, p + h * W + w);
                    if (t > n) {
                        c[p] = t;
                    }
                }
            }
        }
    }

    public void drawRects(int[] c, List<Rect> list, int color) {
        for (Rect r : list) {
            for (int i = r.j1; i < r.j2; i++) {
                c[W * r.i1 + i] = color;
                c[W * r.i2 + i - W] = color;
            }
            for (int i = r.i1; i < r.i2; i++) {
                c[W * i + r.j1] = color;
                c[W * i + r.j2 - 1] = color;
            }
        }
    }

    public void saveRGB(int[] c) {
        for (int i = 0; i < S; i++) {
            CR[i] = c[i] >> 16;
            CG[i] = (c[i] >> 8) & 255;
            CB[i] = c[i] & 255;
        }
    }

}
