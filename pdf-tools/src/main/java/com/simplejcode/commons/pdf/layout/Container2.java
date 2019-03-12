package com.simplejcode.commons.pdf.layout;

/**
 * @author I. Merabishvili | HighPots
 */
public class Container2 extends Container {

    private float[] width;

    public Container2() {
    }

    public Container2(View left, View right, float... width) {
        add(left);
        add(right);
        this.width = width;
    }

    protected float getActualWidth() throws DrawingException {
        return views.get(0).getMinimumWidth() + views.get(1).getMinimumWidth();
    }

    protected float getActualHeight() throws DrawingException {
        return Math.max(views.get(0).getMinimumHeight(), views.get(1).getMinimumHeight());
    }

    protected void layout() throws DrawingException {

        Rectangle rect = getDrawableBounds();

        View left = views.get(0);
        View right = views.get(1);

        float leftWidth = width[0] * rect.getWidth();
        float rightWidth = width[1] * rect.getWidth();
        left.setBounds(rect.extract(0, -1, 0, 0, leftWidth, -1));
        right.setBounds(rect.extract(-1, 0, 0, 0, rightWidth, -1));

    }

}
