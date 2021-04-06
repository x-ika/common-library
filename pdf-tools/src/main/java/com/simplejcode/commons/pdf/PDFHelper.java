package com.simplejcode.commons.pdf;

import com.simplejcode.commons.pdf.layout.Container;
import com.simplejcode.commons.pdf.layout.Rectangle;
import com.simplejcode.commons.pdf.layout.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.*;
import java.time.Duration;
import java.util.List;
import java.util.*;

public final class PDFHelper {

    private PDFHelper() {
    }

    //-----------------------------------------------------------------------------------
    /*
    IO Utilities
     */

    public static InputStream getStream(String file) throws IOException {
        try {
            return new FileInputStream("src/main/resources/" + file);
        } catch (IOException e) {
            return PDFHelper.class.getResourceAsStream("/" + file);
        }
    }

    public static PDFont loadFont(BasicPDFDocument<?> doc, String file) throws IOException {
        return PDType0Font.load(doc.getDocument(), getStream(file));
    }

    public static BufferedImage getImage(String file) {
        try {
            return ImageIO.read(getStream(file));
        } catch (IOException e) {
            throw new RuntimeException("Can not read file: " + file);
        }
    }

    //-----------------------------------------------------------------------------------
    /*
    Date and Time Formatting Utilities
     */

    private static final String INPUT_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    private static final String PDF_DATE_PATTERN = "MM/dd/yyyy";
    private static final String PDF_TIME_PATTERN = "h:mm a";
    private static final String PDF_DATE_AND_TIME_PATTERN = PDF_DATE_PATTERN + " " + PDF_TIME_PATTERN;
    private static final String PDF_DATE_FULL_MONTH_PATTERN = "MMMMM d, yyyy";

    public static String formatDuration(String duration) {
        Duration d = Duration.parse(duration);
        long hours = d.toHours();
        long minutes = d.minusHours(hours).toMinutes();
        long seconds = d.minusHours(hours).minusMinutes(minutes).getSeconds();
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static String formatDateAndTime(String time) {
        return formatDate(time, PDF_DATE_AND_TIME_PATTERN);
    }

    public static String formatDate(String time) {
        return formatDate(time, PDF_DATE_PATTERN);
    }

    public static String formatDateFullMonth(String time) {
        return formatDate(time, PDF_DATE_FULL_MONTH_PATTERN);
    }

    public static String formatTime(String time) {
        return formatDate(time, PDF_TIME_PATTERN);
    }

    private static String formatDate(String time, String pattern) {
        try {

            SimpleDateFormat inputFormat = new SimpleDateFormat(INPUT_DATE_PATTERN);
            Date dateTime = inputFormat.parse(time);

            SimpleDateFormat outputFormat = new SimpleDateFormat(pattern);
            outputFormat.setTimeZone(inputFormat.getTimeZone());
            return outputFormat.format(dateTime);

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    //-----------------------------------------------------------------------------------
    /*
    Drawing Utilities
     */

    /**
     * TBD
     *
     * @param root
     * @throws DrawingException
     * @throws IOException
     */
    public static void drawWithPaging(BasicPDFDocument doc, View root) throws DrawingException, IOException {

        // split root container
        List<View> pages = new ArrayList<>();
        pages.add(root);

        while (true) {
            root.setBounds(doc.getContentRectangle().copy());
            Stack<Integer> path = new Stack<>();
            if (!defineSplitPath(root, doc.getContentRectangle().getY2(), path)) {
                break;
            }
            View rest = splitOnPath(root, path);
            pages.add(rest);
            root = rest;
        }

        // draw pages one by one
        for (int i = 0; i < pages.size(); i++) {
            doc.addPage();
            View page = pages.get(i);
            page.draw(new PDFGraphics(doc, i));
        }

    }

    private static boolean defineSplitPath(View view, float yMax, Stack<Integer> path) {

        if (view.getY2() <= yMax) {
            return false;
        }

        Container container = (Container) view;
        for (int i = 0; i < container.getNumberOfViews(); i++) {

            View child = container.get(i);

            if (child.getY2() > yMax) {
                // try to split deeper
                path.push(i);
                if (isSplittable(child) && defineSplitPath(child, yMax, path)) {
                    return true;
                }
                // need to split earlier
                if (i == 0) {
                    path.pop();
                    return false;
                }
                // split here
                return true;
            }

        }

        return false;
    }

    private static View splitOnPath(View view, Stack<Integer> path) {
        return splitRecursive(view, path, 0);
    }

    private static View splitRecursive(View view, Stack<Integer> path, int d) {

        Container container = (Container) view;

        int ind = path.get(d);

        Container split = container.shallowCopy();

        if (++d != path.size()) {
            split.add(splitRecursive(container.get(ind), path, d));
        }

        int go = d == path.size() ? ind : ind + 1;

        int numberOfViews = container.getNumberOfViews();
        for (int i = go; i < numberOfViews; i++) {
            split.add(container.get(i));
        }
        for (int i = go; i < numberOfViews; i++) {
            container.remove(go);
        }

        return split;
    }

    private static boolean isSplittable(View view) {
        return view instanceof ContainerVertical;
    }


    public static View createSeparator(Color color, float width, float height) {
        VPlaceHolder separator = new VPlaceHolder(width, height);
        separator.setBackground(color);
        return separator;
    }

    public static void addPageNumbers(BasicPDFDocument<?> doc, IFont font) throws DrawingException {
        PDRectangle rect = doc.getPageRectangle();
        PDDocument document = doc.getDocument();

        int nPages = document.getNumberOfPages();
        for (int i = 0; i < nPages; i++) {
            VText text = new VText(Alignments.BOTTOM_RIGHT, font, Color.black, (i + 1) + " / " + nPages);
            float x1 = rect.getWidth() - 40, y1 = rect.getHeight() - 20;
            text.setBounds(new Rectangle(x1, y1, x1 + text.getMinimumWidth(), y1 + text.getMinimumHeight()));
            text.draw(new PDFGraphics(doc, i));
        }
    }

}
