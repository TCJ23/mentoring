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
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@DisplayName("2 - Main class to test SAP INPUT")
class SAPInputReaderTest {

    private static final String SAP_FILE = "src/test/Sample_SAP_DevMan_main_SAMPLE.xlsx";
    private static final String BROKEN_FILE = "src/test/broken-file.xlsx";

    private static final int COLUMNS_COUNT = 13;
    private static final int FIRST_ROW = 0;

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
    private static final int PERS_SUBAREA_COL = 12;

    @Test
    @DisplayName("2.1.1 - should create 25 SAP models from sample excel file")
    void shouldCreate25SAPmodelsFromSampleFileIgnoringNullRow() throws IOException, InvalidFormatException, ExcelException {
        //given
        val converterSAP = new ConverterSAP(LocalDate.now());
        val sapInputReader = new SAPInputReader();
        val workbook = WorkbookFactory.create(new File(SAP_FILE));
        /** we decrease by 1 because of 1st row is composed of column names*/
        int headerColumns = 1;
        val notNullRows = sapInputReader.notNullRows(workbook);
        val rowsSize = notNullRows - headerColumns;
        //when
        val filteredModels = converterSAP.convertInputToSAPMentoringModel(SAP_FILE);
        //then
        assertThat(filteredModels).size().isEqualTo(rowsSize);
    }

    @Test
    @Disabled
    @DisplayName("2.1.2 - test IOException, when Excel file is open while program runs ")
    /** We had to disable test 3.1.2 broken build by IO exceptions
     70311 [ERROR] exceptionFileIsLocked  Time elapsed: 1.066 s  <<< FAILURE!
     org.opentest4j.AssertionFailedError: Expected java.io.IOException to be thrown, but nothing was thrown.
     at gft.mentoring.sap.model.SAPInputReaderTest.exceptionFileIsLocked(SAPInputReaderTest.java:66)
     this works fine in IDE and Maven on Windows, issue within locking file on UNIX*/
    void exceptionFileIsLocked() throws IOException {
        FileChannel channel = new RandomAccessFile(SAP_FILE, "rw").getChannel();
        FileLock lock = channel.lock();
        Throwable exception = assertThrows(ExcelException.class, () -> new SAPInputReader().readExcelSAPfile(SAP_FILE));
        assertThat(exception.getMessage()).isEqualToIgnoringCase("Error reading file");
        lock.close();
    }

    @Test
    @DisplayName("2.1.2a - test EmptyFileException, when incorrect file is given ")
    void incorrectFileGiven() throws IOException {
        File tempFile = File.createTempFile("789", "");
        FileChannel channel = new RandomAccessFile(tempFile.getName(), "rw").getChannel();
        FileLock lock = channel.lock();
        Throwable exception = assertThrows(EmptyFileException.class, () -> new SAPInputReader().readExcelSAPfile(tempFile.getName()));
        assertThat(exception.getMessage()).isEqualToIgnoringCase("The supplied file was empty (zero bytes long)");
        System.out.println(exception.getMessage());
        lock.close();
        tempFile.deleteOnExit();
        Files.delete(tempFile.toPath());
    }

    @Test
    @DisplayName("2.1.2b - test EmptyFileException, when incorrect file is given ")
        /* with wrong assertion exception thrown is at gft.mentoring.sap.model.SAPInputReaderTest.exceptionInvalidFormat*/
    void exceptionInvalidFormat() throws IOException {
        File tempFile = File.createTempFile("1011", "");
        Throwable exception = assertThrows(ExcelException.class, () -> new SAPInputReader().readExcelSAPfile(tempFile.getName()));
        assertThat(exception.getMessage()).isEqualToIgnoringCase("File not found or inaccessible");
        System.out.println(exception.getMessage());
        tempFile.deleteOnExit();
        Files.delete(tempFile.toPath());
    }

    @Test
    @DisplayName("2.1.3 - test FileNotFoundException when SAP file is not there")
    void fileNotFound() {
        Throwable exception = assertThrows(ExcelException.class, () -> new SAPInputReader().readExcelSAPfile(SAP_FILE + "empty place"));
        assertThat(exception.getMessage()).isEqualToIgnoringCase("File not found or inaccessible");
    }

