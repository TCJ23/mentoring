package gft.mentoring.sap.model;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class ExcelValidator {
    private static final String GOLDEN_FILE = "./Sample_SAP_DevMan_20180821.xlsx";

    List<String> correctColumnOrder() throws IOException, InvalidFormatException {
        List<String> columnNames = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(new File(GOLDEN_FILE))) {
            Sheet sheet = workbook.getSheetAt(0);
            int lastCell = sheet.getRow(0).getLastCellNum();
            Row header = sheet.getRow(0);
            for (int i = 0; i < lastCell; i++) {
                columnNames.add(header.getCell(i).getRichStringCellValue().toString());
            }
        }
        return columnNames;
    }

    boolean verifyExcelColumnOrder(String fileName, List<String> correctColumnOrder) {
        return false;
    }
}
