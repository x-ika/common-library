package com.simplejcode.commons.algo;

import static java.lang.Math.*;

public class ImageRect {

    public int i1, j1, i2, j2;

    public ImageRect(int i1, int j1, int i2, int j2) {
        init(i1, j1, i2, j2);
    }

    public void init(ImageRect rect) {
        init(rect.i1, rect.j1, rect.i2, rect.j2);
    }

    public void init(int i1, int j1, int i2, int j2) {
        this.i1 = i1;
        this.j1 = j1;
        this.i2 = i2;
        this.j2 = j2;
    }

    public int h() {
        return i2 - i1;
    }

    public int w() {
        return j2 - j1;
    }

    public double getHWRatio() {
        return 1d * h() / w();
    }

    public int area() {
        return h() * w();
    }


    public int vsec(int ri1, int ri2) {
        return max(min(i2, ri2) - max(i1, ri1), 0);
    }

    public int hsec(int rj1, int rj2) {
        return max(min(j2, rj2) - max(j1, rj1), 0);
    }

    public int vdist(int ri1, int ri2) {
        return i1 < ri2 && ri1 < i2 ? 0 : min(abs(i1 - ri2), abs(i2 - ri1));
    }

    public int hdist(int rj1, int rj2) {
        return j1 < rj2 && rj1 < j2 ? 0 : min(abs(j1 - rj2), abs(j2 - rj1));
    }

    public boolean cont(int i, int j) {
        return i1 <= i && i < i2 && j1 <= j && j < j2;
    }


    public int vsec(ImageRect r) {
        return vsec(r.i1, r.i2);
    }

    public int hsec(ImageRect r) {
        return hsec(r.j1, r.j2);
    }

    public int vdist(ImageRect r) {
        return vdist(r.i1, r.i2);
    }

    public int hdist(ImageRect r) {
        return hdist(r.j1, r.j2);
    }

    public int dist(ImageRect r) {
        return vdist(r) + hdist(r);
    }

    public int sarea(ImageRect r) {
        return vsec(r) * hsec(r);
    }

    public boolean cont(ImageRect r) {
        return cont(r.i1, r.j1) && cont(r.i1, r.j2) && cont(r.i2, r.j1) && cont(r.i2, r.j2);
    }

    public boolean secs(ImageRect r) {
        return j1 < r.j2 && r.j1 < j2 && i1 < r.i2 && r.i1 < i2;
    }

    public void union(ImageRect r) {
        j1 = min(j1, r.j1);
        i1 = min(i1, r.i1);
        j2 = max(j2, r.j2);
        i2 = max(i2, r.i2);
    }

    public void union(ImageRect r, ImageRect out) {
        out.j1 = min(j1, r.j1);
        out.i1 = min(i1, r.i1);
        out.j2 = max(j2, r.j2);
        out.i2 = max(i2, r.i2);
    }


    public ImageRect copy() {
        return new ImageRect(i1, j1, i2, j2);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ImageRect)) {
            return false;
        }
        ImageRect r = (ImageRect) obj;
        return i1 == r.i1 && j1 == r.j1 && i2 == r.i2 && j2 == r.j2;
    }

}
