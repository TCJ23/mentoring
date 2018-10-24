package gft.mentoring.trs.model;

import lombok.val;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("5 - main class for validating TRS conversion to intermediate Mentoring Model ")
class ConvertTRSTest {
    @Test
    @DisplayName("5.1.1 - Should detect if person is leaving GFT")
    void shouldMarkTRSModelasLeaver() {
        //given
        val data = createTRSMentoringModelHelper();
        val trsMentoringModels = new ConvertTRS().convertFromRows(data.iterator());
        //when
        val model = trsMentoringModels.get(0);
        boolean leaver = model.isLeaver();
        //then
        assertEquals(leaver,true);
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
        cell5.setCellValue("technology");
        Cell cell6 = headers.createCell(5);
        cell6.setCellValue("job family");
        Cell cell7 = headers.createCell(6);
        cell7.setCellValue("start date");
        Cell cell8 = headers.createCell(7);
        cell8.setCellValue("office location");
        Cell cell9 = headers.createCell(8);
        cell9.setCellValue("contract type");
        data.add(headers);
        Row row1 = sheet.createRow(1);
        for (int i = 0; i < 11; i++) {
            if (i != 3) {
                Cell cell = row1.createCell(i);
                cell.setCellValue("TRS model");
            }
            Cell statusCell = row1.createCell(2);
            statusCell.setCellValue("Notice Period");
            data.add(row1);
        }
        return data;
    }
}