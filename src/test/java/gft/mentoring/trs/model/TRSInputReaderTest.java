package gft.mentoring.trs.model;

import gft.mentoring.sap.model.ExcelException;
import lombok.Value;
import lombok.val;
import org.apache.poi.EmptyFileException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("4 - Main Class for validating TRS INPUT")
class TRSInputReaderTest {
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
    private static final String[] COLUMN_NAMES = new String[]{"name", "surname", "status", "grade", "job family", "technology",
            "start date", "office location", "contract type"};
    private static final String EMPTY_STRING = "";


    @Test
    @DisplayName("4.1.1 - should create 31 TRS models from sample excel file")
    void shouldCreate31TRSmodelsFromSampleFile() throws IOException, InvalidFormatException, ExcelException {
        //given
        val trsInput = new TRSInputReader();
        val workbook = WorkbookFactory.create(new File(TRS_FILE));
        //* we decrease by 1 because of 1st row is composed of column names
        val headerColumns = 1;
        val notNullRows = trsInput.notNullRows(workbook);
        val rowsSize = notNullRows - headerColumns;
        //when
        val models = trsInput.readExcelTRSfile(TRS_FILE);
        //then
        assertThat(models).size().isEqualTo(rowsSize);
    }

    @Test
    @DisplayName("4.1.1a - test EmptyFileException, when incorrect file is given ")
    void incorrectFilegiven() throws IOException {
        File tempFile = File.createTempFile("123", "");
        FileChannel channel = new RandomAccessFile(tempFile.getName(), "rw").getChannel();
        FileLock lock = channel.lock();
        Throwable exception = assertThrows(EmptyFileException.class, () -> new TRSInputReader().readExcelTRSfile(tempFile.getName()));
        assertThat(exception.getMessage()).isEqualToIgnoringCase("The supplied file was empty (zero bytes long)");
        System.out.println(exception.getMessage());
        lock.close();
    }

    @Test
    @DisplayName("4.1.1b - test EmptyFileException, when incorrect file is given ")
    /*
     with wrong assertion exception thrown is at gft.mentoring.sap.model.SAPInputTest.exceptionInvalidFormat
    */
    void exceptionInvalidFormat() throws IOException {
        File tempFile = File.createTempFile("123", "");
        Throwable exception = assertThrows(ExcelException.class, () -> new TRSInputReader().readExcelTRSfile(tempFile.getName()));
        assertThat(exception.getMessage()).isEqualToIgnoringCase("File not found or inaccessible");
        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("4.1.1c - test FileNotFoundException when TRS file is not there")
    void fileNotFound() {
        Throwable exception = assertThrows(ExcelException.class, () -> new TRSInputReader().readExcelTRSfile(TRS_FILE + "empty place"));
        assertThat(exception.getMessage()).isEqualToIgnoringCase("File not found or inaccessible");
    }

    @Test
    @DisplayName("4.1.1d -IllegalArgumentException(Column name not found in spreadsheet")
    void IllegalArgumentException() {
        TRSInputReader trsInput = new TRSInputReader();
        Row columns = createWRONGHeadersTRS();
        List<String> headers = trsInput.getHeaders(columns);
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("trs sheet");
        Row emptyRow = sheet.createRow(1);
        List<Row> emptyData = new ArrayList<>();
        emptyData.add(emptyRow);
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> new TRSInputReader().readRowsTRS(headers, emptyData.iterator()));
        assertThat(exception.getMessage()).contains("not found in spreadsheet");
    }

