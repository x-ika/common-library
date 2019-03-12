package com.simplejcode.commons.pdf.layout;

import java.awt.*;

/**
 * @author I. Merabishvili | HighPots
 * <p>
 * Basic element which can be drawn.
 * <p>
 * {@link View} consists from view itself, insets and border
 * border is separate view and may be drawn outside of view bounds
 * <p>
 * {@link #getBounds()} returns rectangle given for both insets and view itself
 * {@link #getDrawableBounds()} returns rectangle for only view
 * <p>
 * {@link #getActualWidth()} and {@link #getActualHeight()} are sizes requested by view
 * {@link #getMinimumWidth()} and {@link #getMinimumHeight()} define the minimum rectangle need to be given
 * <p>
 * Alignment logic missing!
 * <p>
 * {@link View} encapsulates drawing process
 * Method {@link #draw(IGraphics)} is abstract but usually drawing consists from 3 phases
 * 1. {@link #drawBackground(IGraphics)}
 * 2. draw the view itself
 * 3. {@link #drawBorder(IGraphics)}
 */
public abstract class View {

    protected AlignmentV vertical;
    protected AlignmentH horizontal;
    protected Margins margins;
    protected com.simplejcode.commons.pdf.layout.Rectangle bounds;

    protected Color background;
    protected Border border;

    public View(AlignmentV vertical, AlignmentH horizontal, Margins margins) {
        this.vertical = vertical;
        this.horizontal = horizontal;
        this.margins = margins;
    }

    public View() {
        this(AlignmentV.TOP, AlignmentH.LEFT, new Margins());
    }

    public View(Alignments alignments) {
        this(alignments.v, alignments.h, new Margins(0));
    }

    public View(Alignments alignments, Margins margins) {
        this(alignments.v, alignments.h, margins);
    }

    public AlignmentV getVertical() {
        return vertical;
    }

    public void setVertical(AlignmentV vertical) {
        this.vertical = vertical;
    }

    public AlignmentH getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(AlignmentH horizontal) {
        this.horizontal = horizontal;
    }

    public Margins getMargins() {
        return margins;
    }

    public void setMargins(Margins margins) {
        this.margins = margins;
    }

    public com.simplejcode.commons.pdf.layout.Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(com.simplejcode.commons.pdf.layout.Rectangle bounds) {
        this.bounds = bounds;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public Border getBorder() {
        return border;
    }

    public void setBorder(Border border) {
        this.border = border;
    }

    //-----------------------------------------------------------------------------------

    protected abstract float getActualWidth() throws DrawingException;

    protected abstract float getActualHeight() throws DrawingException;

    public abstract void draw(IGraphics graphics) throws DrawingException;

    protected void drawBackground(IGraphics graphics) throws DrawingException {
        if (background != null) {
            graphics.fillRect(background, getX1(), getY1(), getWidth(), getHeight());
        }
    }

    protected void drawBorder(IGraphics graphics) throws DrawingException {
        if (border != null) {
            border.draw(graphics);
        }
    }

    //-----------------------------------------------------------------------------------

    public float getMinimumWidth() throws DrawingException {
        return getActualWidth() + margins.getHorizontal();
    }

    public float getMinimumHeight() throws DrawingException {
        return getActualHeight() + margins.getVertical();
    }

    public com.simplejcode.commons.pdf.layout.Rectangle getDrawableBounds() {
        return new com.simplejcode.commons.pdf.layout.Rectangle(getX1() + getFromX1(), getY1() + getFromY1(), getX2() - getFromX2(), getY2() - getFromY2());
    }

    public float getFromX1() {
        return margins.getFromX1();
    }

    public float getFromX2() {
        return margins.getFromX2();
    }

    public float getFromY2() {
        return margins.getFromY2();
    }

    public float getFromY1() {
        return margins.getFromY1();
    }

    public float getX1() {
        return bounds.getX1();
    }

    public float getY1() {
        return bounds.getY1();
    }

    public float getX2() {
        return bounds.getX2();
    }

    public float getY2() {
        return bounds.getY2();
    }

    public float getWidth() {
        return bounds.getWidth();
    }

    public float getHeight() {
        return bounds.getHeight();
    }

    //-----------------------------------------------------------------------------------

    public float getX(float width) {
        return getX(bounds.getX1() + margins.getFromX1(), bounds.getX2() - margins.getFromX2(), width, horizontal);
    }

    public float getY(float height) {
        return getY(bounds.getY1() + margins.getFromY1(), bounds.getY2() - margins.getFromY2(), height, vertical);
    }

    public static float getX(float x1, float x2, float w, AlignmentH alignment) {
        switch (alignment) {
            case LEFT:
                return x1;
            case CENTER:
                return (x2 + x1 - w) / 2;
            case RIGHT:
                return x2 - w;
            default:
                throw new RuntimeException("Not possible");
        }
    }

    public static float getY(float y1, float y2, float h, AlignmentV alignment) {
        switch (alignment) {
            case TOP:
                return y1;
            case CENTER:
                return (y2 + y1 - h) / 2;
            case BOTTOM:
                return y2 - h;
            default:
                throw new RuntimeException("Not possible");
        }
    }

}
