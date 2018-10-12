package gft.mentoring.sap.model;

import lombok.val;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;



@DisplayName("Main class to test SAP INPUT")
class SAPInputTest {

    private static final String SAP_FILE = "./Sample_SAP_DevMan_20180821.xlsx";

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
    @DisplayName("3.1 - SAP file contains additional column - model will not change")
    void shouldCreate25SAPmodelsEvenWithAdditionalColumn() throws IOException, InvalidFormatException {
        //given
        SAPInput sapInput = new SAPInput();
        Workbook workbook = WorkbookFactory.create(new File(SAP_FILE));
        /* we decrease by 1 because of 1st row is composed of column names*/
        val columns = workbook.getSheetAt(0).getRow(0).getPhysicalNumberOfCells();
        //when
        val models = sapInput.readExcelSAPfile(SAP_FILE);
        val saperFields = models.get(0).getClass().getDeclaredFields().length;
        //then
        assertThat(saperFields < columns);
    }

    @Test
    @DisplayName("test for throwing exceptions FileNotFoundException, The process cannot access the file because it is being used by another process ")
    void exceptionTesting() {
        Throwable exception = assertThrows(FileNotFoundException.class, () -> {
            throw new IllegalArgumentException("The process cannot access the file because it is being used by another process");
        });
        assertEquals("The process cannot access the file because it is being used by another process", exception.getMessage());
    }


}