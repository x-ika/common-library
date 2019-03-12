package com.simplejcode.commons.pdf.layout;

/**
 * @author I. Merabishvili | HighPots
 */
public interface IFont {

    float getSize();

    float getHeight() throws DrawingException;

    float getAscent() throws DrawingException;

    float getDescent() throws DrawingException;

    float getLeading() throws DrawingException;

    float getStringWidth(String text) throws DrawingException;

    default float getSpaceWidth() throws DrawingException {
        return getStringWidth(" ");
    }

}
