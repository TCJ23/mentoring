package gft.mentoring.sap.model;

import gft.mentoring.Family;
import lombok.Value;
import lombok.val;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DisplayName("3 - Main class to test conversion from SAP file to Mentoring Model")
class ConverterSAPTest {
    private static final String SAP_FILE = "./Sample_SAP_DevMan_20180821.xlsx";
    private static final int firstRow = 0;
    private static final String[] COLUMN_NAMES = new String[]{"first name", "last name", "initials", "pers.no.",
            "employee subgroup", "job family", "job", "cost center", "init.entry", "pers.no. superior", "pers.no. mentor"};
    public static final int INIT_ENTRY_COL = 8;
    private static final String EMPTY_STRING = "";

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
        val director = new ConverterSAP().convertInputToSAPMentoringModel(SAP_FILE).get(INIT_ENTRY_COL);
        //when
        val directorLevel = director.getLevel();
        //then
        assertEquals(INIT_ENTRY_COL, directorLevel);
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
        //when
        val sapMentoringModels = new ConverterSAP().convertFilteredRowsSAP(data.iterator());
        val model = sapMentoringModels.get(0);
        val family = model.getFamily();
        val employee = model.isContractor();
        val level = model.getLevel();
        val seniority = model.getSeniority();
        //then
        assertThat(family).isEqualTo(Family.AMS);
        assertThat(employee).isFalse();
        assertThat(level).isEqualTo(6);
        assertThat(seniority).isEqualTo(0);
        assertAll(
                () -> assertEquals("SAP model", model.getFirstName()),
                () -> assertEquals("SAP model", model.getLastName()),
                () -> assertEquals("SAP model", model.getSpecialization()),
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
        cell6.setCellValue("job family");
        Cell cell7 = row0.createCell(6);
        cell7.setCellValue("job");
        Cell cell9 = row0.createCell(7);
        cell9.setCellValue("init.entry");
        Cell cell8 = row0.createCell(8);
        cell8.setCellValue("cost center");
        Cell cell10 = row0.createCell(9);
        cell10.setCellValue("pers.no. superior");
        Cell cell11 = row0.createCell(10);
        cell11.setCellValue("pers.no. mentor");
        data.add(row0);

        Row row1 = sheet.createRow(1);
        for (int i = 0; i < 11; i++) {
            if (i < 4 || i > 7) {
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
            Cell cellSeniority = row1.createCell(7);
            cellSeniority.setCellValue("wrong data format");
        }
        return data;
    }

    @Test
    @DisplayName("3.2.5 - verify that init entry column is converted to days")
    void shouldConvertInitEntryDateToDays() {
        //given
        val data = createSAPSeniortiyExamples();
        val sapMentoringModels = new ConverterSAP().convertFilteredRowsSAP(data.iterator());
        //when
        val worked30daysInGFT = sapMentoringModels.get(0).getSeniority();
        val workedOneYearInGFT = sapMentoringModels.get(1).getSeniority();
        val worked2YearsInGFT = sapMentoringModels.get(2).getSeniority();
        //then
        assertEquals(30, worked30daysInGFT);
        assertEquals(365, workedOneYearInGFT);
        assertEquals(730, worked2YearsInGFT);
    }

    private static List<Row> createSAPSeniortiyExamples() {
        List<Row> data = new ArrayList<>();
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("trs sheet");
        Row headers = applyColumnNamesToSpreadSheet(sheet);
        data.add(headers);
        short lastColumn = headers.getLastCellNum();
        Function<Integer, String> data1 = i -> {
            switch (i) {
                case INIT_ENTRY_COL:
                    return dateCreatorFromNowMinusDays(30);
                default:
                    return null;
            }
        };
        Row row1 = applyDataToRowsInSheet(1, sheet, lastColumn, data1);
        Function<Integer, String> data2 = i -> {
            switch (i) {
                case INIT_ENTRY_COL:
                    return dateCreatorFromNowMinusDays(365);
                default:
                    return null;
            }
        };
        Row row2 = applyDataToRowsInSheet(2, sheet, lastColumn, data2);
        Function<Integer, String> data3 = i -> {
            switch (i) {
                case INIT_ENTRY_COL:
                    return dateCreatorFromNowMinusDays(730);
                default:
                    return null;
            }
        };
        Row row3 = applyDataToRowsInSheet(3, sheet, lastColumn, data3);

        data.add(row1);
        data.add(row2);
        data.add(row3);
        return data;
    }

    @NotNull
    private static Row applyDataToRowsInSheet(int rowNum, Sheet sheet, short dataLength, Function<Integer, String> dataProvider) {
        Row row = sheet.createRow(rowNum);
        for (int i = 0; i < dataLength; i++) {
            String value = dataProvider.apply(i);
            Cell cell = row.createCell(i);
            if (value == null) {
                cell.setCellValue("TRS model");
            } else {
                cell.setCellValue(value);
            }
        }
        return row;
    }

    private static void iterateOverColumnsAndSetValues(Row headers) {
        int columnAmount = 0;
        for (String columnName : COLUMN_NAMES) {
            Cell cell = headers.createCell(columnAmount);
            cell.setCellValue(columnName);
            columnAmount++;
        }
    }

    @NotNull
    private static Row applyColumnNamesToSpreadSheet(Sheet sheet) {
        Row headers = sheet.createRow(firstRow);
        iterateOverColumnsAndSetValues(headers);
        return headers;
    }

    @NotNull
    private static String dateCreatorFromNowMinusDays(int days) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.now().minusDays(days);
        return formatter.format(date);
    }

    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("rowByExamples")
    @DisplayName("3.2.6 - verify that init entry column is converted to days")
    void shouldConvertInitEntryDateToDaysParam(RowExample rowExample) {
        //given
        val dataReader = new SAPInputReader();
        val headers = dataReader.getHeaders(applyColNamesToSingleRow());
        val singleRowData = Collections.singletonList(rowExample.testData).iterator();
        val data = dataReader.readRowsSAP(headers, singleRowData);

        val dataConversion = new ConverterSAP();
        val actualSeniority = dataConversion.getSapMentoringModels(data).get(0).getSeniority();
        //then
        Assertions.assertEquals(rowExample.expected.getSeniority(), actualSeniority);
    }

