package com.simplejcode.commons.misc.util;

import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public final class ExcelUtils {

    private ExcelUtils() {
    }

    //-----------------------------------------------------------------------------------

    public static Workbook createWorkbook(boolean xssf) {
        try {
            return WorkbookFactory.create(xssf);
        } catch (IOException e) {
            throw convert(e);
        }
    }

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

    public static Font createFont(Workbook workbook, boolean bold, int height) {
        Font font = workbook.createFont();
        font.setBold(bold);
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

    public static void populate(Sheet sheet, CellStyle[] styles, int rowInd, int colInd, List<?> values) {
        Row row = sheet.getRow(rowInd);
        for (int i = 0; i < styles.length; i++) {
            Cell col = row.createCell(colInd++);
            col.setCellStyle(styles[i]);
            Object value = values.get(i);
            if (value == null) {
                continue;
            }
            if (value instanceof Boolean) {
                col.setCellValue((Boolean) value);
                continue;
            }
            if (value instanceof Double) {
                col.setCellType(CellType.NUMERIC);
                col.setCellValue((Double) value);
                continue;
            }
            if (value instanceof Date) {
                col.setCellValue((Date) value);
                continue;
            }
            col.setCellValue(value.toString());
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

    @Deprecated
    public static List<ExcelRow> parseSingleSheetFile(InputStream inputStream, int numberOfColumns) {
        try {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            List<ExcelRow> rows = new ArrayList<>();

            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                ExcelRow excelRow = new ExcelRow();
                for (int j = 0; j < numberOfColumns; j++) {
                    excelRow.getCells().add(new ExcelCell(getCellValue(row.getCell(j))));
                }
                rows.add(excelRow);
            }

            return rows;
        } catch (IOException e) {
            throw convert(e);
        }
    }

    public static Map<String, List<ExcelRow>> parseFile(InputStream inputStream) {
        try {
            Workbook workbook = WorkbookFactory.create(inputStream);

            Map<String, List<ExcelRow>> result = new HashMap<>();

            int sheetCount = workbook.getNumberOfSheets();

            DataFormatter formatter = new DataFormatter();

            for (int sheetInd = 0; sheetInd < sheetCount; sheetInd++) {

                String sheetName = workbook.getSheetName(sheetInd);
                Sheet sheet = workbook.getSheetAt(sheetInd);

                List<ExcelRow> rows = new ArrayList<>();

                int numberOfColumns = sheet.getRow(0).getLastCellNum();

                for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) {
                        break;
                    }

                    ExcelRow excelRow = new ExcelRow();
                    for (int j = 0; j < numberOfColumns; j++) {
                        Cell cell = row.getCell(j);
                        String value = formatter.formatCellValue(cell);
                        excelRow.getCells().add(new ExcelCell(StringUtils.isBlank(value) ? null : value));
                    }
                    rows.add(excelRow);
                }

                result.put(sheetName, rows);

            }

            return result;
        } catch (IOException e) {
            throw convert(e);
        }
    }

    private static Object getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                return BigDecimal.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case STRING:
                return cell.getStringCellValue();
            default:
                return null;
        }
    }

    //-----------------------------------------------------------------------------------

    private static RuntimeException convert(Exception e) {
        return ExceptionUtils.wrap(e);
    }

}
