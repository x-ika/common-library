package com.simplejcode.commons.misc.util;

import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.*;

public final class ExcelUtils {

    private ExcelUtils() {
    }

    //-----------------------------------------------------------------------------------

    public static CellStyle createCellStyle(Workbook workbook) {
        return createCellStyle(workbook, (short) 0);
    }

    public static CellStyle createCellStyle(Workbook workbook, short predefinedColor) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);

        if (predefinedColor != 0) {
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setFillForegroundColor(predefinedColor);
        }

        return style;
    }

    public static Font createFont(Workbook workbook, int height) {
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight((short) height);
        return font;
    }

    public static void populate(Sheet sheet, CellStyle style, int rowInd, int colInd, List<?> values) {
        Row row = sheet.getRow(rowInd);
        for (Object value : values) {
            Cell col = row.createCell(colInd++);
            col.setCellValue(value == null ? "" : value.toString());
            col.setCellStyle(style);
        }
    }

    public static byte[] toByteArray(Workbook workbook) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeTo(workbook, out);
        return out.toByteArray();
    }

    public static void writeToFile(Workbook workbook, File file) {
        try {
            writeTo(workbook, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            throw convert(e);
        }
    }

    public static void writeTo(Workbook workbook, OutputStream out) {
        try {
            workbook.write(out);
            out.close();
        } catch (IOException e) {
            throw convert(e);
        }
    }

    //-----------------------------------------------------------------------------------

    private static RuntimeException convert(Exception e) {
        return ExceptionUtils.wrap(e);
    }

}
