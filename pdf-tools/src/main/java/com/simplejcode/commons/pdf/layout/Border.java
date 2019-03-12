package com.simplejcode.commons.pdf.layout;

/**
 * @author I. Merabishvili | HighPots
 */
public abstract class Border extends View {

    protected View view;

    public Border(View view) {
        this.view = view;
    }

    protected float getActualWidth() throws DrawingException {
        return view.getActualWidth();
    }

    protected float getActualHeight() throws DrawingException {
        return view.getActualHeight();
    }

}
