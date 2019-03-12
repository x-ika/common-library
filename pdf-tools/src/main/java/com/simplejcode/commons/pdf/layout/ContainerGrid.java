package com.simplejcode.commons.pdf.layout;

import java.awt.*;

/**
 * @author I. Merabishvili | HighPots
 */
public class ContainerGrid extends com.simplejcode.commons.pdf.layout.Container {

    private View[][] grid;

    private float[] width;
    private float cellMargin;
    private float gridWidth;
    private Color gridColor;
    private Color[] rowBackgrounds;

    private float[] columnX, columnW;
    private int rowsFilled;

    public ContainerGrid() {
    }

    public ContainerGrid(float[] width, float cellMargin, float gridWidth, Color gridColor, Color... pattern) {
        init(width, cellMargin, gridWidth, gridColor, pattern);
    }

    protected void init(float[] width, float cellMargin, float gridWidth, Color gridColor, Color... pattern) {
        this.width = width;
        this.cellMargin = cellMargin;
        this.gridWidth = gridWidth;
        this.gridColor = gridColor;
        int maxRows = 128;
        grid = new View[maxRows][width.length];
        rowBackgrounds = new Color[maxRows];
        for (int i = 0; i < maxRows; i++) {
            rowBackgrounds[i] = pattern[i % pattern.length];
        }
    }

    public int getRowsFilled() {
        return rowsFilled;
    }

    public void setBounds(com.simplejcode.commons.pdf.layout.Rectangle bounds) {
        this.bounds = bounds;
        int n = width.length;
        columnX = new float[n + 1];
        columnW = new float[n];
        columnX[0] = cellMargin + gridWidth;
        float totalWidth = getDrawableBounds().getWidth() - (n + 1) * gridWidth;
        for (int i = 0; i < n; i++) {
            float w = totalWidth * width[i];
            columnX[i + 1] = columnX[i] + w + gridWidth;
            columnW[i] = w - 2 * cellMargin;
        }
        super.setBounds(bounds);
    }

    public void add(int row, int column, View view) {
        add(view);
        grid[row][column] = view;
        rowsFilled = Math.max(rowsFilled, row + 1);
    }

    public void setBackground(int row, Color color) {
        rowBackgrounds[row] = color;
    }

    public void removeLastRow() {
        rowsFilled--;
        for (int i = 0; i < width.length; i++) {
            remove(grid[rowsFilled][i]);
            grid[rowsFilled][i] = null;
        }
    }

    protected float getActualWidth() throws DrawingException {
        return getDrawableBounds().getWidth();
    }

    protected float getActualHeight() throws DrawingException {
        float h = 0;
        for (int i = 0; i < rowsFilled; i++) {
            h += getRowHeight(i);
        }
        return h + (rowsFilled + 1) * gridWidth;
    }

    public void drawViews(IGraphics graphics) throws DrawingException {

        layout();

        drawRowBackgrounds(graphics);

        drawGrid(graphics);

        layoutAndDrawViews(graphics);

    }

    protected void drawRowBackgrounds(IGraphics graphics) throws DrawingException {

        com.simplejcode.commons.pdf.layout.Rectangle rect = getDrawableBounds();

        float x = rect.getX1() + gridWidth;
        float w = rect.getWidth() - 2 * gridWidth;

        float h = gridWidth;
        for (int i = 0; i < rowsFilled; i++) {
            float t = getRowHeight(i);
            graphics.fillRect(rowBackgrounds[i], x, rect.getY1() + h, w, t);
            h += t + gridWidth;
        }
    }

    protected void drawGrid(IGraphics graphics) throws DrawingException {

        if (gridWidth == 0 || gridColor == null) {
            return;
        }

        com.simplejcode.commons.pdf.layout.Rectangle rect = getDrawableBounds();

        // horizontal
        float y = rect.getY1() + gridWidth / 2;
        for (int i = 0; i < rowsFilled; i++) {
            graphics.drawLine(gridColor, gridWidth, rect.getX1(), y, rect.getX2(), y);
            y += getRowHeight(i) + gridWidth;
        }
        graphics.drawLine(gridColor, gridWidth, rect.getX1(), y, rect.getX2(), y);

        // vertical
        y -= gridWidth / 2;
        for (int i = 0; i < width.length + 1; i++) {
            float x1 = rect.getX1() + columnX[i] - cellMargin - gridWidth / 2;
            graphics.drawLine(gridColor, gridWidth, x1, rect.getY1(), x1, y);
        }

    }

    protected void layout() throws DrawingException {

        if (columnW == null) {
            return;
        }

        float total = gridWidth;

        for (int i = 0; i < rowsFilled; i++) {
            View[] row = grid[i];

            float h = getRowHeight(i);

            for (int j = 0; j < width.length; j++) {
                View view = row[j];
                if (view != null) {
                    view.setBounds(boundsFor(columnX[j], total + cellMargin, columnW[j], h - 2 * cellMargin));
                }
            }

            total += h + gridWidth;

        }

        getBounds().setY2(getDrawableBounds().getY1() + total + getFromY2());

    }

    public com.simplejcode.commons.pdf.layout.Rectangle boundsFor(float x1, float y1, float width, float height) {
        return getDrawableBounds().extract(x1, -1, y1, -1, width, height);
    }

    private float getRowHeight(int row) throws DrawingException {
        float rowHeight = 0;
        for (int i = 0; i < width.length; i++) {
            View view = grid[row][i];
            if (view != null) {
                view.setBounds(new com.simplejcode.commons.pdf.layout.Rectangle(0, 0, columnW[i], 0));
                rowHeight = Math.max(rowHeight, view.getMinimumHeight());
            }
        }
        return rowHeight + 2 * cellMargin;
    }

    public com.simplejcode.commons.pdf.layout.Container shallowCopy() {
        com.simplejcode.commons.pdf.layout.Container copy = super.shallowCopy();
        ((ContainerGrid) copy).init(width, cellMargin, gridWidth, gridColor, rowBackgrounds);
        return copy;
    }

}
