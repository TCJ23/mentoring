package gft.mentoring.sap.model;

import lombok.val;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.EmptyFileException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@DisplayName("2 - Main class to test SAP INPUT")
class SAPInputTest {

    private static final String SAP_FILE = "./Sample_SAP_DevMan_20180821.xlsx";
    private static final String BROKEN_FILE = "./broken-file.xlsx";
    private static final int COLUMNS_COUNT = 11;

    @Test
    @DisplayName("3.1.1 - should create 25 SAP models from sample excel file")
    void shouldCreate25SAPmodelsFromSampleFile() throws IOException, InvalidFormatException, ExcelException {
        //given
        SAPInput sapInput = new SAPInput();
        Workbook workbook = WorkbookFactory.create(new File(SAP_FILE));
        /** we decrease by 1 because of 1st row is composed of column names*/
        int headerColumns = 1;
        val notNullRows = sapInput.notNullRows(workbook);
        val rowsSize = notNullRows - headerColumns;
        //when
        val models = sapInput.readExcelSAPfile(SAP_FILE);
        //then
        assertThat(models).size().isEqualTo(rowsSize);
        assertThat(models).size().isEqualTo(25);
    }

    @Test
    @Disabled
    @DisplayName("3.1.2 - test IOException, when Excel file is open while program runs ")
    /** We had to disable test 3.1.2 broken build by IO exceptions
     70311 [ERROR] exceptionFileIsLocked  Time elapsed: 1.066 s  <<< FAILURE!
     org.opentest4j.AssertionFailedError: Expected java.io.IOException to be thrown, but nothing was thrown.
     at gft.mentoring.sap.model.SAPInputTest.exceptionFileIsLocked(SAPInputTest.java:66)
     this works fine in IDE and Maven on Windows, issue within locking file on UNIX*/
    void exceptionFileIsLocked() throws IOException {
        FileChannel channel = new RandomAccessFile(SAP_FILE, "rw").getChannel();
        FileLock lock = channel.lock();
        Throwable exception = assertThrows(ExcelException.class, () -> new SAPInput().readExcelSAPfile(SAP_FILE));
        assertThat(exception.getMessage()).isEqualToIgnoringCase("Error reading file");
        lock.close();
    }

    @Test
    @DisplayName("3.1.2a - test EmptyFileException, when incorrect file is given ")
    void incorrectFilegiven() throws IOException {
        File tempFile = File.createTempFile("123", "");
        FileChannel channel = new RandomAccessFile(tempFile.getName(), "rw").getChannel();
        FileLock lock = channel.lock();
        Throwable exception = assertThrows(EmptyFileException.class, () -> new SAPInput().readExcelSAPfile(tempFile.getName()));
        assertThat(exception.getMessage()).isEqualToIgnoringCase("The supplied file was empty (zero bytes long)");
        System.out.println(exception.getMessage());
        lock.close();
    }

