package com.simplejcode.commons.algo.struct;

public class SegmentList {

    private double x, y;

    private SegmentList next;


    public SegmentList(double x, double y, SegmentList next) {
        init(x, y, next);
    }


    public void init(double i, double j, SegmentList l) {
        x = i;
        y = j;
        next = l;
    }

    public boolean contains(double v) {
        return x <= v && v < y || next != null && next.contains(v);
    }

    public void cut(double i, double j) {
        double a = x, b = y;
        if (j < b) {
            if (a < i) {
                SegmentList list = new SegmentList(j, b, next);
                y = i;
                next = list;
            } else if (a < j) {
                x = j;
            }
        } else {
            SegmentList list = next;
            while (list != null && i <= list.x && list.y <= j) {
                list = list.next;
            }
            if (a < i) {
                if (i < b) {
                    y = i;
                }
                if ((next = list) != null) {
                    list.cut(i, j);
                }
            } else {
                if (list == null) {
                    init(0, 0, null);
                } else {
                    init(list.x, list.y, list.next);
                    cut(i, j);
                }
            }
        }
    }

    public double length() {
        return y - x + (next == null ? 0 : next.length());
    }

    public String toString() {
        return "[" + x + ", " + y + "] " + next;
    }

}
