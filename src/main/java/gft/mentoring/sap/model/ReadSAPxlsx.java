package gft.mentoring.sap.model;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ReadSAPxlsx {
    static final String SAP_FILE = "./Sample_SAP_DevMan_20180821.xlsx";

    static ArrayList<SAPmodel> readXLSX(String inputFile) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(inputFile));
        Sheet sheet = workbook.getSheetAt(0);
        ArrayList<SAPmodel> sapers = new ArrayList<>();
        for (int i = sheet.getFirstRowNum() + 1; i < sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            sapers.add(createSAPmodelFromRow(row));
        }
        workbook.close();
        return sapers;
    }

    private static SAPmodel createSAPmodelFromRow(Row row) {
        SAPmodel saper = new SAPmodel();
        Cell cell = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        saper.setFirstName(cell.getStringCellValue());
        Cell cell1 = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        saper.setLastName(cell1.getStringCellValue());
        Cell cell2 = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        saper.setInitials(cell2.getStringCellValue());
        Cell cell3 = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        saper.setPersonalNR(cell3.getStringCellValue());
        Cell cell4 = row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        saper.setEmployeeSubGrp(cell4.getStringCellValue());
        Cell cell5 = row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        saper.setPosition(cell5.getStringCellValue());
        Cell cell6 = row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        saper.setJob(cell6.getStringCellValue());
        Cell cell7 = row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        saper.setCostCenter(cell7.getStringCellValue());
        Cell cell8 = row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        saper.setInitEntry(cell8.getDateCellValue());
        Cell cell9 = row.getCell(9, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        saper.setPersNrSuperior(cell9.getStringCellValue());
        Cell cell10 = row.getCell(10, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        saper.setPersNrMentor(cell10.getStringCellValue());
        return saper;
    }


    public static void main(String[] args) throws IOException, InvalidFormatException {
        ArrayList<SAPmodel> models = readXLSX(SAP_FILE);
        models.forEach(saPmodel -> System.out.println(saPmodel));
        System.out.println(models.size());
    }

}
