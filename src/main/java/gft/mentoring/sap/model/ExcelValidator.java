package gft.mentoring.sap.model;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class ExcelValidator {
    private static final String GOLDEN_FILE = "./Sample_SAP_DevMan_20180821.xlsx";

    static List<String> correctColumnOrder() throws IOException, InvalidFormatException {
        return readExcelFileForColumnNames(GOLDEN_FILE);
    }

    boolean verifyExcelColumnOrder(@NotNull String fileName) throws IOException, InvalidFormatException {
        List<String> columnNames = readExcelFileForColumnNames(fileName);
        List<String> correctColumnOrder = correctColumnOrder();
        return correctColumnOrder.equals(columnNames);
    }

    private static List<String> readExcelFileForColumnNames(@NotNull String fileName) throws IOException, InvalidFormatException {
        List<String> columnNames = correctColumnOrder();
        try (Workbook workbook = WorkbookFactory.create(new File(fileName))) {
            Sheet sheet = workbook.getSheetAt(0);
            int lastCell = sheet.getRow(0).getLastCellNum();
            Row header = sheet.getRow(0);
            for (int i = 0; i < lastCell; i++) {
                columnNames.add(header.getCell(i).getRichStringCellValue().toString());
            }
        }
        return columnNames;
    }
}
