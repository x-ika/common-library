package com.simplejcode.commons.pdf.layout;

import java.awt.*;

public class BorderRect extends Border {

    private final Color color;
    private final float width;

    public BorderRect(View view, Color color, float width) {
        super(view);
        this.color = color;
        this.width = width;
    }

    public void draw(IGraphics graphics) throws DrawingException {
        graphics.drawRect(view.getX1(), view.getY1(), view.getWidth(), view.getHeight(), width, color);
    }

}
