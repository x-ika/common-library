package com.simplejcode.commons.misc;

import java.util.*;

public final class GeometryUtils {

    private GeometryUtils() {
    }

    //-----------------------------------------------------------------------------------
    /*
    Points
     */

    public static double size(double[] box) {
        return Math.max(box[1] - box[0], box[3] - box[2]);
    }

    public static double[] getBoundingBox(double[] b1, double[] b2) {
        return new double[]{Math.min(b1[0], b2[0]), Math.max(b1[1], b2[1]), Math.min(b1[2], b2[2]), Math.max(b1[3], b2[3])};
    }

    public static double[] updateBoundingBox(int n, double[] x, double[] y, double[] box) {
        return updateBoundingBox(n, ArrayUtils.ORDER, x, y, box);
    }

    public static double[] updateBoundingBox(int n, int[] id, double[] x, double[] y, double[] box) {
        double minx = box[0], maxx = box[1];
        double miny = box[2], maxy = box[3];
        for (int i = 0; i < n; i++) {
            double tx = x[id[i]], ty = y[id[i]];
            minx = Math.min(minx, tx);
            maxx = Math.max(maxx, tx);
            miny = Math.min(miny, ty);
            maxy = Math.max(maxy, ty);
        }
        box[0] = minx;
        box[1] = maxx;
        box[2] = miny;
        box[3] = maxy;
        return box;
    }

    public static double[] ifrotate(double ox, double oy, int a, int n,
                                    double[] x, double[] y,
                                    double[] sin, double[] cos, double[] box)
    {
        return ifrotate(ox, oy, a, n, ArrayUtils.ORDER, x, y, sin, cos, box);
    }

    public static double[] ifrotate(double ox, double oy, int a, int n,
                                    int[] id, double[] x, double[] y,
                                    double[] sin, double[] cos, double[] box)
    {
        double minx = box[0], maxx = box[1];
        double miny = box[2], maxy = box[3];
        for (int i = 0; i < n; i++) {
            int j = id[i];
            double tx = x[j] - ox, ty = y[j] - oy;
            double rx = tx * cos[a] - ty * sin[a] + ox;
            double ry = tx * sin[a] + ty * cos[a] + oy;
            minx = Math.min(minx, rx);
            maxx = Math.max(maxx, rx);
            miny = Math.min(miny, ry);
            maxy = Math.max(maxy, ry);
        }
        box[0] = minx;
        box[1] = maxx;
        box[2] = miny;
        box[3] = maxy;
        return box;
    }

    public static void rotate(double ox, double oy, int a, int n,
                              double[] x, double[] y,
                              double[] sin, double[] cos)
    {
        rotate(ox, oy, a, n, ArrayUtils.ORDER, x, y, sin, cos);
    }

    public static void rotate(double ox, double oy, int a, int n,
                              int[] id, double[] x, double[] y,
                              double[] sin, double[] cos)
    {
        for (int i = 0; i < n; i++) {
            int j = id[i];
            double tx = x[j] - ox, ty = y[j] - oy;
            x[j] = tx * cos[a] - ty * sin[a] + ox;
            y[j] = tx * sin[a] + ty * cos[a] + oy;
        }
    }

    public static void move(double dx, double dy, int n, double[] x, double[] y) {
        move(dx, dy, n, ArrayUtils.ORDER, x, y);
    }

    public static void move(double dx, double dy, int n, int[] id, double[] x, double[] y) {
        for (int i = 0; i < n; i++) {
            int j = id[i];
            x[j] += dx;
            y[j] += dy;
        }
    }

    public static void scale(double k, int n, double[] x, double[] y) {
        scale(k, n, ArrayUtils.ORDER, x, y);
    }

    public static void scale(double k, int n, int[] id, double[] x, double[] y) {
        for (int i = 0; i < n; i++) {
            int j = id[i];
            x[j] *= k;
            y[j] *= k;
        }
    }

    //-----------------------------------------------------------------------------------
    /*
    Line/Segment Intersection
     */

    public static double A, B, C;
    public static double ox, oy;

    public void buildLine(double x, double y, double a) {
        A = Math.sin(a);
        B = -Math.cos(a);
        C = -A * x - B * y;
    }

