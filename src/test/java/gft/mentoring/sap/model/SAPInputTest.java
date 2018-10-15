package gft.mentoring.sap.model;

import lombok.Cleanup;
import lombok.val;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@DisplayName("Main class to test SAP INPUT")
class SAPInputTest {

    private static final String SAP_FILE = "./Sample_SAP_DevMan_20180821.xlsx";
    private static final String BROKEN_FILE = "./broken-file.xlsx";
    private static final int COLUMNS_COUNT = 11;

    @Test
    @DisplayName("3.1 - should create 25 SAP models from sample excel file")
    void shouldCreate25SAPmodelsFromSampleFile() throws IOException, InvalidFormatException {
        //given
        SAPInput sapInput = new SAPInput();
        Workbook workbook = WorkbookFactory.create(new File(SAP_FILE));
        /* we decrease by 1 because of 1st row is composed of column names*/
        int headerColumns = 1;
        val notNullRows = sapInput.notNullRows(workbook);
        val rowsSize = notNullRows - headerColumns;
        //when
        val models = sapInput.readExcelSAPfile(SAP_FILE);
        //then
        assertThat(models).size().isEqualTo(rowsSize);
        assertThat(models).size().isEqualTo(25);
        for (SAPmodel model : models) {
            System.out.println(model);
        }
    }

    @Test
    @DisplayName("3.1 - test IOException, when Excel file is open while program runs ")
    void exceptionFileIsLocked() throws IOException {
        FileChannel channel = new RandomAccessFile(SAP_FILE, "rw").getChannel();
//        @Cleanup
        FileLock lock = channel.tryLock();

        Throwable exception = assertThrows(IOException.class, () -> {
            new SAPInput().readExcelSAPfile(SAP_FILE);
        });
        assertEquals("The process cannot access the file because another process has locked a portion of the file"
                , exception.getMessage());
        lock.close();
    }

    @Test
    @DisplayName("3.1 - test FileNotFoundException when SAP file is not there")
    void fileNotFound() throws IOException {
        Throwable exception = assertThrows(FileNotFoundException.class, () -> {
            new SAPInput().readExcelSAPfile(SAP_FILE + "empty place");
        });
    }

    @Test
    @DisplayName("3.1 - test readRows correctly without sample file")
    void createModelFromRandomFile() {
        //given
        Row mockRow = mock(Row.class);
        Cell[] mockCells = new Cell[COLUMNS_COUNT];
        for (int i = 0; i < COLUMNS_COUNT; i++)
            mockCells[i] = mock(Cell.class);
        Object[] values = new Object[COLUMNS_COUNT];
        for (int i = 0; i < COLUMNS_COUNT; i++) {
            if (i == 8) values[i] = Calendar.getInstance().getTime();
            else values[i] = RandomStringUtils.randomAscii(20);
        }

        for (int i = 0; i < COLUMNS_COUNT; i++) {
            Mockito.when(mockRow.getCell(Mockito.eq(i), Mockito.any(Row.MissingCellPolicy.class))).thenReturn(mockCells[i]);
            if (i != 8) {
                when(mockCells[i].getStringCellValue()).thenReturn((String) values[i]);
            }
        }
        Date time = Calendar.getInstance().getTime();
        when(mockCells[8].getDateCellValue()).thenReturn(time);
        values[8] = time;
        //when
        List<Row> data = Collections.singletonList(mockRow);
        val models = new SAPInput().readRows(data.iterator());
        //then
        assertThat(models).isNotEmpty();
        int i = 0;
//        for (int i = 0; i < COLUMNS_COUNT; i++) {
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
    }
//}