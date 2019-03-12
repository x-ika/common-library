package com.simplejcode.commons.pdf.layout;

/**
 * @author I. Merabishvili | HighPots
 */
public class ContainerVertical extends Container {

    private float gap;

    public ContainerVertical() {
        this(0);
    }

    public ContainerVertical(float gap) {
        this.gap = gap;
    }

    protected float getActualWidth() throws DrawingException {
        float w = 0;
        for (View child : views) {
            w = Math.max(w, child.getMinimumWidth());
        }
        return w;
    }

    protected float getActualHeight() throws DrawingException {
        float h = 0;
        for (View child : views) {
            h += child.getMinimumHeight() + gap;
        }
        return h - (views.isEmpty() ? 0 : gap);
    }

    protected void layout() throws DrawingException {

        for (View view : views) {
            view.setBounds(new Rectangle(0, 0, getWidth(), 0));
        }

        Rectangle rect = getDrawableBounds();

        float y = rect.getY1();

        for (View view : views) {
            float height = view.getMinimumHeight();
            view.setBounds(new Rectangle(rect.getX1(), y, rect.getX2(), y + height));
            y += height + gap;
        }
        y -= views.isEmpty() ? 0 : gap;

        getBounds().setY2(y + getFromY2());

    }

    public Container shallowCopy() {
        Container copy = super.shallowCopy();
        ((ContainerVertical) copy).gap = gap;
        return copy;
    }

}
