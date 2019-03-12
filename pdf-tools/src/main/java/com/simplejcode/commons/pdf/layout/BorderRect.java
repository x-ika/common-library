package com.simplejcode.commons.pdf.layout;

import java.awt.*;

/**
 * @author I. Merabishvili | HighPots
 */
public class BorderRect extends Border {

    private Color color;
    private float width;

    public BorderRect(View view, Color color, float width) {
        super(view);
        this.color = color;
        this.width = width;
    }

    public void draw(IGraphics graphics) throws DrawingException {
        graphics.drawRect(view.getX1(), view.getY1(), view.getWidth(), view.getHeight(), width, color);
    }

}
