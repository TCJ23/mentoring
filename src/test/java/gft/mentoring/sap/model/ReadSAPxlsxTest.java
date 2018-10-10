package gft.mentoring.sap.model;

import lombok.val;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class ReadSAPxlsxTest {

    private static final String SAP_FILE = "./Sample_SAP_DevMan_20180821.xlsx";

    @Test
    @DisplayName("should create 25 SAP models from sample excel file")
    void shouldCreate25SAPmodels() throws IOException, InvalidFormatException {
        //given
        Workbook workbook = WorkbookFactory.create(new File(SAP_FILE));
        ReadSAPxlsx readSAPxlsx = new ReadSAPxlsx();
        /* we decrease by 1 because of 1st row is composed of column names*/
        val rowsSize = workbook.getSheetAt(0).getLastRowNum() - 1;
        //when
        val models = readSAPxlsx.readXLSX(SAP_FILE);
        assertThat(models).size().isEqualTo(rowsSize);
//        assertThat(models).size().isEqualTo(25);
    }
}