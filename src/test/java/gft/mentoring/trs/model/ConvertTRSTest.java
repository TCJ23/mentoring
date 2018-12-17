package gft.mentoring.trs.model;

import gft.mentoring.Family;
import gft.mentoring.sap.model.ExcelException;
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
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("5 - Main class for validating TRS conversion to intermediate Mentoring Model ")
class ConvertTRSTest {
    private static final LocalDate BASE_DATE = LocalDate.now();
    private static final String TRS_FILE = "src/test/Sample_TRS_DevMan_main_SAMPLE.xlsx";
    private static final int firstRow = 0;
    private static final int nameCol = 0;
    private static final int surnameCol = 1;
    private static final int statsCol = 2;
    private static final int jobFamilyCol = 4;
    private static final int gradeCol = 3;
    private static final int technologyCol = 5;
    private static final int startDateCol = 6;
    private static final int officeLocationCol = 7;
    private static final int contractTypeCol = 8;
    private static final String validatorCheck = "Validator ensures this column cannot be empty";
    private static final String[] COLUMN_NAMES = new String[]{"name", "surname", "status", "grade",
            "job family", "technology", "start date", "office location", "contract type"};
    private static final String EMPTY_STRING = "";


    @Test
    @DisplayName("5.1.1 - Should detect if person is leaving GFT, just hired or permanent employee")
    void shouldMarkTRSModelasLeaver() {
        //given
        val data = createTRSMentoringModelHelper();
        val trsMentoringModels = new ConvertTRS(BASE_DATE).convertFilteredRowsTRS(data.iterator());
        //when
        val modelR0 = trsMentoringModels.get(0);
        val modelR1 = trsMentoringModels.get(1);
        val modelR2 = trsMentoringModels.get(2);
        boolean leaver = modelR0.isLeaver();
        boolean hired = modelR1.isLeaver();
        boolean employee = modelR2.isLeaver();
        //then
        assertTrue(leaver);
        assertTrue(hired);
        assertFalse(employee);
    }

    @Test
    @DisplayName("5.1.2 - Should convert grade column to proper GFT Level")
    void shoulConvertToProperLevel() {
        //given
        val data = createTRSMentoringModelHelper();
        val trsMentoringModels = new ConvertTRS(BASE_DATE).convertFilteredRowsTRS(data.iterator());
        //when
        val level3 = trsMentoringModels.get(0).getLevel();
        val level4 = trsMentoringModels.get(1).getLevel();
        val level7 = trsMentoringModels.get(2).getLevel();

        //then
        assertEquals(3, level3);
        assertEquals(4, level4);
        assertEquals(7, level7);
    }

    @Test
    @DisplayName("5.1.3 - get proper Family from Job Family column")
    void shouldConvertPostionColumnToOneOf10Families() {
        //given
        val data = createTRSMentoringModelHelper();
        val trsMentoringModels = new ConvertTRS(BASE_DATE).convertFilteredRowsTRS(data.iterator());
        //when
        val testingFamily = trsMentoringModels.get(0).getFamily();
        val developmentFamily = trsMentoringModels.get(1).getFamily();
        val missingData = trsMentoringModels.get(2).getFamily();
        //then
        assertEquals(Family.TESTING, testingFamily);
        assertEquals(Family.PROJECT_DEVELOPMENT, developmentFamily);
        assertEquals(Family.UNDEFINED, missingData);
        assertThat(testingFamily).isIn(Family.values());
        assertThat(developmentFamily).isIn(Family.values());
        assertThat(missingData).isIn(Family.values());
    }

    @Test
    @DisplayName("5.1.4 - verify that start date column is converted to days")
    void shouldConvertStartDateToDays() {
        //given
        val data = createTRSMentoringModelHelper();
        val trsMentoringModels = new ConvertTRS(BASE_DATE).convertFilteredRowsTRS(data.iterator());
        //when
        val worked30daysInGFT = trsMentoringModels.get(0).getSeniority();
        val workedOneYearInGFT = trsMentoringModels.get(1).getSeniority();
        val worked2YearsInGFT = trsMentoringModels.get(2).getSeniority();
        //then
        assertEquals(30, worked30daysInGFT);
        assertEquals(365, workedOneYearInGFT);
        assertEquals(730, worked2YearsInGFT);
    }

