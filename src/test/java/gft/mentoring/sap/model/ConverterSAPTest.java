package gft.mentoring.sap.model;

import gft.mentoring.Family;
import lombok.Value;
import lombok.val;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DisplayName("3 - Main class to test conversion from SAP file to Mentoring Model")
class ConverterSAPTest {
    private static final String SAP_FILE = "./Sample_SAP_DevMan_20180821.xlsx";
    private static final int firstRow = 0;
    private static final String[] COLUMN_NAMES = new String[]{"first name", "last name", "initials",
            "pers.no.", "employee subgroup", "job family",
            "job", "cost center", "init.entry", "pers.no. superior",
            "pers.no. mentor", "date of birth"};
    private static final int COLUMNS_COUNT = 12;
    private static final int FIRST_NAME_COL = 0;
    private static final int LAST_NAME_COL = 1;
    private static final int INITIALS_COL = 2;
    private static final int PERS_NO_COL = 3;
    private static final int EMPLOYEE_SUBGRP_COL = 4;
    private static final int JOB_FAMILY_COL = 5;
    private static final int JOB_COL = 6;
    private static final int COST_CENTER_COL = 7;
    private static final int INIT_ENTRY_COL = 8;
    private static final int PERS_NO_SUPERIOR_COL = 9;
    private static final int PERS_NO_MENTOR_COL = 10;
    private static final int DATE_OF_BIRTH_COL = 11;
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
        val data = createSAPMentoringModelHelperFromRow();
        //when
        val sapMentoringModels = new ConverterSAP().convertFilteredRowsSAP(data.iterator());
        val model = sapMentoringModels.get(0);
        val family = model.getFamily();
        val employee = model.isContractor();
        val level = model.getLevel();
        val seniority = model.getSeniority();
        val age = model.getAge();
        //then
        assertThat(family).isEqualTo(Family.AMS);
        assertThat(employee).isFalse();
        assertThat(level).isEqualTo(6);
        assertThat(seniority).isEqualTo(0);
        assertThat(age).isEqualTo(0);
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

   /* private static List<Row> cellCreatorHelper() {
        Map<Integer, String> m = new HashMap<>() {
            @Override
            String get(Integer i) {
                String v = super.get(i);
                return v != null ? v : "SAP model";
            }
        }
    }*/


