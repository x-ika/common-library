package com.simplejcode.commons.algo.d3;

import com.simplejcode.commons.algo.MatrixUtils;

public class Plane {

    private double a, b, c, d;

    public Plane(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public Plane(double x, double y, double z, double ax, double ay) {
        // z0 = z + (x0 - x) * tan(ax) + (y0 - y) * tan(ay)
        a = Math.tan(ax);
        b = Math.tan(ay);
        c = -1;
        d = z - a * x - b * y;
    }

    public Plane(Point3D a,  Point3D b, Point3D c) {
        // https://en.wikipedia.org/wiki/Plane_(geometry)#Method_2

        d = -MatrixUtils.det9(
                a.x, a.y, a.z,
                b.x, b.y, b.z,
                c.x, c.y, c.z);

        this.a = MatrixUtils.det9(
                1, a.y, a.z,
                1, b.y, b.z,
                1, c.y, c.z);

        this.b = MatrixUtils.det9(
                a.x, 1, a.z,
                b.x, 1, b.z,
                c.x, 1, c.z);

        this.c = MatrixUtils.det9(
                a.x, a.y, 1,
                b.x, b.y, 1,
                c.x, c.y, 1);
    }

    public double signum(Point3D p) {
        return a * p.x + b * p.y + c * p.z + d;
    }

}