    @Test
    @DisplayName("2.1.5a - Verify that column names order matches GOLDEN FILE ")
    void shouldMatchGoldenFileColumnOrder() throws ExcelException {
        //given
        val excelValidator = new ExcelValidator();
        //when
        val fileIsOK = excelValidator.verifyExcelColumnOrder(SAP_FILE);
        //then
        assertThat(fileIsOK).isTrue();
    }

    @Test
    @DisplayName("2.1.5b - Verify that column names order DOES NOT match GOLDEN FILE ")
    void shouldNOTMatchGoldenFileColumnOrder() throws ExcelException {
        //given
        val excelValidator = new ExcelValidator();
        //when
        val fileIsNotOK = excelValidator.verifyExcelColumnOrder(BROKEN_FILE);
        //then
        assertThat(fileIsNotOK).isFalse();
    }

    @Test
    @DisplayName("2.1.5c - Verify that when validating column order also throws exception is thrown")
    void shouldThrowException() {
        Throwable exception = assertThrows(ExcelException.class, () -> new ExcelValidator()
                .verifyExcelColumnOrder(SAP_FILE + "empty place"));
        assertThat(exception.getMessage()).isEqualToIgnoringCase("Unable to check header row correctness");
    }

    @Test
    @DisplayName("2.1.6 - create 1 SAP model from Row without excel file")
    void shouldCreateSingleSAPmodelFromRow() {
        //given
        SAPInputReader createSAPModel = new SAPInputReader();
        Workbook wb = new XSSFWorkbook();
        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("sap sheet");
        List<Row> data = new ArrayList<>();
        Row columnNames = createHeaders();
        Row row1 = sheet.createRow(1);
        for (int i = 0; i < COLUMNS_COUNT; i++) {
            Cell cell = row1.createCell(i);
            cell.setCellValue("SAP model");
        }
        data.add(row1);
        //when
        val headers = createSAPModel.getHeaders(columnNames);
        val sapModels = createSAPModel.readRowsSAP(headers, data.iterator());
        val model = sapModels.get(0);
        //then
        assertAll(
                () -> assertEquals("SAP model", model.getFirstName()),
                () -> assertEquals("SAP model", model.getLastName()),
                () -> assertEquals("SAP model", model.getInitials()),
                () -> assertEquals("SAP model", model.getPersonalNR()),
                () -> assertEquals("SAP model", model.getCostCenter()),
                () -> assertEquals("SAP model", model.getEmployeeSubGrp()),
                () -> assertEquals("SAP model", model.getJobFamily()),
                () -> assertEquals("SAP model", model.getJob()),
                () -> assertEquals("SAP model", model.getCostCenter()),
                () -> assertEquals("SAP model", model.getInitEntry()),
                () -> assertEquals("SAP model", model.getPersNrSuperior()),
                () -> assertEquals("SAP model", model.getPersNrMentor()),
                () -> assertEquals("SAP model", model.getDateOfBirth())
        );
    }

    private static Row createHeaders() {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("test sheet");
        Row row0 = sheet.createRow(FIRST_ROW);
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
        Cell cell8 = row0.createCell(COST_CENTER_COL);
        cell8.setCellValue("cost center");
        Cell cell9 = row0.createCell(INIT_ENTRY_COL);
        cell9.setCellValue("init.entry");
        Cell cell10 = row0.createCell(PERS_NO_SUPERIOR_COL);
        cell10.setCellValue("pers.no. superior");
        Cell cell11 = row0.createCell(PERS_NO_MENTOR_COL);
        cell11.setCellValue("pers.no. mentor");
        Cell cell12 = row0.createCell(DATE_OF_BIRTH_COL);
        cell12.setCellValue("date of birth");
        Cell cell13 = row0.createCell(PERS_SUBAREA_COL);
        cell13.setCellValue("personnel subarea");
        return row0;
    }
}