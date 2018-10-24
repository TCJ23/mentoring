package gft.mentoring.trs.model;

import gft.mentoring.sap.model.ExcelException;
import lombok.val;
import org.apache.poi.EmptyFileException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TRSInputTest {
    private static final String TRS_FILE = "./Sample_TRS_DevMan_20181002.xlsx";

    @Test
    @DisplayName("4.1.1 - should create 31 TRS models from sample excel file")
    void shouldCreate31TRSmodelsFromSampleFile() throws IOException, InvalidFormatException, ExcelException {
        //given
        TRSInput trsInput = new TRSInput();
        Workbook workbook = WorkbookFactory.create(new File(TRS_FILE));
        //when
        val models = trsInput.readExcelTRSfile(TRS_FILE);
        //then
        assertThat(models).size().isEqualTo(31);
    }

    @Test
    @DisplayName("4.1.1a - test EmptyFileException, when incorrect file is given ")
    void incorrectFilegiven() throws IOException {
        File tempFile = File.createTempFile("123", "");
        FileChannel channel = new RandomAccessFile(tempFile.getName(), "rw").getChannel();
        FileLock lock = channel.lock();
        Throwable exception = assertThrows(EmptyFileException.class, () -> new TRSInput().readExcelTRSfile(tempFile.getName()));
        assertThat(exception.getMessage()).isEqualToIgnoringCase("The supplied file was empty (zero bytes long)");
        System.out.println(exception.getMessage());
        lock.close();
    }

    @Test
    @DisplayName("4.1.1b - test EmptyFileException, when incorrect file is given ")
        /* with wrong assertion exception thrown is at gft.mentoring.sap.model.SAPInputTest.exceptionInvalidFormat*/
    void exceptionInvalidFormat() throws IOException {
        File tempFile = File.createTempFile("123", "");
        Throwable exception = assertThrows(ExcelException.class, () -> new TRSInput().readExcelTRSfile(tempFile.getName()));
        assertThat(exception.getMessage()).isEqualToIgnoringCase("File not found or inaccessible");
        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("4.1.1c - test FileNotFoundException when SAP file is not there")
    void fileNotFound() {
        Throwable exception = assertThrows(ExcelException.class, () -> new TRSInput().readExcelTRSfile(TRS_FILE + "empty place"));
        assertThat(exception.getMessage()).isEqualToIgnoringCase("File not found or inaccessible");
    }

    @Test
    @DisplayName("4.1.6 - create 1 TRS model from Row without excel file")
    void shouldCreateSingleTRSmodelFromRow() {
        //given
        TRSInput createTRSmodel = new TRSInput();
        Workbook wb = new XSSFWorkbook();
        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("trs sheet");

        List<Row> columnNames = new ArrayList<>();
        Row row0 = sheet.createRow(0);
        Cell cell1 = row0.createCell(0);
        cell1.setCellValue("name");
        Cell cell2 = row0.createCell(1);
        cell2.setCellValue("surname");
        Cell cell3 = row0.createCell(2);
        cell3.setCellValue("status");
        Cell cell4 = row0.createCell(3);
        cell4.setCellValue("grade");
        Cell cell5 = row0.createCell(4);
        cell5.setCellValue("technology");
        Cell cell6 = row0.createCell(5);
        cell6.setCellValue("job family");
        Cell cell7 = row0.createCell(6);
        cell7.setCellValue("start date");
        Cell cell8 = row0.createCell(7);
        cell8.setCellValue("office location");
        Cell cell9 = row0.createCell(8);
        cell9.setCellValue("contract type");
        columnNames.add(row0);

        List<String> headers = createTRSmodel.getHeaders(columnNames.iterator().next());

        Row row1 = sheet.createRow(1);
        List<Row> data = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            Cell cell = row1.createCell(i);
            cell.setCellValue("TRS model");
        }
        data.add(row1);
        //when
        val trsModels = createTRSmodel.readRowsTRS(headers, data.iterator());
        val model = trsModels.get(0);
        //then
        assertAll(
                () -> assertEquals("TRS model", model.getName()),
                () -> assertEquals("TRS model", model.getSurname()),
                () -> assertEquals("TRS model", model.getStatus()),
                () -> assertEquals("TRS model", model.getGrade()),
                () -> assertEquals("TRS model", model.getTechnology()),
                () -> assertEquals("TRS model", model.getJobFamily()),
                () -> assertEquals("TRS model", model.getStartDate()),
                () -> assertEquals("TRS model", model.getOfficeLocation()),
                () -> assertEquals("TRS model", model.getContractType())
        );
    }
}