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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("5 - main class for validating TRS conversion to intermediate Mentoring Model ")
class ConvertTRSTest {
    private static final String TRS_FILE = "./Sample_TRS_DevMan_20181002.xlsx";
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
    public static final String[] COLUMN_NAMES = new String[]{"name", "surname", "status", "grade", "job family", "technology",
            "start date", "office location", "contract type"};


    @Test
    @DisplayName("5.1.1 - Should detect if person is leaving GFT, just hired or permanent employee")
    void shouldMarkTRSModelasLeaver() {
        //given
        val data = createTRSMentoringModelHelper();
        val trsMentoringModels = new ConvertTRS().convertFilteredRows(data.iterator());
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
        val trsMentoringModels = new ConvertTRS().convertFilteredRows(data.iterator());
        //when
//        assertThat(trsMentoringModels).hasSize(3);
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
    void shouldConvertPostionColumnToOneOf10Families() throws ExcelException, InvalidFormatException {
        //given
        val data = createTRSMentoringModelHelper();
        val trsMentoringModels = new ConvertTRS().convertFilteredRows(data.iterator());
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
        val trsMentoringModels = new ConvertTRS().convertFilteredRows(data.iterator());
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
        val trsMentoringModels = new ConvertTRS().convertFilteredRows(data.iterator());
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
        val trsMentoringModels = new ConvertTRS().convertFilteredRows(data.iterator());
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
        val trsMentoringModels = new ConvertTRS().convertInputToTRSMentoringModel(TRS_FILE);
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
        val trsMentoringModels = new ConvertTRS().convertInputToTRSMentoringModel(TRS_FILE);
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

    /*@NotNull
    private static Row applyColumnNamesToSpreadSheet(Sheet sheet) {
        Row headers = sheet.createRow(firstRow);
        Cell cell0 = headers.createCell(nameCol);
        cell0.setCellValue("name");
        Cell cell1 = headers.createCell(surnameCol);
        cell1.setCellValue("surname");
        Cell cell2 = headers.createCell(statsCol);
        cell2.setCellValue("status");
        Cell cell3 = headers.createCell(gradeCol);
        cell3.setCellValue("grade");
        Cell cell4 = headers.createCell(jobFamilyCol);
        cell4.setCellValue("job family");
        Cell cell5 = headers.createCell(technologyCol);
        cell5.setCellValue("technology");
        Cell cell6 = headers.createCell(startDateCol);
        cell6.setCellValue("start date");
        Cell cell7 = headers.createCell(officeLocationCol);
        cell7.setCellValue("office location");
        Cell cell8 = headers.createCell(contractTypeCol);
        cell8.setCellValue("contract type");
        return headers;
    }*/
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

    private static String dateCreatorFromNowMinusDays(int days) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.now().minusDays(days);
        return formatter.format(date);
    }

   /* @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("rowByExamples")
    @DisplayName("5.2.1 - various scenarios in parametrized test")
    void shouldMapTRSinputToIntermediateModelFields(RowExample rowExample) {
        //given
        ConvertTRS dataConversion = new ConvertTRS();
        val trsMentoringModels = new ConvertTRS().convertFilteredRows(Arrays.asList(rowExample.headers, rowExample.testData)
                .iterator());

        val dataInput = new TRSInputReader();
        val headers = dataInput.getHeaders(applyColNamesToSingleRow());
        val singleRowData = Collections.singletonList(rowExample.testData).iterator();
        val rawData = dataInput.readRowsTRS(headers, singleRowData);
        val actualBasicModel = rawData.get(0);
        val actualIntermediateModel = dataConversion.createTRSMentoringModel(rawData);
        //then
//        assertEquals(rowExample.expected, actualBasicModel);
//        Assertions.assertEquals(rowExample.expected, actualBasicModel);
        assertThat(actualBasicModel.getStartDate()).isEqualTo(rowExample.expected.getSeniority());
        assertThat(actualBasicModel.getName()).isEqualTo(rowExample.expected.getFirstName());
        assertThat(actualBasicModel.getJobFamily()).isEqualTo(rowExample.expected.getFamily());
        assertThat(actualBasicModel.getSurname()).isEqualTo(rowExample.expected.getLevel());
    }*/

    private static Stream<RowExample> rowByExamples() {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("trs testing sheet");
        Row headers = applyColumnNamesToSpreadSheet(sheet);

        val emptyRow = addRowToSheet(sheet, 1);
//        Row emptyRow = sheet.createRow(1);
//        emptyRow.createCell(nameCol).setCellValue("X");
     /*   emptyRow.createCell(surnameCol).setCellValue("");
        emptyRow.createCell(statsCol).setCellValue("");
        emptyRow.createCell(jobFamilyCol).setCellValue("");
        emptyRow.createCell(gradeCol).setCellValue("");
        emptyRow.createCell(technologyCol).setCellValue("");
        emptyRow.createCell(startDateCol).setCellValue("");
        emptyRow.createCell(officeLocationCol).setCellValue("");
        emptyRow.createCell(contractTypeCol).setCellValue("");*/

        TRSMentoringModel emptyModel = new TRSMentoringModel();
        emptyModel.setFirstName(validatorCheck);

        /*emptyModel.setLastName("");
        emptyModel.setSpecialization("");
        emptyModel.setLocalization("");
*/
        emptyModel.setContractor(false);
        emptyModel.setSeniority(0);
        emptyModel.setLevel(0);
        emptyModel.setFamily(Family.UNDEFINED);
        emptyModel.setLeaver(false);

        return Stream.of(
                new RowExample("Empty row should create empty model with default values if they exists ",
                        emptyRow, emptyModel, headers)
//                new RowExample("Should map wrong data to UNDEFINED Family ", row2, model2, headers)
        );
    }

    /* parameters are being modified, not produced hence naming convention */
    private static Row addRowToSheet(Sheet sheet, int rowNum) {
        val row = sheet.createRow(rowNum);
        row.createCell(nameCol).setCellValue(validatorCheck);
//        row.createCell(6).setCellValue("11-11-2017");
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