    public static boolean intersects(long ax1, long ay1, long ax2, long ay2, long bx1, long by1, long bx2, long by2) {

        long anx = ay1 - ay2;
        long any = ax2 - ax1;
        long bnx = by1 - by2;
        long bny = bx2 - bx1;

        long det = anx * bny - any * bnx;

        if (det == 0) {
            return false;
        }

        long b1 = ax1 * anx + ay1 * any;
        long b2 = bx1 * bnx + by1 * bny;

        ox = (double) (b1 * bny - b2 * any) / det;
        oy = (double) (b2 * anx - b1 * bnx) / det;

        return true;

    }

    public static boolean intersects(double a, double b, double c, double x1, double y1, double x2, double y2) {
        if (put(a, b, c, x1, y1) * put(a, b, c, x2, y2) > 0) {
            return false;
        }
        double dx = x2 - x1;
        double dy = y2 - y1;
        double p = a * x1 + b * y1 + c;
        double q = a * dx + b * dy;
        double k = -p / q;
        ox = x1 + k * dx;
        oy = y1 + k * dy;
        return true;
    }

    public static boolean intersects(double ax1, double ay1, double ax2, double ay2, double bx1, double by1, double bx2, double by2) {

        double a1 = ay1 - ay2, b1 = ax2 - ax1, c1 = ax1 * ay2 - ax2 * ay1;
        double a2 = by1 - by2, b2 = bx2 - bx1, c2 = bx1 * by2 - bx2 * by1;

        if (put(a1, b1, c1, bx1, by1) * put(a1, b1, c1, bx2, by2) > 0) {
            return false;
        }
        if (put(a2, b2, c2, ax1, ay1) * put(a2, b2, c2, ax2, ay2) > 0) {
            return false;
        }

        double dx = ax2 - ax1;
        double dy = ay2 - ay1;
        double p = a2 * ax1 + b2 * ay1 + c2;
        double q = a2 * dx + b2 * dy;
        double k = -p / q;
        ox = ax1 + k * dx;
        oy = ay1 + k * dy;
        return true;
    }

    public static long sideOf(long x1, long y1, long x2, long y2, long px, long py) {
        return Long.signum(put(x1, y1, x2, y2, px, py));
    }

    public static double put(double a, double b, double c, double x, double y) {
        return a * x + b * y + c;
    }

    public static long put(long x1, long y1, long x2, long y2, long px, long py) {
        return px * (y1 - y2) - py * (x1 - x2) + x1 * y2 - y1 * x2;
    }

    public static long scalar(long x1, long y1, long x2, long y2) {
        return x1 * x2 + y1 * y2;
    }

    public static long vector(long x1, long y1, long x2, long y2) {
        return x1 * y2 - y1 * x2;
    }

    //-----------------------------------------------------------------------------------
    /*
    Figures/Area/Hull
     */

    public static double area(double x1, double y1, double x2, double y2, double x3, double y3) {
        return Math.abs((x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1)) / 2d;
    }

    public static double area(int x1, int y1, int x2, int y2, int x3, int y3) {
        return Math.abs((x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1)) / 2d;
    }

    public static double area(double a, double b, double c) {
        double p = (a + b + c) / 2;
        return Math.sqrt(p * (p - a) * (p - b) * (p - c));
    }

    public static double area(int[] x, int[] y) {
        double s = 0;
        for (int i = 1; i < x.length - 1; i++) {
            s += area(x[0], y[0], x[i], y[i], x[i + 1], y[i + 1]);
        }
        return s;
    }

    public static int[] H;

    public static int getConvexHull(int n, double[] x, double[] y) {

        if (H == null || H.length < n) {
            H = new int[n];
        }

        double lx = 1e9, ly = 0;
        for (int i = 0; i < n; i++) {
            if (lx > x[i] || lx == x[i] && ly > y[i]) {
                lx = x[i];
                ly = y[i];
            }
        }
        double[] c = new double[n];
        for (int i = 0; i < n; i++) {
            double dx = x[i] - lx, dy = y[i] - ly;
            double d2 = dx * dx + dy * dy;
            c[i] = d2 < 1e-12 ? -1e9 : Math.atan2(dy, dx);
        }
        ArrayUtils.quicksort(0, n, c, x, y);

        int[] h = H;
        int sz = 0;
        for (int i = 0; i < n; i++) {
            h[sz++] = i;
            while (sz > 2) {
                int p = h[sz - 3], q = h[sz - 2], r = h[sz - 1];
                double v1x = x[q] - x[p], v1y = y[q] - y[p];
                double v2x = x[r] - x[q], v2y = y[r] - y[q];
                if (v1x * v2y - v1y * v2x > 0) {
                    break;
                }
                h[--sz - 1] = r;
            }
        }
        ArrayUtils.restore(0, n, x, y);
        ArrayUtils.transformBy(0, sz, ArrayUtils.P, h);
        return sz;

    }