    @Test
    @DisplayName("5.1.5 - assure that Locations properly")
    void shouldConvertLocalization() {
        //given
        val data = createTRSMentoringModelHelper();
        val trsMentoringModels = new ConvertTRS(BASE_DATE).convertFilteredRowsTRS(data.iterator());
        //when
        val lodzOffice = trsMentoringModels.get(0).getLocalization();
        val poznanOffice = trsMentoringModels.get(1).getLocalization();
        val warsawOffice = trsMentoringModels.get(2).getLocalization();
        //then
        assertThat(lodzOffice).isEqualToIgnoringCase("Łódź");
        assertThat(poznanOffice).isEqualToIgnoringCase("Poznań");
        assertThat(warsawOffice).isEqualToIgnoringCase("Warszawa");
    }

    @Test
    @DisplayName("5.1.6 - assure that Contract Type is verified properly")
    void shouldConvertContractType() {
        //given
        val data = createTRSMentoringModelHelper();
        val trsMentoringModels = new ConvertTRS(BASE_DATE).convertFilteredRowsTRS(data.iterator());
        //when
        val employee = trsMentoringModels.get(0).isContractor();
        val contractor = trsMentoringModels.get(1).isContractor();
        //then
        assertFalse(employee);
        assertTrue(contractor);
    }

    @Test
    @DisplayName("5.1.7 - convert from file, first & lastname")
    void shouldConvertGFTPersonFromFile() throws ExcelException, InvalidFormatException {
        //given
        val trsMentoringModels = new ConvertTRS(BASE_DATE).convertInputToTRSMentoringModel(TRS_FILE);
        //when
        val model = trsMentoringModels.get(0);
        val firstName = model.getFirstName();
        val lastName = model.getLastName();
        //then
        assertThat(firstName).containsIgnoringCase("Adam");
        assertThat(lastName).containsIgnoringCase("Adamczewski");
    }