    @Test
    @DisplayName("4.1.6 - create 1 TRS model from Row without excel file")
    void shouldCreateSingleTRSmodelFromRow() {
        //given
        val readTRSdata = new TRSInputReader();
        val xssfWorkbook = new XSSFWorkbook();
        val sheet = xssfWorkbook.createSheet("trs sheet");

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

        val headers = readTRSdata.getHeaders(columnNames.iterator().next());

        val row1 = sheet.createRow(1);
        List<Row> data = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            Cell cell = row1.createCell(i);
            cell.setCellValue("TRS model");
        }
        data.add(row1);
        //when
        val trsModels = readTRSdata.readRowsTRS(headers, data.iterator());
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

    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("rowByExamples")
    @DisplayName("4.2.1 - various scenarios in parametrized test")
    void shouldMapTRSinputToTRSBaseModelOnlyStringFields(RowExample rowExample) {
        //given
        val dataInput = new TRSInputReader();
        val headers = dataInput.getHeaders(applyColNamesToSingleRow());
        val singleRowData = Collections.singletonList(rowExample.testData).iterator();
        val rawData = dataInput.readRowsTRS(headers, singleRowData);
        //when
        val actualBasicModel = rawData.get(0);
        //then
        Assertions.assertEquals(rowExample.expected, actualBasicModel);
    }

    private static Stream<TRSInputReaderTest.RowExample> rowByExamples() {
        val wb = new XSSFWorkbook();
        val sheet = wb.createSheet("trs testing sheet");
        val headers = applyColumnNamesToSpreadSheet(sheet);

        val emptyRow = addRowToSheet(sheet);
        val fullDataRow = addRowToSheet(sheet);
        fillRowWithData(fullDataRow);
        /**
         * We are setting this as empty Strings as
         * @see TRSInputReader#stringFromCell(Cell)
         * uses apache poin implementation
         * @see Cell#getStringCellValue()
         */
        val emptyModel = createTRSModel(EMPTY_STRING);
        val fullModel = createTRSModel("data");

        return Stream.of(
                new TRSInputReaderTest.RowExample("Empty row should create TRS model with empty strings",
                        emptyRow, emptyModel, headers),
                new TRSInputReaderTest.RowExample("Row with data should create TRS model with strings only",
                        fullDataRow, fullModel, headers)
        );
    }

    @NotNull
    private static TRSModel createTRSModel(String cellValue) {
        val trsModel = new TRSModel();
        trsModel.setName(cellValue);
        trsModel.setSurname(cellValue);
        trsModel.setStatus(cellValue);
        trsModel.setGrade(cellValue);
        trsModel.setTechnology(cellValue);
        trsModel.setJobFamily(cellValue);
        trsModel.setStartDate(cellValue);
        trsModel.setOfficeLocation(cellValue);
        trsModel.setContractType(cellValue);
        return trsModel;
    }

    @NotNull
    private static Row applyColumnNamesToSpreadSheet(Sheet sheet) {
        Row headers = sheet.createRow(firstRow);
        iterateOverColumnNamesAndSetValues(headers);
        return headers;
    }

    @NotNull
    private static void iterateOverColumnNamesAndSetValues(Row headers) {
        int columnAmount = 0;
        for (String columnName : COLUMN_NAMES) {
            Cell cell = headers.createCell(columnAmount);
            cell.setCellValue(columnName);
            columnAmount++;
        }
    }

    @NotNull
    private static void fillRowWithData(Row rowWithData) {
        int columnAmount = 0;
        for (String columnName : COLUMN_NAMES) {
            Cell cell = rowWithData.createCell(columnAmount);
            cell.setCellValue("data");
            columnAmount++;
        }
    }

    @NotNull
    private static Row applyColNamesToSingleRow() {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("test sheet");
        Row headers = sheet.createRow(firstRow);
        iterateOverColumnNamesAndSetValues(headers);
        return headers;
    }

    /* parameters are being modified, not produced hence naming convention */
    private static Row addRowToSheet(Sheet sheet) {
        val row = sheet.createRow(1);
        return row;
    }

    @Value
    static class RowExample {
        private String scenario;
        private Row testData;
        private TRSModel expected;
        private Row headers;

        @Override
        public String toString() {
            return scenario;
        }
    }

    private static Row createWRONGHeadersTRS() {
        Workbook wb = new XSSFWorkbook();
        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("trs sheet");
        List<Row> columnNames = new ArrayList<>();
        Row row0 = sheet.createRow(0);
        Cell cell1 = row0.createCell(0);
        cell1.setCellValue("THIS HEADER IS WRONG");
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
        return row0;
    }
}
