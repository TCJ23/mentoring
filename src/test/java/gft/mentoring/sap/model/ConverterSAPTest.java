package gft.mentoring.sap.model;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConverterSAPTest {
    private static final String SAP_FILE = "./Sample_SAP_DevMan_20180821.xlsx";

    @Test
    @DisplayName("Verify SAP Model Job info to Ment.Model Level conversion")
    void shouldConvertLevelToInt() throws ExcelException, InvalidFormatException {
        //given
        SAPMentoringModel sapMMs = new ConverterSAP().convertInputToSAPMentoringModel(SAP_FILE).get(0);
        //when
        int level3 = sapMMs.getLevel();
        //then
        System.out.println(level3);
        Assertions.assertEquals(3,level3);
    }
}