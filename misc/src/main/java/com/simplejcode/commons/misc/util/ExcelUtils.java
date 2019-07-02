package com.simplejcode.commons.misc.util;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;

import java.util.*;

public final class ExcelUtils {

    private ExcelUtils() {
    }

    //-----------------------------------------------------------------------------------
    /*
    Heavily Custom
     */

    public static CellStyle createHeaderStyle(Workbook workbook) {

        CellStyle style = createCellStyle(workbook);
        style.setFont(createFont(workbook, (short) 300));

        return style;
    }

    public static CellStyle createContentStyle(Workbook workbook) {

        CellStyle style = createCellStyle(workbook);
        style.setFont(createFont(workbook, (short) 200));

        return style;
    }

    public static CellStyle createWarnStyle(Workbook workbook) {

        CellStyle style = createCellStyle(workbook, HSSFColor.HSSFColorPredefined.LIGHT_YELLOW.getIndex());
        style.setFont(createFont(workbook, (short) 200));


        return style;
    }

    //-----------------------------------------------------------------------------------
    /*
    More General
     */

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

    public static CellStyle createCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);

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

}
