package com.simplejcode.commons.algo.d3;

import java.util.*;

public class Polyhedron {

    private static final double EPS = 1e-9;

    private int n;

    private Point3D[] ps;

    private Point3D[][] surfaces;

    public Polyhedron(Point3D[] ps) {
        this.ps = ps;
        n = ps.length;
        check();
    }

    public Point3D getPoint(int i) {
        return ps[i];
    }

    private void check() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                double d = ps[i].dist(ps[j]);
                if (d < EPS) {
                    throw new RuntimeException(String.format("Points %d and %d are too closer", i, j));
                }
                for (int k = 0; k < n; k++) {
                    if (k != i && k != j && Math.abs(ps[i].dist(ps[k]) + ps[j].dist(ps[k]) - d) < EPS) {
                        throw new RuntimeException(String.format("Points %d, %d and %d are colinear", i, j, k));
                    }
                }
            }
        }
    }

    private void buildSurface() {
        if (surfaces != null) {
            return;
        }
        surfaces = new Point3D[n][];
        int ns = 0;
        Point3D[] tmp = new Point3D[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                M:
                for (int k = 0; k < j; k++) {
                    Plane plane = new Plane(ps[i], ps[j], ps[k]);
                    int dir = 0, z = 0;
                    for (int t = 0; t < n; t++) {
                        double s = plane.signum(ps[t]);
                        if (Math.abs(s) < EPS) {
                            tmp[z++] = ps[t];
                            if (z == 1 && t != k || z == 2 && t != j || z == 3 && t != i) {
                                continue M;
                            }
                        } else {
                            if (dir * s < 0) {
                                continue M;
                            }
                            dir = s > 0 ? 1 : -1;
                        }
                    }

                    for (int a = 1; a < z; a++) {
                        N:
                        for (int b = a; b < z; b++) {
                            Point3D u = tmp[b].subtract(tmp[a - 1]);
                            for (int c = a; c < z; c++) {
                                Point3D v = tmp[c].subtract(tmp[a - 1]);
                                if (dir * plane.signum(tmp[a].add(u.vector(v))) > 0) {
                                    continue N;
                                }
                            }
                            Point3D t = tmp[a];
                            tmp[a] = tmp[b];
                            tmp[b] = t;
                            break;
                        }
                    }

                    surfaces[ns++] = Arrays.copyOf(tmp, z);

                }
            }
        }

        surfaces = Arrays.copyOf(surfaces, ns);
    }

    public Polyhedron[] divide(Plane plane) {

        buildSurface();

        Collection<Point3D> c1 = new ArrayList<>();
        Collection<Point3D> c2 = new ArrayList<>();

        for (Point3D p : ps) {
            double s = plane.signum(p);
            if (s < EPS) {
                c1.add(p);
            }
            if (s > -EPS) {
                c2.add(p);
            }
        }

        for (Point3D[] surface : surfaces) {
            for (int i = 0; i < surface.length; i++) {
                Point3D a = surface[i], b = surface[(i + 1) % surface.length];
                if (plane.signum(a) * plane.signum(b) < 0) {
                    Point3D left = a, right = b;
                    while (left.dist(right) > EPS) {
                        Point3D middle = right.add(left).multiply(0.5);
                        double s = plane.signum(middle);
                        if (s == 0) {
                            left = middle;
                            break;
                        }
                        if (s * plane.signum(left) > 0) {
                            left = middle;
                        } else {
                            right = middle;
                        }
                    }

                    add(c1, left);
                    add(c2, left);
                }
            }
        }

        return new Polyhedron[]{
                new Polyhedron(c1.toArray(new Point3D[0])),
                new Polyhedron(c2.toArray(new Point3D[0]))
        };
    }

    private void add(Collection<Point3D> c, Point3D p) {
        for (Point3D t : c) {
            if (p.dist(t) < EPS) {
                return;
            }
        }
        c.add(p);
    }

    public double getVolume() {

        // http://wwwf.imperial.ac.uk/~rn/centroid.pdf

        buildSurface();

        double volume = 0;

        for (Point3D[] surface : surfaces) {
            for (int i = 2; i < surface.length; i++) {
                Point3D a = surface[0];
                Point3D db = surface[i - 1].subtract(a);
                Point3D dc = surface[i].subtract(a);

                volume += a.scalar(db.vector(dc));
            }
        }

        return volume / 6;
    }

}
