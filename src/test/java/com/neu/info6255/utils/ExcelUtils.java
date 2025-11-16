package com.neu.info6255.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtils {

    public static List<Map<String, String>> getTestData(String sheetName) {
        List<Map<String, String>> testData = new ArrayList<>();
        String excelPath = "src/test/resources/TestData.xlsx";

        try (FileInputStream fis = new FileInputStream(excelPath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' not found");
            }

            Row headerRow = sheet.getRow(0);
            List<String> headers = new ArrayList<>();

            // Get all headers
            for (Cell cell : headerRow) {
                headers.add(cell.getStringCellValue());
            }

            // Read all data rows
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Map<String, String> rowData = new HashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j);
                    String value = getCellValue(cell);
                    rowData.put(headers.get(j), value);
                }
                testData.add(rowData);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error reading Excel file: " + e.getMessage());
        }

        return testData;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    // Get login credentials from Excel
    public static Map<String, String> getLoginCredentials() {
        List<Map<String, String>> loginData = getTestData("LoginData");
        if (loginData.isEmpty()) {
            throw new RuntimeException("No login data found in Excel");
        }
        return loginData.get(0); // Return first row
    }
}