    @Test
    @DisplayName("5.1.8 - mappping of technology to specialization")
    void shouldConvertGFTSpecializationBasedOnTechnologyColumn() throws ExcelException, InvalidFormatException {
        //given
        val trsMentoringModels = new ConvertTRS(BASE_DATE).convertInputToTRSMentoringModel(TRS_FILE);
        //when
        val dotNetSpecialized = trsMentoringModels.get(0).getSpecialization();
        val amsSupporter = trsMentoringModels.get(1).getSpecialization();
        //then
        assertThat(dotNetSpecialized).containsIgnoringCase(".net");
        assertThat(amsSupporter).containsIgnoringCase("ams");
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

    private static List<Row> createTRSMentoringModelHelper() {
        List<Row> data = new ArrayList<>();
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("trs sheet");
        Row headers = applyColumnNamesToSpreadSheet(sheet);
        data.add(headers);
        short lastColumn = headers.getLastCellNum();
        Function<Integer, String> data1 = i -> {
            switch (i) {
                case 2:
                    return "Notice Period";
                case 3:
                    return "L3";
                case 4:
                    return "Testing";
                case 6:
                    return dateCreatorFromNowMinusDays(30);
                case 7:
                    return "Łódź";
                case 8:
                    return "Permanent";
                default:
                    return null;
            }
        };
        Row row1 = applyDataToRowsInSheet(1, sheet, lastColumn, data1);

        Function<Integer, String> data2 = i -> {
            switch (i) {
                case 2:
                    return "Hired";
                case 3:
                    return "L4";
                case 4:
                    return "Project Development";
                case 6:
                    return dateCreatorFromNowMinusDays(365);
                case 7:
                    return "Poznań";
                case 8:
                    return "Contract";
                default:
                    return null;
            }
        };
        Row row2 = applyDataToRowsInSheet(2, sheet, lastColumn, data2);

        Function<Integer, String> data3 = i -> {
            switch (i) {
                case 2:
                    return "Employee";
                case 3:
                    return "L7";
                case 4:
                    return "";
                case 6:
                    return dateCreatorFromNowMinusDays(730);
                case 7:
                    return "Warszawa";
                default:
                    return null;
            }
        };
        Row row3 = applyDataToRowsInSheet(3, sheet, lastColumn, data3);

        Function<Integer, String> data4 = i -> {
            switch (i) {
                case 6:
                    return "18/10/2018";
                default:
                    return null;
            }
        };
        Row row4 = applyDataToRowsInSheet(4, sheet, lastColumn, data4);

        data.add(row1);
        data.add(row2);
        data.add(row3);
        data.add(row4);
        return data;
    }

    @NotNull
    private static Row applyColumnNamesToSpreadSheet(Sheet sheet) {
        Row headers = sheet.createRow(firstRow);
        iterateOverColumnsAndSetValues(headers);
        return headers;
    }

    @NotNull
    private static Row applyColNamesToSingleRow() {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("test sheet");
        Row headers = sheet.createRow(firstRow);
        iterateOverColumnsAndSetValues(headers);
        return headers;
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
    private static String dateCreatorFromNowMinusDays(int days) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return formatter.format(BASE_DATE.minusDays(days));
    }

    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("rowByExamples")
    @DisplayName("5.2.1 - various scenarios in parametrized test")
    void shouldConvertTRSdataToIntermediateModelFieldsWithDefaultValues(RowExample rowExample) {
        //given
        val dataReader = new TRSInputReader();
        val headers = dataReader.getHeaders(applyColNamesToSingleRow());
        val singleRowData = Collections.singletonList(rowExample.testData).iterator();
        val data = dataReader.readRowsTRS(headers, singleRowData);

        val dataConversion = new ConvertTRS(BASE_DATE);
        val intermediateModel = dataConversion.createTRSMentoringModel(data).get(0);
        //then
        Assertions.assertEquals(rowExample.expected, intermediateModel);
    }

    private static Stream<RowExample> rowByExamples() {
        val xssfWorkbook = new XSSFWorkbook();
        val sheet = xssfWorkbook.createSheet("trs testing sheet");
        val headers = applyColumnNamesToSpreadSheet(sheet);

        val emptyRow = sheet.createRow(1);
        val intModelWithDefaultValues = createTrsMentoringModel(EMPTY_STRING, 0, 0,
                false, false, Family.UNDEFINED);

        val correctDataRow = addValidRowToSheet(sheet, 2);
        val correctModel = createTrsMentoringModel("data", 365, 5,
                false, false, Family.AMS);

        val edgeCasesRow = addEdgeCasesRowToSheet(sheet, 3);
        val edgeCasesModel = createTrsMentoringModel("Edge cases", 1, 8,
                true, true, Family.PROJECT_DEVELOPMENT);

        return Stream.of(
                new RowExample("Empty row should create empty model with default values if they exists ",
                        emptyRow, intModelWithDefaultValues, headers),
                new RowExample("Correct Row Sample should generate valid data",
                        correctDataRow, correctModel, headers),
                new RowExample("Edge cases row, true contractor, leaver, director, lowercasing & spacing ",
                        edgeCasesRow, edgeCasesModel, headers)
        );
    }

    @NotNull
    private static TRSMentoringModel createTrsMentoringModel(String datasample, int seniority, int level,
                                                             boolean contractor, boolean leaver, Family fam) {
        val model = new TRSMentoringModel();
        model.setFirstName(datasample);
        model.setLastName(datasample);
        model.setSpecialization(datasample);
        model.setLocalization(datasample);
        model.setSeniority(seniority);
        model.setLevel(level);
        model.setFamily(fam);
        model.setContractor(contractor);
        model.setLeaver(leaver);
        return model;
    }


    /* parameters are being modified, not produced hence naming convention */
    private static Row addValidRowToSheet(Sheet sheet, int rownum) {
        val row = sheet.createRow(rownum);
        row.createCell(nameCol).setCellValue("data");
        row.createCell(startDateCol).setCellValue(dateCreatorFromNowMinusDays(365));
        row.createCell(surnameCol).setCellValue("data");
        row.createCell(statsCol).setCellValue("data");
        row.createCell(jobFamilyCol).setCellValue("AMS");
        row.createCell(gradeCol).setCellValue("L5");
        row.createCell(technologyCol).setCellValue("data");
        row.createCell(officeLocationCol).setCellValue("data");
        row.createCell(contractTypeCol).setCellValue("data");
        return row;
    }

    private static Row addEdgeCasesRowToSheet(Sheet sheet, int rownum) {
        val row = sheet.createRow(rownum);
        row.createCell(nameCol).setCellValue("Edge cases");
        row.createCell(surnameCol).setCellValue("Edge cases");
        row.createCell(startDateCol).setCellValue(dateCreatorFromNowMinusDays(1));
        row.createCell(statsCol).setCellValue("hired");
        row.createCell(jobFamilyCol).setCellValue("project development");
        row.createCell(gradeCol).setCellValue("ld");
        row.createCell(technologyCol).setCellValue("Edge cases");
        row.createCell(officeLocationCol).setCellValue("Edge cases");
        row.createCell(contractTypeCol).setCellValue("contract");
        return row;
    }

    @Value
    static class RowExample {
        private String scenario;
        private Row testData;
        private TRSMentoringModel expected;
        private Row headers;

        @Override
        public String toString() {
            return scenario;
        }
    }
}