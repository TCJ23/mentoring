package gft.mentoring.sap.model;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Main class to test conversion from SAP file to Mentoring Model")
class ConverterSAPTest {
    private static final String SAP_FILE = "./Sample_SAP_DevMan_20180821.xlsx";

    @Test
    @DisplayName("3.2.1 - Verify SAP Model Job info to Ment.Model Level conversion")
    void shouldConvertLevelToInt() throws ExcelException, InvalidFormatException {
        //given
        SAPMentoringModel sapMMs = new ConverterSAP().convertInputToSAPMentoringModel(SAP_FILE).get(0);
        //when
        int level3 = sapMMs.getLevel();
        //then
        Assertions.assertEquals(3, level3);
    }

    @Test
    @DisplayName("3.2.2 - Verify SAP Model Job info for Directors converts to Level 7 or higher")
    void shouldConvertDirectorLevelToInt() throws ExcelException, InvalidFormatException {
        //given
        SAPMentoringModel director = new ConverterSAP().convertInputToSAPMentoringModel(SAP_FILE).get(8);
        //when
        int directorLevel = director.getLevel();
        //then
        Assertions.assertEquals(8, directorLevel);
    }
}