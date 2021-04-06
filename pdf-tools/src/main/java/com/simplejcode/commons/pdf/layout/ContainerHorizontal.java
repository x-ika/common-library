package com.simplejcode.commons.pdf.layout;

public class ContainerHorizontal extends Container {

    private final float gap;

    public ContainerHorizontal() {
        this(0);
    }

    public ContainerHorizontal(float gap) {
        this.gap = gap;
    }

    protected float getActualWidth() throws DrawingException {
        float w = 0;
        for (View child : views) {
            w += child.getMinimumWidth() + gap;
        }
        return w - (views.isEmpty() ? 0 : gap);
    }

    protected float getActualHeight() throws DrawingException {
        float h = 0;
        for (View child : views) {
            h = Math.max(h, child.getMinimumHeight());
        }
        return h;
    }

    protected void layout() throws DrawingException {

        for (View view : views) {
            view.setBounds(new Rectangle(0, 0, getWidth(), 0));
        }

        Rectangle rect = getDrawableBounds();

        float x = rect.getX1();

        for (View view : views) {
            float width = view.getMinimumWidth();
            view.setBounds(new Rectangle(x, rect.getY1(), x + width, rect.getY2()));
            x += width + gap;
        }

    }

}
