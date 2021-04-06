package com.simplejcode.commons.pdf.layout;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author I. Merabishvili
 * <p>
 * This class is responsible for the actual drawing
 */
public interface IGraphics {

    void drawText(Color color, IFont font,
                  float x, float y,
                  String text) throws DrawingException;

    void drawLine(Color color, float line,
                  float x1, float y1,
                  float x2, float y2) throws DrawingException;

    void drawRect(float x, float y,
                  float w, float h,
                  float line, Color color) throws DrawingException;

    void fillRect(Color color,
                  float x, float y,
                  float w, float h) throws DrawingException;

    void drawImage(BufferedImage image,
                   float x, float y,
                   float w, float h) throws DrawingException;

}