    @NotNull
    private static Row applyColNamesToSingleRow() {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("test sheet");
        Row headers = sheet.createRow(firstRow);
        iterateOverColumnsAndSetValues(headers);
        return headers;
    }

    private static Stream<RowExample> rowByExamples() {
        val xssfWorkbook = new XSSFWorkbook();
        val sheet = xssfWorkbook.createSheet("trs testing sheet");
        val headers = applyColumnNamesToSpreadSheet(sheet);

        val worked30daysInGFT = addDateCellToRowToSheet(sheet, 1, 30, "data");
        val intModel30 = createSAPMentoringModel(EMPTY_STRING, 30, 5, false, Family.UNDEFINED);

        val workedOneYearInGFT = addDateCellToRowToSheet(sheet, 2, 365, "data");
        val intModel365 = createSAPMentoringModel(EMPTY_STRING, 365, 5, false, Family.UNDEFINED);

        val workedTwoYearsInGFT = addDateCellToRowToSheet(sheet, 3, 730, "data");
        val intModel730 = createSAPMentoringModel(EMPTY_STRING, 730, 5, false, Family.UNDEFINED);

        return Stream.of(
                new RowExample("Working one month(30 days) in GFT ",
                        worked30daysInGFT, intModel30, headers),
                new RowExample("Working one year(365 days) in GFT ",
                        workedOneYearInGFT, intModel365, headers),
                new RowExample("Working one year(730 days) in GFT ",
                        workedTwoYearsInGFT, intModel730, headers)
        );
    }

    private static Row addDateCellToRowToSheet(Sheet sheet, int rownum, int days, String stringCell) {
        val row = sheet.createRow(rownum);
        row.createCell(INIT_ENTRY_COL).setCellValue(dateCreatorFromNowMinusDays(days));
        return row;
    }

    @Value
    static class RowExample {
        private String scenario;
        private Row testData;
        private SAPMentoringModel expected;
        private Row headers;

        @Override
        public String toString() {
            return scenario;
        }
    }

    @NotNull
    private static SAPMentoringModel createSAPMentoringModel(String datasample, int seniority, int level,
                                                             boolean contractor, Family fam) {
        val model = new SAPMentoringModel();
        model.setFirstName(datasample);
        model.setLastName(datasample);
        model.setSpecialization(datasample);
        model.setSapID(datasample);
        model.setLineManagerID(datasample);
        model.setMenteeID(datasample);
        /*meaningful settings*/
        model.setSeniority(seniority);
        model.setLevel(level);
        model.setFamily(fam);
        model.setContractor(contractor);
        return model;
    }
}
