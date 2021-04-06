package com.simplejcode.commons.pdf;

import com.simplejcode.commons.pdf.layout.*;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.io.IOException;

public class PDFFont implements IFont {

    /**
     * Represents the PD font object
     */
    private final PDFont font;

    /**
     * Font size
     */
    private final float size;

    /**
     * Used to calculate font specific metrics
     */
    private final float coef;

    public PDFFont(PDFont font, float size) {
        this.font = font;
        this.size = size;
        coef = 1e-3f * size;
    }

    public PDFont getFont() {
        return font;
    }

    public float getSize() {
        return size;
    }

    public float getHeight() throws DrawingException {
        try {
            return coef * font.getBoundingBox().getHeight();
        } catch (IOException e) {
            throw new DrawingException(e);
        }
    }

    public float getAscent() throws DrawingException {
        return coef * font.getFontDescriptor().getAscent();
    }

    public float getDescent() throws DrawingException {
        return coef * font.getFontDescriptor().getDescent();
    }

    public float getLeading() throws DrawingException {
        return coef * font.getFontDescriptor().getLeading();
    }

    public float getStringWidth(String text) throws DrawingException {
        try {
            return coef * font.getStringWidth(text);
        } catch (IOException e) {
            throw new DrawingException(e);
        }
    }

    public float getSpaceWidth() throws DrawingException {
        return coef * font.getSpaceWidth();
    }

}