    private static List<Row> createSAPMentoringModelHelperFromRow() {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("test sheet");
        List<Row> data = new ArrayList<>();
        Row row0 = sheet.createRow(0);
        Cell cell1 = row0.createCell(FIRST_NAME_COL);
        cell1.setCellValue("first name");
        Cell cell2 = row0.createCell(LAST_NAME_COL);
        cell2.setCellValue("last name");
        Cell cell3 = row0.createCell(INITIALS_COL);
        cell3.setCellValue("initials");
        Cell cell4 = row0.createCell(PERS_NO_COL);
        cell4.setCellValue("pers.no.");
        Cell cell5 = row0.createCell(EMPLOYEE_SUBGRP_COL);
        cell5.setCellValue("employee subgroup");
        Cell cell6 = row0.createCell(JOB_FAMILY_COL);
        cell6.setCellValue("job family");
        Cell cell7 = row0.createCell(JOB_COL);
        cell7.setCellValue("job");
        Cell cell9 = row0.createCell(INIT_ENTRY_COL);
        cell9.setCellValue("init.entry");
        Cell cell8 = row0.createCell(COST_CENTER_COL);
        cell8.setCellValue("cost center");
        Cell cell10 = row0.createCell(PERS_NO_SUPERIOR_COL);
        cell10.setCellValue("pers.no. superior");
        Cell cell11 = row0.createCell(PERS_NO_MENTOR_COL);
        cell11.setCellValue("pers.no. mentor");
        Cell cell12 = row0.createCell(DATE_OF_BIRTH_COL);
        cell12.setCellValue("date of birth");
        data.add(row0);

        Row row1 = sheet.createRow(1);

        Map<Integer, String> values = new MapWithDefault<>("SAP model");
        values.put(EMPLOYEE_SUBGRP_COL, "Staff");
        values.put(JOB_FAMILY_COL, "AMS");
        values.put(JOB_COL, "L6 (Seasoned)");
        values.put(INIT_ENTRY_COL, "wrong data format");
        values.put(DATE_OF_BIRTH_COL, "wrong data format");

        data.add(row1);

        for (int i = 0; i < COLUMNS_COUNT; i++) {
            Cell cell = row1.createCell(i);
            cell.setCellValue(values.get(i));
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

    @Test
    @DisplayName("3.2.8 - verify that date of birth column is converted to years")
    void shouldConvertDateOfBirthToYears() {
        //given
        List<Row> data = new ArrayList<>();
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("trs sheet");
        Row headers = applyColumnNamesToSpreadSheet(sheet);
        data.add(headers);

        Row row1 = sheet.createRow(1);
        Map<Integer, String> values = new MapWithDefault<>("string data");
        /* date of birth 35 years ago */
        values.put(DATE_OF_BIRTH_COL, "23-09-1983");
        data.add(row1);
        for (int i = 0; i < COLUMNS_COUNT; i++) {
            Cell cell = row1.createCell(i);
            cell.setCellValue(values.get(i));
        }
        //when
        List<SAPMentoringModel> list = new ConverterSAP().convertFilteredRowsSAP(data.iterator());
        int actualAge = list.get(0).getAge();
        //then
        Assertions.assertEquals(35,actualAge);
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

    @NotNull
    private static String dateCreatorFromNowMinusYears(int years) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.now().minusYears(years);
        return formatter.format(date);
    }

    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("seniorityPerRowPerExample")
    @DisplayName("3.2.6 - verify that init entry column is converted to days")
    void shouldConvertInitEntryDateToDaysParam(RowExample rowExample) {
        //given
        val dataReader = new SAPInputReader();
        val headers = dataReader.getHeaders(applyColNamesToSingleRow());
        val singleRowData = Collections.singletonList(rowExample.testData).iterator();
        val data = dataReader.readRowsSAP(headers, singleRowData);
        //when
        val dataConversion = new ConverterSAP();
        val actualSeniority = dataConversion.getSapMentoringModels(data).get(0).getSeniority();
        //then
        Assertions.assertEquals(rowExample.expected.getSeniority(), actualSeniority);
    }

    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("agePerRowPerExample")
    @DisplayName("3.2.7 - verify that date of birth column is producing age of GFT employee")
    void shouldConvertDateOfBirthColumnToYearsParam(RowExample rowExample) {
        //given
        val dataReader = new SAPInputReader();
        val headers = dataReader.getHeaders(applyColNamesToSingleRow());
        val singleRowData = Collections.singletonList(rowExample.testData).iterator();
        val data = dataReader.readRowsSAP(headers, singleRowData);
        //when
        val dataConversion = new ConverterSAP();
        val actualAge = dataConversion.getSapMentoringModels(data).get(0).getAge();
        //then
        Assertions.assertEquals(rowExample.expected.getAge(), actualAge);
    }


    @NotNull
    private static Row applyColNamesToSingleRow() {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("test sheet");
        Row headers = sheet.createRow(firstRow);
        iterateOverColumnsAndSetValues(headers);
        return headers;
    }

    private static Stream<RowExample> agePerRowPerExample() {
        val xssfWorkbook = new XSSFWorkbook();
        val sheet = xssfWorkbook.createSheet("trs testing sheet");
        val headers = applyColumnNamesToSpreadSheet(sheet);

        val baby1YearOld = addDateCellToRowToSheet(sheet, 1, 30, 1, "data");
        val intModelBaby = createSAPMentoringModel(EMPTY_STRING, 30, 5, false, Family.UNDEFINED
                , 1);

        val senior30years = addDateCellToRowToSheet(sheet, 2, 365, 30, "data");
        val intModel365 = createSAPMentoringModel(EMPTY_STRING, 365, 5, false, Family.UNDEFINED
                , 30);

        val senior35years = addDateCellToRowToSheet(sheet, 3, 730, 35, "data");
        val intModel730 = createSAPMentoringModel(EMPTY_STRING, 730, 5, false, Family.UNDEFINED
                , 35);

        return Stream.of(
                new RowExample("I am baby 1 year old ",
                        baby1YearOld, intModelBaby, headers),
                new RowExample("I'm 30 years old ",
                        senior30years, intModel365, headers),
                new RowExample("I'm 35 years old",
                        senior35years, intModel730, headers)
        );
    }

    private static Stream<RowExample> seniorityPerRowPerExample() {
        val xssfWorkbook = new XSSFWorkbook();
        val sheet = xssfWorkbook.createSheet("trs testing sheet");
        val headers = applyColumnNamesToSpreadSheet(sheet);

        val worked30daysInGFT = addDateCellToRowToSheet(sheet, 1, 30, 1, "data");
        val intModel30 = createSAPMentoringModel(EMPTY_STRING, 30, 5, false, Family.UNDEFINED
                , 1);

        val workedOneYearInGFT = addDateCellToRowToSheet(sheet, 2, 365, 1, "data");
        val intModel365 = createSAPMentoringModel(EMPTY_STRING, 365, 5, false, Family.UNDEFINED
                , 1);

        val workedTwoYearsInGFT = addDateCellToRowToSheet(sheet, 3, 730, 1, "data");
        val intModel730 = createSAPMentoringModel(EMPTY_STRING, 730, 5, false, Family.UNDEFINED
                , 1);

        return Stream.of(
                new RowExample("Working one month(30 days) in GFT ",
                        worked30daysInGFT, intModel30, headers),
                new RowExample("Working one year(365 days) in GFT ",
                        workedOneYearInGFT, intModel365, headers),
                new RowExample("Working one year(730 days) in GFT ",
                        workedTwoYearsInGFT, intModel730, headers)
        );
    }

    private static Row addDateCellToRowToSheet(Sheet sheet, int rownum, int days, int age, String stringCell) {
        val row = sheet.createRow(rownum);
        row.createCell(INIT_ENTRY_COL).setCellValue(dateCreatorFromNowMinusDays(days));
        row.createCell(DATE_OF_BIRTH_COL).setCellValue(dateCreatorFromNowMinusYears(age));
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
    private static SAPMentoringModel createSAPMentoringModel(String dataSample, int seniority, int level,
                                                             boolean contractor, Family fam, int age) {
        val model = new SAPMentoringModel();
        model.setFirstName(dataSample);
        model.setLastName(dataSample);
        model.setSpecialization(dataSample);
        model.setSapID(dataSample);
        model.setLineManagerID(dataSample);
        model.setMenteeID(dataSample);
        /*meaningful settings*/
        model.setSeniority(seniority);
        model.setLevel(level);
        model.setFamily(fam);
        model.setContractor(contractor);
        model.setAge(age);
        return model;
    }

    static class MapWithDefault<K, V> extends HashMap<K, V> {
        private V defaultVal;

        private MapWithDefault(V defaultVal) {
            this.defaultVal = defaultVal;
        }

        @Override
        public V get(Object o) {
            V v = super.get(o);
            return v != null ? v : defaultVal;
        }
    }
}