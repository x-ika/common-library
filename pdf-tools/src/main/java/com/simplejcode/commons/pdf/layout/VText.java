package com.simplejcode.commons.pdf.layout;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * @author I. Merabishvili | HighPots
 * <p>
 * This class represents the text.
 * Split logic is applied to the text before drawn
 * If bounds are not indicated, then text is split according to new line characters, otherwise width parameters
 * is also considered during the splitting.
 */
public class VText extends View {

    private IFont font;
    private Color color;
    private String text;

    private List<String> lines;

    public VText(Alignments alignments, IFont font, Color color, String text) {
        super(alignments);
        this.font = font;
        this.color = color;
        this.text = text == null ? "" : text;
    }

    public VText(IFont font, Color color, String text) {
        this(Alignments.TOP_LEFT, font, color, text);
    }

    protected float getActualWidth() throws DrawingException {
        splitText();
        float w = 0;
        for (String line : lines) {
            w = Math.max(w, font.getStringWidth(line));
        }
        return w;
    }

    protected float getActualHeight() throws DrawingException {
        splitText();
        return lines.size() * font.getHeight();
    }

    public void draw(IGraphics graphics) throws DrawingException {

        drawBackground(graphics);

        float h = getActualHeight();

        float startY = getY(h);
        float lineHeight = font.getHeight();

        for (int j = 0; j < lines.size(); j++) {
            String line = lines.get(j);

            float x = getX(font.getStringWidth(line));
            float y = startY + (j + 0.75f) * lineHeight;
            graphics.drawText(color, font, x, y, line);
        }

        drawBorder(graphics);
    }

    private void splitText() throws DrawingException {
        if (lines == null) {
            lines = new ArrayList<>();
            for (String line : text.split("\n")) {
                if (bounds == null) {
                    lines.add(line);
                } else {
                    lines.addAll(splitText(line, bounds.getWidth() - margins.getHorizontal(), font));
                }
            }
        }
    }

    private static List<String> splitText(String text, float width, IFont font) throws DrawingException {
        float space = font.getSpaceWidth();
        float cur = 0;
        List<String> lines = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0, p = -1; i <= text.length(); i++) {
            char c = i < text.length() ? text.charAt(i) : ' ';
            if (!Character.isWhitespace(c)) {
                continue;
            }
            String word = text.substring(p + 1, i);
            p = i;

            boolean isLast = i == text.length();

            float wordWidth = font.getStringWidth(word);
            float add = isLast ? 0 : space;
            if (sb.length() == 0 || cur + wordWidth + add <= width) {
                cur += wordWidth + add;
                sb.append(word);
                if (!isLast) {
                    sb.append(' ');
                }
            } else {
                lines.add(sb.substring(0, sb.length() - 1));
                sb.setLength(0);
                sb.append(word).append(' ');
                cur = wordWidth;
            }
            if (c == '\n') {
                // hack to force new line
                cur = width + 1;
            }

        }
        lines.add(sb.substring(0, sb.length()));
        return lines;
    }

}
