package com.simplejcode.commons.pdf.layout;

public class Margins {

    protected float fromY2, fromX2, fromY1, fromX1;

    public Margins(float fromY2, float fromX2, float fromY1, float fromX1) {
        this.fromY2 = fromY2;
        this.fromX2 = fromX2;
        this.fromY1 = fromY1;
        this.fromX1 = fromX1;
    }

    public Margins(float gap) {
        this(gap, gap, gap, gap);
    }

    public Margins() {
        this(0);
    }

    public float getHorizontal() {
        return fromX1 + fromX2;
    }

    public float getVertical() {
        return fromY2 + fromY1;
    }

    public float getFromX1() {
        return fromX1;
    }

    public float getFromX2() {
        return fromX2;
    }

    public float getFromY2() {
        return fromY2;
    }

    public float getFromY1() {
        return fromY1;
    }

}
