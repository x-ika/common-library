package com.simplejcode.commons.pdf;

import com.simplejcode.commons.pdf.layout.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.io.*;
import java.util.Stack;

/**
 * @author I. Merabishvili | HighPots
 */
public abstract class BasicPDFDocument<T> implements IDrawingContext {

    protected T model;

    protected PDDocument document;
    protected PDRectangle pageRectangle;
    protected Rectangle contentRectangle;

    protected Stack<PDPageContentStream> streams;

    public BasicPDFDocument() {
        streams = new Stack<>();
    }

    //-----------------------------------------------------------------------------------
    /*
    Protected API
     */

    protected PDDocument getDocument() {
        return document;
    }

    protected PDRectangle getPageRectangle() {
        return pageRectangle;
    }

    protected Rectangle getContentRectangle() {
        return contentRectangle;
    }

    protected PDPageContentStream getStream() {
        return streams.peek();
    }

    protected PDPageContentStream getStream(int pageIndex) {
        return streams.get(pageIndex);
    }

    protected void addPage() throws IOException {
        PDPage page = new PDPage(pageRectangle);
        document.addPage(page);
        streams.push(new PDPageContentStream(document, page));
    }

    protected abstract void draw() throws DrawingException, IOException;

    //-----------------------------------------------------------------------------------
    /*
    Public API
     */

    public void setModel(T model) {
        this.model = model;
    }

    public void save(OutputStream out) throws DrawingException, IOException {

        draw();

        for (PDPageContentStream stream : streams) {
            stream.close();
        }
        document.save(out);
        document.close();

    }

}
