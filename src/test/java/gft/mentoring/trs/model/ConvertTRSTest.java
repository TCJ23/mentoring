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
                case 2: return "Notice Period";
                case 3: return "L3";
                case 4: return "Testing";
                default: return null;
            }
        };
        Row row1 = createRow(1, sheet, lastColumn, data1);
        Function<Integer, String> data2 = i -> {
            switch (i) {
                case 2: return "Hired";
                case 3: return "L4";
                case 4: return "Project Development";
                default: return null;
            }
        };
        Row row2 = createRow(2, sheet, lastColumn, data2);
        Function<Integer, String> data3 = i -> {
            switch (i) {
                case 2: return "Employee";
                case 3: return "L7";
                case 4: return "";
                default: return null;
            }
        };
        Row row3 = createRow(3, sheet, lastColumn, data3);
        data.add(row1);
        data.add(row2);
        data.add(row3);
        return data;
    }



    @NotNull
    private static Row createRow(int rowNum, Sheet sheet, short dataLength, Function<Integer,String> dataProvider) {
        Row row1 = sheet.createRow(rowNum);
        for (int i = 0; i < dataLength; i++) {
            String value = dataProvider.apply(i);
                Cell cell = row1.createCell(i);
            if (value == null) {
                cell.setCellValue("TRS model");
            } else {
                cell.setCellValue(value);
            }
        }
        return row1;
    }
}