    @Test
    @DisplayName("3.1.2b - test EmptyFileException, when incorrect file is given ")
        /* with wrong assertion exception thrown is at gft.mentoring.sap.model.SAPInputTest.exceptionInvalidFormat*/
    void exceptionInvalidFormat() throws IOException {
        File tempFile = File.createTempFile("123", "");
        Throwable exception = assertThrows(ExcelException.class, () -> new SAPInput().readExcelSAPfile(tempFile.getName()));
        assertThat(exception.getMessage()).isEqualToIgnoringCase("File not found or inaccessible");
        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("3.1.3 - test FileNotFoundException when SAP file is not there")
    void fileNotFound() {
        Throwable exception = assertThrows(ExcelException.class, () -> new SAPInput().readExcelSAPfile(SAP_FILE + "empty place"));
        assertThat(exception.getMessage()).isEqualToIgnoringCase("File not found or inaccessible");
    }

    @Test
    @DisplayName("3.1.4 - test readRowsSAP correctly without sample file")
    void createModelFromRandomFile() {
        //given
        Row mockRow = mock(Row.class);
        Cell[] mockCells = new Cell[COLUMNS_COUNT];
        for (int i = 0; i < COLUMNS_COUNT; i++)
            mockCells[i] = mock(Cell.class);
        Object[] values = new Object[COLUMNS_COUNT];
        for (int i = 0; i < COLUMNS_COUNT; i++) {
            if (i != 8)
                values[i] = RandomStringUtils.randomAscii(20);
        }

        for (int i = 0; i < COLUMNS_COUNT; i++) {
            Mockito.when(mockRow.getCell(Mockito.eq(i), Mockito.any(Row.MissingCellPolicy.class))).thenReturn(mockCells[i]);
            if (i != 8) {
                when(mockCells[i].getStringCellValue()).thenReturn((String) values[i]);
            }
        }
        Date time = Calendar.getInstance().getTime();
        when(mockCells[8].getCellTypeEnum()).thenReturn(CellType.NUMERIC);
        when(mockCells[8].getNumericCellValue()).thenReturn((double) time.getTime());
        values[8] = String.valueOf((double) time.getTime());
        //when
        val data = Collections.singletonList(mockRow);
        val models = new SAPInput().readRowsSAP(data.iterator());
        //then
        assertThat(models).isNotEmpty();
        int i = 0;
        assertThat(models.get(0).getFirstName()).isEqualTo(values[i++]);
        assertThat(models.get(0).getLastName()).isEqualTo(values[i++]);
        assertThat(models.get(0).getInitials()).isEqualTo(values[i++]);
        assertThat(models.get(0).getPersonalNR()).isEqualTo(values[i++]);
        assertThat(models.get(0).getEmployeeSubGrp()).isEqualTo(values[i++]);
        assertThat(models.get(0).getPosition()).isEqualTo(values[i++]);
        assertThat(models.get(0).getJob()).isEqualTo(values[i++]);
        assertThat(models.get(0).getCostCenter()).isEqualTo(values[i++]);
        assertThat(models.get(0).getInitEntry()).isEqualTo(values[i++]);
        assertThat(models.get(0).getPersNrSuperior()).isEqualTo(values[i++]);
        assertThat(models.get(0).getPersNrMentor()).isEqualTo(values[i]);
    }

    @Test
    @DisplayName("3.1.5a - Verify that column names order matches GOLDEN FILE ")
    void shouldMatchGoldenFileColumnOrder() throws ExcelException {
        //given
        val excelValidator = new ExcelValidator();
        //when
        val fileIsOK = excelValidator.verifyExcelColumnOrder(SAP_FILE);
        //then
        assertThat(fileIsOK).isTrue();
    }

    @Test
    @DisplayName("3.1.5b - Verify that column names order DOES NOT match GOLDEN FILE ")
    void shouldNOTMatchGoldenFileColumnOrder() throws ExcelException {
        //given
        val excelValidator = new ExcelValidator();
        //when
        val fileIsNotOK = excelValidator.verifyExcelColumnOrder(BROKEN_FILE);
        //then
        assertThat(fileIsNotOK).isFalse();
    }

    @Test
    @DisplayName("3.1.5c - Verify that when validating column order also throws exception is thrown")
    void shouldThrowException() throws ExcelException {
        Throwable exception = assertThrows(ExcelException.class, () -> new ExcelValidator()
                .verifyExcelColumnOrder(SAP_FILE + "empty place"));
        assertThat(exception.getMessage()).isEqualToIgnoringCase("Unable to check header row correctness");
    }

    @Test
    @DisplayName("3.1.6 - create 1 SAP model from Row without excel file")
    void shouldCreateSingleSAPmodelFromRow() {
        //given
        SAPInput createSAPModel = new SAPInput();
        Workbook wb = new XSSFWorkbook();
        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("test sheet");

        List<Row> columnNames = new ArrayList<>();
        Row row0 = sheet.createRow(0);
        Cell cell1 = row0.createCell(0);
        cell1.setCellValue("first name");
        Cell cell2 = row0.createCell(1);
        cell2.setCellValue("lasr name");
        Cell cell3 = row0.createCell(2);
        cell3.setCellValue("initials");
        Cell cell4 = row0.createCell(3);
        cell4.setCellValue("pers. no.");
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
        cell10.setCellValue("Pers.no. Superior");
        Cell cell11 = row0.createCell(10);
        cell11.setCellValue("Pers.no. Mentor");
        columnNames.add(row0);
        List<String> headers = createSAPModel.getHeaders(columnNames.iterator().next());

        Row row1 = sheet.createRow(1);
        List<Row> data = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            Cell cell = row1.createCell(i);
            cell.setCellValue("SAP model");
        }
        data.add(row1);
        //when
//        List<SAPmodel> sapModels = createSAPModel.readRowsSAP(headers, data.iterator());
        List<SAPmodel> sapModels = createSAPModel.readRowsSAP(data.iterator());
        SAPmodel model = sapModels.get(0);
        //then
        assertAll(
                () -> assertEquals("SAP model", model.getFirstName()),
                () -> assertEquals("SAP model", model.getLastName()),
                () -> assertEquals("SAP model", model.getInitials()),
                () -> assertEquals("SAP model", model.getPersonalNR()),
                () -> assertEquals("SAP model", model.getCostCenter()),
                () -> assertEquals("SAP model", model.getEmployeeSubGrp()),
                () -> assertEquals("SAP model", model.getPosition()),
                () -> assertEquals("SAP model", model.getJob()),
                () -> assertEquals("SAP model", model.getCostCenter()),
                () -> assertEquals("SAP model", model.getInitEntry()),
                () -> assertEquals("SAP model", model.getPersNrSuperior()),
                () -> assertEquals("SAP model", model.getPersNrMentor())
        );
    }
}