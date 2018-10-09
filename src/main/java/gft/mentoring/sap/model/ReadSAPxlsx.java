package gft.mentoring.sap.model;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ReadSAPxlsx {
    static final String SAP_FILE = "./Sample_SAP_DevMan_20180821.xlsx";

    public static void main(String[] args) throws IOException, InvalidFormatException {

        Workbook workbook = WorkbookFactory.create(new File(SAP_FILE));

        System.out.println("Workbook should have only 1 Sheet on this one has " + workbook.getNumberOfSheets() + "huuray ");

        System.out.println("Retrieving this Sheet ");
        workbook.forEach(sheet -> {
            System.out.println("with name" + sheet.getSheetName());
        });

        Sheet sheet = workbook.getSheetAt(0);

        System.out.println("\n\nIterating over Rows and Columns\n");
        sheet.forEach(row -> {
            row.forEach(cell -> {
                printCellValue(cell);
            });
            System.out.println();
        });

        ArrayList<SAPmodel> sapers = new ArrayList<>();

        for (int i = sheet.getFirstRowNum() + 1; i < sheet.getLastRowNum(); i++) {
            SAPmodel saper = new SAPmodel();
            Row row = sheet.getRow(i);
            for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                if (j == 0) {
                    saper.setFirstName(cell.getStringCellValue());
                }
                if (j == 1) {
                    saper.setLastName(cell.getStringCellValue());
                }
                if (j == 2) {
                    saper.setInitials(cell.getStringCellValue());
                }
                if (j == 3) {
                    saper.setPersonalNR(cell.getStringCellValue());
                }
                if (j == 4) {
                    saper.setEmployeeSubGrp(cell.getStringCellValue());
                }
                if (j == 5) {
                    saper.setPosition(cell.getStringCellValue());
                }
                if (j == 6) {
                    saper.setJob(cell.getStringCellValue());
                }
                if (j == 7) {
                    saper.setCostCenter(cell.getStringCellValue());
                }
                if (j == 7) {
                    saper.setCostCenter(cell.getStringCellValue());
                }
                if (j == 8) {
                    saper.setInitEntry(cell.getDateCellValue());
                }
                if (j == 9) {
                    saper.setPersNrSuperior(cell.getStringCellValue());
                }
                if (j == 10) {
                    saper.setPersNrMentor(cell.getStringCellValue());
                }
            }
            System.out.println(saper);
            sapers.add(saper);
        }
        System.out.println(sapers);
        // Closing the workbook
        workbook.close();
    }

    private static void printCellValue(Cell cell) {
        switch (cell.getCellTypeEnum()) {
            case BOOLEAN:
                System.out.print(cell.getBooleanCellValue());
                break;
            case STRING:
                System.out.print(cell.getRichStringCellValue().getString());
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    System.out.print(cell.getDateCellValue());
                } else {
                    System.out.print(cell.getNumericCellValue());
                }
                break;
            case FORMULA:
                System.out.print(cell.getCellFormula());
                break;
            case BLANK:
                System.out.print("");
                break;
            default:
                System.out.print("");
        }

        System.out.print("\t");
    }

}
