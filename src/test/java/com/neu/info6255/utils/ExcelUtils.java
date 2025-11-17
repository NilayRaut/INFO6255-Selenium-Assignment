package com.neu.info6255.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ExcelUtils {

    private static final String EXCEL_PATH = "src/test/resources/TestData.xlsx";

    /**
     * Get login credentials from LoginData sheet
     * Returns the FIRST row with full email (for NEU login)
     */
    public static Map<String, String> getLoginCredentials() {
        Map<String, String> credentials = new HashMap<>();

        try (FileInputStream fis = new FileInputStream(EXCEL_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("LoginData");
            if (sheet == null) {
                throw new RuntimeException("LoginData sheet not found in Excel file");
            }

            // Get header row
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new RuntimeException("Header row not found in LoginData sheet");
            }

            // Find column indices
            int usernameCol = -1;
            int passwordCol = -1;

            for (Cell cell : headerRow) {
                String header = cell.getStringCellValue().trim();
                if (header.equalsIgnoreCase("Username")) {
                    usernameCol = cell.getColumnIndex();
                } else if (header.equalsIgnoreCase("Password")) {
                    passwordCol = cell.getColumnIndex();
                }
            }

            if (usernameCol == -1 || passwordCol == -1) {
                throw new RuntimeException("Username or Password column not found");
            }

            // Get FIRST data row only (row 1, which has full email)
            Row dataRow = sheet.getRow(1);
            if (dataRow == null) {
                throw new RuntimeException("No login data found");
            }

            String username = getCellValueAsString(dataRow.getCell(usernameCol));
            String password = getCellValueAsString(dataRow.getCell(passwordCol));

            credentials.put("Username", username);
            credentials.put("Password", password);

            System.out.println("✓ Loaded credentials for: " + username);

        } catch (IOException e) {
            throw new RuntimeException("Error reading Excel file: " + e.getMessage(), e);
        }

        return credentials;
    }

    /**
     * Get test data from any sheet (like EventData)
     */
    public static List<Map<String, String>> getTestData(String sheetName) {
        List<Map<String, String>> testDataList = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(EXCEL_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException(sheetName + " sheet not found in Excel file");
            }

            // Get header row
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new RuntimeException("Header row not found in " + sheetName);
            }

            // Get all headers
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(cell.getStringCellValue().trim());
            }

            // Read all data rows
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row)) {
                    continue;
                }

                Map<String, String> rowData = new HashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j);
                    String value = getCellValueAsString(cell);
                    rowData.put(headers.get(j), value);
                }

                testDataList.add(rowData);
            }

            System.out.println("✓ Loaded " + testDataList.size() + " rows from " + sheetName);

        } catch (IOException e) {
            throw new RuntimeException("Error reading Excel file: " + e.getMessage(), e);
        }

        return testDataList;
    }

    /**
     * Get cell value as String regardless of cell type
     */
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // Format number without scientific notation
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    /**
     * Check if a row is empty
     */
    private static boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }

        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                String value = getCellValueAsString(cell);
                if (!value.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }
}