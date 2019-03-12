package com.simplejcode.commons.pdf.layout;

import java.util.*;

/**
 * @author I. Merabishvili | HighPots
 * <p>
 * Basic PDF element containing another elements
 * <p>
 * This class encapsulates drawing of the children as well as the layout logic
 * <p>
 * Drawing goes according to the contract defined by {@link View}
 * 1. {@link #drawBackground(IGraphics)}
 * 2. drawing children
 * 2.1 {@link #layout()}
 * 2.2 Call draw for each child
 * 3. {@link #drawBorder(IGraphics)}
 * <p>
 * {@link #layout()} is responsible for calling {@link #setBounds(Rectangle)} for children.
 * Consequently calling {@link #setBounds(Rectangle)} for {@link Container} object should layout it's children!
 */
public abstract class Container extends View {

    protected List<View> views;

    public Container() {
        views = new ArrayList<>();
    }

    public void setBounds(Rectangle bounds) {
        super.setBounds(bounds);
        try {
            layout();
        } catch (DrawingException e) {
            e.printStackTrace();
        }
    }

    public int getNumberOfViews() {
        return views.size();
    }

    public View get(int index) {
        return views.get(index);
    }

    public void add(View view) {
        views.add(view);
    }

    public void remove(View view) {
        views.remove(view);
    }

    public void remove(int index) {
        views.remove(index);
    }

    public void draw(IGraphics graphics) throws DrawingException {

        drawBackground(graphics);

        drawViews(graphics);

        drawBorder(graphics);

    }

    protected void drawViews(IGraphics graphics) throws DrawingException {
        layoutAndDrawViews(graphics);
    }

    protected void layoutAndDrawViews(IGraphics graphics) throws DrawingException {
        layout();

        for (View view : views) {
            view.draw(graphics);
        }
    }

    //-----------------------------------------------------------------------------------

    protected abstract void layout() throws DrawingException;

    //-----------------------------------------------------------------------------------

    public Container shallowCopy() {
        try {
            Container copy = getClass().getConstructor().newInstance();
            copy.setHorizontal(horizontal);
            copy.setVertical(vertical);
            copy.setMargins(margins);
            copy.setBackground(background);
            copy.setBorder(border);
            return copy;
        } catch (Exception e) {
            throw new RuntimeException("Unable to clone " + getClass(), e);
        }
    }

}
