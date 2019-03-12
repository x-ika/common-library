package com.simplejcode.commons.pdf.layout;

/**
 * @author I. Merabishvili | HighPots
 */
public class Rectangle {

    protected float x1, y1, x2, y2;

    public Rectangle(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public float getX1() {
        return x1;
    }

    public void setX1(float x1) {
        this.x1 = x1;
    }

    public float getY1() {
        return y1;
    }

    public void setY1(float y1) {
        this.y1 = y1;
    }

    public float getX2() {
        return x2;
    }

    public void setX2(float x2) {
        this.x2 = x2;
    }

    public float getY2() {
        return y2;
    }

    public void setY2(float y2) {
        this.y2 = y2;
    }

    public float getWidth() {
        return x2 - x1;
    }

    public float getHeight() {
        return y2 - y1;
    }

    //-----------------------------------------------------------------------------------

    public Rectangle copy() {
        return new Rectangle(x1, y1, x2, y2);
    }

    public Rectangle extract(float x1, float x2, float y1, float y2, float w, float h) {
        if (def(x1) + def(x2) + def(w) == 2) {
            if (x1 == -1) x1 = getWidth() - x2 - w;
            if (x2 == -1) x2 = getWidth() - x1 - w;
        }
        x1 = x1 != -1 ? x1 : 0;
        x2 = x2 != -1 ? x2 : 0;

        if (def(y1) + def(y2) + def(h) == 2) {
            if (y1 == -1) y1 = getHeight() - y2 - h;
            if (y2 == -1) y2 = getHeight() - y1 - h;
        }
        y1 = y1 != -1 ? y1 : 0;
        y2 = y2 != -1 ? y2 : 0;

        return new Rectangle(getX1() + x1, getY1() + y1, getX2() - x2, getY2() - y2);
    }

    private static int def(float f) {
        return f != -1 ? 1 : 0;
    }

    public String toString() {
        return "Rectangle{" + "x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2 + '}';
    }

}