    public static boolean isConvex(long[] x, long[] y) {
        int last = x.length - 1;
        if (last < 2) {
            return true;
        }
        long ax = x[0] - x[last];
        long ay = y[0] - y[last];
        long bx = x[1] - x[0];
        long by = y[1] - y[0];
        long sign = Long.signum(vector(bx, by, ax, ay));
        for (int i = 1; i < last; i++) {
            ax = bx;
            ay = by;
            bx = x[i + 1] - x[i];
            by = y[i + 1] - y[i];
            if (sign != Long.signum(vector(bx, by, ax, ay))) {
                return false;
            }
        }
        return true;
    }

    public static boolean contains(long[] x, long[] y, long px, long py) {

        int n = x.length;

        long inf = (long) 1e18;
        long minx = inf, maxx = -inf;
        long miny = inf, maxy = -inf;
        for (int i = 0; i < n; i++) {
            long tx = x[i], ty = y[i];
            minx = Math.min(minx, tx);
            maxx = Math.max(maxx, tx);
            miny = Math.min(miny, ty);
            maxy = Math.max(maxy, ty);
        }
        if (px < minx || maxx < px || py < miny || maxy < py) {
            return false;
        }
        int hits = 0;
        long lastx = x[n - 1], lasty = y[n - 1];
        long curx, cury, dx, dy, ddx, ddy;

        for (int i = 0; i < n; lastx = curx, lasty = cury, i++) {
            curx = x[i];
            cury = y[i];
            dx = lastx - curx;
            dy = lasty - cury;
            if (dy == 0) {
                continue;
            }
            if ((minx = Math.min(lastx, curx)) < px) {
                continue;
            }

            if (dy > 0) {
                miny = Math.min(cury, lasty);
                maxy = Math.max(cury, lasty);
                ddx = px - curx;
                ddy = py - cury;
            } else {
                miny = Math.min(cury, lasty);
                maxy = Math.max(cury, lasty);
                ddx = px - lastx;
                ddy = py - lasty;
            }
            if (miny < py && py <= maxy && (px < minx || vector(dx, dy, ddx, ddy) > 0)) {
                hits++;
            }
        }

        return (hits & 1) == 1;
    }

    public static double area(long[] x, long[] y) {
        double s = 0;
        int n = x.length - 1;
        for (int i = 1; i < n; i++) {
            s += area(x[0], y[0], x[i], y[i], x[i + 1], y[i + 1]);
        }
        return s;
    }

    public static long area(long[] x1, long[] y1, long[] x2, long[] y2) {
        int n = x1.length;

        Set<Long> xSet = new TreeSet<>();
        Set<Long> ySet = new TreeSet<>();
        for (int i = 0; i < n; i++) {
            xSet.add(x1[i]);
            xSet.add(x2[i]);
            ySet.add(y1[i]);
            ySet.add(y2[i]);
        }
        Long[] xs = new Long[xSet.size()];
        Long[] ys = new Long[ySet.size()];
        xSet.toArray(xs);
        ySet.toArray(ys);
        long area = 0;
        for (int i = 1; i < xs.length; i++) {
            for (int j = 1; j < ys.length; j++) {
                for (int k = 0; k < n; k++) {
                    long cx1 = x1[k];
                    long cy1 = y1[k];
                    long cx2 = x2[k];
                    long cy2 = y2[k];

                    long rx1 = xs[i - 1];
                    long ry1 = ys[j - 1];
                    long rx2 = xs[i];
                    long ry2 = ys[j];

                    if (cx1 <= rx1 && rx2 <= cx2 && cy1 <= ry1 && ry2 <= cy2) {
                        area += (rx2 - rx1) * (ry2 - ry1);
                        break;
                    }
                }
            }
        }
        return area;
    }

}
