package gft.mentoring.sap.model;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@DisplayName("3 - Main class to test conversion from SAP file to Mentoring Model")
class ConverterSAPTest {
    private static final String SAP_FILE = "./Sample_SAP_DevMan_20180821.xlsx";

    @Test
    @DisplayName("3.2.1a - Verify SAP Model Job info to Ment.Model Level conversion")
    void shouldConvertLevelToInt() throws ExcelException, InvalidFormatException {
        //given
        SAPMentoringModel sapMMs = new ConverterSAP().convertInputToSAPMentoringModel(SAP_FILE).get(0);
        //when
        int level3 = sapMMs.getLevel();
        //then
        assertEquals(3, level3);
    }

    @Test
    @DisplayName("3.2.1b - Verify SAP Model Job info for Directors converts to Level 7 or higher")
    void shouldConvertDirectorLevelToInt() throws ExcelException, InvalidFormatException {
        //given
        SAPMentoringModel director = new ConverterSAP().convertInputToSAPMentoringModel(SAP_FILE).get(8);
        //when
        int directorLevel = director.getLevel();
        //then
        assertEquals(8, directorLevel);
    }

    @Test
    @DisplayName("3.2.2a - Verify SAP Model Employee Subgroup column defines if person is a contractor")
    void shouldConvertEmployeeSubgroupColumnToIsContractorTrue() throws ExcelException, InvalidFormatException {
        //given
        SAPMentoringModel model = new ConverterSAP().convertInputToSAPMentoringModel(SAP_FILE).get(1);
        //when
        boolean contractor = model.isContractor();
        //then
        assertThat(contractor).isTrue();
    }

    @Test
    @DisplayName("3.2.2b - Verify SAP Model Employee Subgroup column defines if person is an employee")
    void shouldConvertEmployeeSubgroupColumnToIsContractorFalse() throws ExcelException, InvalidFormatException {
        //given
        SAPMentoringModel model = new ConverterSAP().convertInputToSAPMentoringModel(SAP_FILE).get(0);
        //when
        boolean employee = model.isContractor();
        //then
        assertThat(employee).isFalse();
    }
}