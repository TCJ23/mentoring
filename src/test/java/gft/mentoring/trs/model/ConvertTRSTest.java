package gft.mentoring.trs.model;

import gft.mentoring.Family;
import gft.mentoring.sap.model.ExcelException;
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

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("5 - main class for validating TRS conversion to intermediate Mentoring Model ")
class ConvertTRSTest {
    @Test
    @DisplayName("5.1.1 - Should detect if person is leaving GFT, just hired or permanent employee")
    void shouldMarkTRSModelasLeaver() {
        //given
        val data = createTRSMentoringModelHelper();
        val trsMentoringModels = new ConvertTRS().convertFromRows(data.iterator());
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
        val trsMentoringModels = new ConvertTRS().convertFromRows(data.iterator());
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
    void shouldConvertPostionColumnToOneOf10Families() throws ExcelException, InvalidFormatException {
        //given
        val data = createTRSMentoringModelHelper();
        val trsMentoringModels = new ConvertTRS().convertFromRows(data.iterator());
        //when
        val testingFamily = trsMentoringModels.get(0).getFamily();
        val developmentFamily = trsMentoringModels.get(1).getFamily();
        val missingData = trsMentoringModels.get(2).getFamily();
        //then
        assertEquals(Family.TESTING, testingFamily);
        assertEquals(Family.PROJECT_DEVELOPMENT, developmentFamily);
        assertEquals(Family.UNDEFINED, missingData);
    }

    @Test
    @DisplayName("5.1.4 - verify that start date column is converted to days")
    void shouldConvertStartDateToDays() {
        //given
        val data = createTRSMentoringModelHelper();
        val trsMentoringModels = new ConvertTRS().convertFromRows(data.iterator());
        //when
        val worked30daysInGFT = trsMentoringModels.get(0).getSeniority();
        val workedOneYearInGFT = trsMentoringModels.get(1).getSeniority();
        val worked2YearsInGFT = trsMentoringModels.get(2).getSeniority();
        //then
        assertEquals(30, worked30daysInGFT);
        assertEquals(365, workedOneYearInGFT);
        assertEquals(730, worked2YearsInGFT);
    }

    @NotNull
    private static Row createRow(int rowNum, Sheet sheet, short dataLength, Function<Integer, String> dataProvider) {
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
    @Test
     void oneYear() {
        LocalDate now = LocalDate.now();
        LocalDate oneYearAgo = now.minusDays(1).minusMonths(12).minusYears(1);
//        LocalDate oneYearAgo = now.minusYears(1);
        System.out.println(oneYearAgo);
        Period period = Period.between(oneYearAgo, now);
        int diff = period.getDays();

        assertEquals(365, diff);
    }

    @Test
    void givenTwoDatesInJava8_whenDifferentiating_thenWeGetSix() {
        LocalDate now = LocalDate.now();
        LocalDate sixDaysBehind = now.minusDays(6);

        Period period = Period.between(sixDaysBehind, now);
        int diff = period.getDays();

        assertEquals(6, diff);
    }

    private static List<Row> createTRSMentoringModelHelper() {
        List<Row> data = new ArrayList<>();
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("trs sheet");
        Row headers = sheet.createRow(0);
        Cell cell1 = headers.createCell(0);
        cell1.setCellValue("name");
        Cell cell2 = headers.createCell(1);
        cell2.setCellValue("surname");
        Cell cell3 = headers.createCell(2);
        cell3.setCellValue("status");
        Cell cell4 = headers.createCell(3);
        cell4.setCellValue("grade");
        Cell cell5 = headers.createCell(4);
        cell5.setCellValue("job family");
        Cell cell6 = headers.createCell(5);
        cell6.setCellValue("technology");
        Cell cell7 = headers.createCell(6);
        cell7.setCellValue("start date");
        Cell cell8 = headers.createCell(7);
        cell8.setCellValue("office location");
        Cell cell9 = headers.createCell(8);
        cell9.setCellValue("contract type");
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
                default:
                    return null;
            }
        };
        Row row1 = createRow(1, sheet, lastColumn, data1);
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
                default:
                    return null;
            }
        };
        Row row2 = createRow(2, sheet, lastColumn, data2);
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
                default:
                    return null;
            }
        };
        Row row3 = createRow(3, sheet, lastColumn, data3);
        data.add(row1);
        data.add(row2);
        data.add(row3);
        return data;
    }

    private static String dateCreatorFromNowMinusDays(int days) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.now().minusDays(days);
        return formatter.format(date);
    }
/*    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("rowByExamples")
    @DisplayName("5.2 - various scenarios in parametrized test")
    void shouldMapTRSdataToIntermediateModelFields(RowExample rowExample) {
        //given
        val data = newtrsMenModel();
//                ("Notice period").build();
        val trsMentoringModels = new ConvertTRS().convertFromRows(data.iterator());
        //when

        //then
    }

    private static TRSMentoringModel newtrsMenModel() {
        return new TRSMentoringModelBuilder().build();
    }
    *//*private static TRSMentoringModel newtrsMenModel() {
        return new TRSMentoringModel();
    }*//*
    private static Stream<RowExample> rowByExamples() {
        return Stream.of(
                new RowExample("Should detect if person is leaving GFT", 3, Family.UNDEFINED, true, false)
        );
    }

    @Value
    static class RowExample {
        private String scenario;
        private int grade;
        private Family family;
        private boolean leaver;
        private boolean accepted;

        @Override
        public String toString() {
            return scenario;
        }
    }*/
}
