/*
package gft.mentoring.sap.model;

import gft.mentoring.Family;
import lombok.val;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DisplayName("3 - Main class to test conversion from SAP file to Mentoring Model")
class ConverterSAPTest {
    private static final String SAP_FILE = "./Sample_SAP_DevMan_20180821.xlsx";

    @Test
    @DisplayName("3.2.1a - Verify SAP Model Job info to Ment.Model Level conversion")
    void shouldConvertLevelToInt() throws ExcelException, InvalidFormatException {
        //given
        val sapMMs = new ConverterSAP().convertInputToSAPMentoringModel(SAP_FILE).get(0);
        //when
        val level3 = sapMMs.getLevel();
        //then
        assertEquals(3, level3);
    }

    @Test
    @DisplayName("3.2.1b - Verify SAP Model Job info for Directors converts to Level 7 or higher")
    void shouldConvertDirectorLevelToInt() throws ExcelException, InvalidFormatException {
        //given
        val director = new ConverterSAP().convertInputToSAPMentoringModel(SAP_FILE).get(8);
        //when
        val directorLevel = director.getLevel();
        //then
        assertEquals(8, directorLevel);
    }

    @Test
    @DisplayName("3.2.2a - Verify SAP Model Employee Subgroup column defines if person is a CONTRACTOR")
    void shouldConvertEmployeeSubgroupColumnToIsContractorTrue() throws ExcelException, InvalidFormatException {
        //given
        val model = new ConverterSAP().convertInputToSAPMentoringModel(SAP_FILE).get(1);
        //when
        val contractor = model.isContractor();
        //then
        assertThat(contractor).isTrue();

    }

    @Test
    @DisplayName("3.2.2b - Verify SAP Model Employee Subgroup column defines if person is an EMPLOYEE")
    void shouldConvertEmployeeSubgroupColumnToIsContractorFalse() throws ExcelException, InvalidFormatException {
        //given
        val model = new ConverterSAP().convertInputToSAPMentoringModel(SAP_FILE).get(0);
        //when
        val employee = model.isContractor();
        //then
        assertThat(employee).isFalse();
    }

    @Test
    @DisplayName("3.2.3 a - Position column should generate proper Family")
    void shouldConvertPostionColumnToOneOf10Families() throws ExcelException, InvalidFormatException {
        //given
        val model = new ConverterSAP().convertInputToSAPMentoringModel(SAP_FILE).get(9);
        //when
        val testerFamily = model.getFamily();
        //then
        assertThat(testerFamily).isEqualTo(Family.TESTING);
    }

    @Test
    @DisplayName("3.2.4 - validate all logic without excel file, based on Row object")
    void shouldValidateAllLogicWithoutExcelFile() {
        //given
        val data = createSAPMentoringModelHelper();
        val sapMentoringModels = new ConverterSAP().convertFromRows(data.iterator());
        //when
        val model = sapMentoringModels.get(0);
        val family = model.getFamily();
        val employee = model.isContractor();
        val level = model.getLevel();
        //then
        assertThat(family).isEqualTo(Family.AMS);
        assertThat(employee).isFalse();
        assertThat(level).isEqualTo(6);
        assertAll(
                () -> assertEquals("SAP model", model.getFirstName()),
                () -> assertEquals("SAP model", model.getLastName()),
                () -> assertEquals("SAP model", model.getSpecialization()),
                () -> assertEquals("SAP model", model.getSeniority()),
                () -> assertEquals("SAP model", model.getLineManagerID()),
                () -> assertThat(model.getSapID().equalsIgnoreCase("sap model")).isTrue(),
                () -> assertThat(model.getFederationID().equalsIgnoreCase("sap model")).isTrue(),
                () -> assertThat(model.getMenteeID().equalsIgnoreCase("sap model")).isTrue()
        );
    }

    private static List<Row> createSAPMentoringModelHelper() {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("test sheet");
        List<Row> data = new ArrayList<>();
        Row row0 = sheet.createRow(0);
        Cell cell1 = row0.createCell(0);
        cell1.setCellValue("first name");
        Cell cell2 = row0.createCell(1);
        cell2.setCellValue("last name");
        Cell cell3 = row0.createCell(2);
        cell3.setCellValue("initials");
        Cell cell4 = row0.createCell(3);
        cell4.setCellValue("pers.no.");
        Cell cell5 = row0.createCell(4);
        cell5.setCellValue("employee subgroup");
        Cell cell6 = row0.createCell(5);
        cell6.setCellValue("position");
        Cell cell7 = row0.createCell(6);
        cell7.setCellValue("job");
        Cell cell8 = row0.createCell(7);
        cell8.setCellValue("cost center");
        Cell cell9 = row0.createCell(8);
        cell9.setCellValue("init.entry");
        Cell cell10 = row0.createCell(9);
        cell10.setCellValue("pers.no. superior");
        Cell cell11 = row0.createCell(10);
        cell11.setCellValue("pers.no. mentor");
        data.add(row0);

        Row row1 = sheet.createRow(1);
        for (int i = 0; i < 11; i++) {
            if (i < 4 || i > 6) {
                Cell cell = row1.createCell(i);
                cell.setCellValue("SAP model");
            }
            Cell cellEmpOrContr = row1.createCell(4);
            cellEmpOrContr.setCellValue("Staff");
            Cell cellFamily = row1.createCell(5);
            cellFamily.setCellValue("AMS");
            Cell cellLevel = row1.createCell(6);
            cellLevel.setCellValue("L6 (Seasoned)");
            data.add(row1);
        }
        return data;
    }
}
*/
