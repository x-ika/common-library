package com.simplejcode.commons.pdf.layout;

public class VPlaceHolder extends View {

    private final float w;
    private final float h;

    public VPlaceHolder(float w, float h) {
        this.w = w;
        this.h = h;
    }

    protected float getActualWidth() throws DrawingException {
        return w;
    }

    protected float getActualHeight() throws DrawingException {
        return h;
    }

    public void draw(IGraphics graphics) throws DrawingException {
        drawBackground(graphics);
        drawBorder(graphics);
    }

}
