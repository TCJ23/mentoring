package gft.mentoring.trs.model;

import lombok.val;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TRSInputTest {
    private static final String TRS_FILE = "./Sample_TRS_DevMan_20181002.xlsx";

    @Test
    @DisplayName("4.1.1 - should create 31 TRS models from sample excel file")
    void shouldCreate31TRSmodelsFromSampleFile() throws IOException, InvalidFormatException {
        //given
        TRSInput trsInput = new TRSInput();
        Workbook workbook = WorkbookFactory.create(new File(TRS_FILE));
        //when
        val models = trsInput.readExcelTRSfile(TRS_FILE);
        //then
        assertThat(models).size().isEqualTo(31);
    }
}