package gft.mentoring.sap.model;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

class ExcelValidator {
    private static final String GOLDEN_FILE = "./Sample_SAP_DevMan_main_SAMPLE.xlsx";
    private static final String SAP_2_MENTEES_ASSIGNED = "SAP_2MenteesAssigned.xlsx";
    private static final String SAP_AGE_EXAMPLES = "SAP_age_conversion_check.xlsx";
    private static final String WERONIKA = "Weronika format z SAPa.xlsx";


    boolean verifyExcelColumnOrder(@NotNull String fileName) throws ExcelException {
        try {
            List<String> columnNames = readExcelFileForHeaderNames(fileName);
            List<String> correctColumnNames = readExcelFileForHeaderNames(GOLDEN_FILE);
            return columnNames.equals(correctColumnNames);
        } catch (Exception e) {
            throw new ExcelException("Unable to check header row correctness", e);
        }
    }

    private static List<String> readExcelFileForHeaderNames(@NotNull String fileName) throws IOException, InvalidFormatException {
        List<String> columnNames = new ArrayList<>();
        Workbook workbook = WorkbookFactory.create(new File(fileName));
        Sheet sheet = workbook.getSheetAt(0);
        int lastCell = sheet.getRow(0).getLastCellNum();
        Row header = sheet.getRow(0);
        for (int i = 0; i < lastCell; i++) {
            columnNames.add(header.getCell(i).getRichStringCellValue().toString());
        }
        workbook.close();
        return columnNames;
    }

    private static List<String> getCellContent(@NotNull String fileName) throws IOException, InvalidFormatException {
        DataFormatter formatter = new DataFormatter();
        List<String> cellContent = new ArrayList<>();
        Workbook workbook = WorkbookFactory.create(new File(fileName));
        Sheet sheet = workbook.getSheetAt(0);
        int lastCell = sheet.getRow(0).getLastCellNum();
        Row header = sheet.getRow(0);
        for (int i = 0; i < lastCell; i++) {
            cellContent.add(header.getCell(i).getRichStringCellValue().toString());
        }
        Row firstRow = sheet.getRow(1);
        for (int i = 0; i < lastCell; i++) {
            cellContent.add(formatter.formatCellValue(firstRow.getCell(i)));
        }
        workbook.close();
        return cellContent;
    }

    public static void main(String[] args) throws IOException, InvalidFormatException {
//        List<String> strings = readExcelFileForHeaderNames(GOLDEN_FILE);
//        strings.forEach(System.out::println);
//        List<String> cells = getCellContent(WERONIKA);
//        cells.forEach(System.out::println);
        getCellsContentPOI(WERONIKA);
    }

    private static void getCellsContentPOI(@NotNull String fileName) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(fileName));
        DataFormatter formatter = new DataFormatter();
        Sheet sheet1 = workbook.getSheetAt(0);
        for (Row row : sheet1) {
            for (Cell cell : row) {
                CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
                System.out.print(cellRef.formatAsString());
                System.out.print(" - ");

                // get the text that appears in the cell by getting the cell value and applying any data formats (Date, 0.00, 1.23e9, $1.23, etc)
                String text = formatter.formatCellValue(cell);
                System.out.println(text);

                // Alternatively, get the value and format it yourself
                switch (cell.getCellTypeEnum()) {
                    case STRING:
                        System.out.println(cell.getRichStringCellValue().getString());
                        break;
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            System.out.println(cell.getDateCellValue());
                        } else {
                            System.out.println(cell.getNumericCellValue());
                        }
                        break;
                    case BOOLEAN:
                        System.out.println(cell.getBooleanCellValue());
                        break;
                    case FORMULA:
                        System.out.println(cell.getCellFormula());
                        break;
                    case BLANK:
                        System.out.println();
                        break;
                    default:
                        System.out.println();
                }
            }
        }
        workbook.close();
    }
}
