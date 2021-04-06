package com.simplejcode.commons.pdf.layout;

import java.awt.image.BufferedImage;

public class VImage extends View {

    private final BufferedImage image;
    private final float scale;

    public VImage(Alignments alignments, BufferedImage image, float scale) {
        super(alignments);
        this.image = image;
        this.scale = scale;
    }

    protected float getActualWidth() {
        return scale * image.getWidth();
    }

    protected float getActualHeight() {
        return scale * image.getHeight();
    }

    public void draw(IGraphics graphics) throws DrawingException {

        float w = getActualWidth();
        float h = getActualHeight();

        graphics.drawImage(image, getX(w), getY(h), w, h);

    }

}
