package com.simplejcode.commons.pdf.layout;

/**
 * @author I. Merabishvili | HighPots
 */
public class VPlaceHolder extends View {

    private float w, h;

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
