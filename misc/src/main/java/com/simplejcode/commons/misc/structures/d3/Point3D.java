package com.simplejcode.commons.misc.structures.d3;

public class Point3D {

    public final double x, y, z;

    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3D multiply(double k) {
        return new Point3D(k * x, k * y, k * z);
    }

    public Point3D add(Point3D p) {
        return new Point3D(x + p.x, y + p.y, z + p.z);
    }

    public Point3D subtract(Point3D p) {
        return new Point3D(x - p.x, y - p.y, z - p.z);
    }

    public double scalar(Point3D p) {
        return x * p.x + y * p.y + z * p.z;
    }

    public Point3D vector(Point3D p) {
        return new Point3D(y * p.z - z * p.y, z * p.x - x * p.z, x * p.y - y * p.x);
    }

    public double dist(Point3D p) {
        return subtract(p).norm();
    }

    public double norm() {
        return Math.sqrt(x * x + y * y + z * z);
    }

}
