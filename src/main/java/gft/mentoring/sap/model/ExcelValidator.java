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

    boolean verifyExcelColumnOrder(@NotNull String fileName) throws ExcelException {
        try {
            List<String> columnNames = readExcelFileForHeaderNames(fileName);
            List<String> correctColumnNames = readExcelFileForHeaderNames(GOLDEN_FILE);
            return columnNames.equals(correctColumnNames);
        } catch (Exception e) {
            throw new ExcelException("Unable to check header row correctness", e);
        }
    }

     static List<String> readExcelFileForHeaderNames(@NotNull String fileName) throws IOException, InvalidFormatException {
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
}
