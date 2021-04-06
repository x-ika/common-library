package com.simplejcode.commons.pdf;

import com.simplejcode.commons.pdf.layout.*;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public final class PDFGraphics implements IGraphics {

    private final BasicPDFDocument context;

    private final int pageIndex;

    public PDFGraphics(BasicPDFDocument context, int pageIndex) {
        this.context = context;
        this.pageIndex = pageIndex;
    }

    public void drawText(Color color, IFont font,
                         float x, float y,
                         String text) throws DrawingException
    {
        PDPageContentStream stream = context.getStream(pageIndex);
        y = translate(context, y);
        try {
            stream.setStrokingColor(color);
            stream.setNonStrokingColor(color);
            stream.beginText();
            stream.setFont(((PDFFont) font).getFont(), font.getSize());
            stream.newLineAtOffset(x, y);
            stream.showText(text);
            stream.endText();
        } catch (IOException e) {
            throw new DrawingException(e);
        }
    }

    public void drawLine(Color color, float line,
                         float x1, float y1,
                         float x2, float y2) throws DrawingException
    {
        PDPageContentStream stream = context.getStream(pageIndex);
        y1 = translate(context, y1);
        y2 = translate(context, y2);
        try {
            stream.setStrokingColor(color);
            stream.setLineWidth(line);
            stream.moveTo(x1, y1);
            stream.lineTo(x2, y2);
            stream.stroke();
        } catch (IOException e) {
            throw new DrawingException(e);
        }
    }

    public void drawRect(float x, float y,
                         float w, float h,
                         float line, Color color) throws DrawingException
    {
        PDPageContentStream stream = context.getStream(pageIndex);
        y = translate(context, y + h);
        try {
            stream.setLineWidth(line);
            stream.setStrokingColor(color);
            stream.addRect(x, y, w, h);
            stream.stroke();
        } catch (IOException e) {
            throw new DrawingException(e);
        }
    }

    public void fillRect(Color color,
                         float x, float y,
                         float w, float h) throws DrawingException
    {
        PDPageContentStream stream = context.getStream(pageIndex);
        y = translate(context, y + h);
        try {
            stream.setNonStrokingColor(color);
            stream.addRect(x, y, w, h);
            stream.fill();
        } catch (IOException e) {
            throw new DrawingException(e);
        }
    }

    public void drawImage(BufferedImage image,
                          float x, float y,
                          float w, float h) throws DrawingException
    {
        PDPageContentStream stream = context.getStream(pageIndex);
        y = translate(context, y + h);
        try {
            PDImageXObject pdImage = LosslessFactory.createFromImage(context.getDocument(), image);
            stream.drawImage(pdImage, x, y, w, h);
        } catch (IOException e) {
            throw new DrawingException(e);
        }
    }

    private float translate(BasicPDFDocument pdf, float y) {
        return pdf.getPageRectangle().getHeight() - y;
    }

}
