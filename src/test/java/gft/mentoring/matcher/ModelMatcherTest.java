package gft.mentoring.matcher;

import gft.mentoring.MentoringModel;
import gft.mentoring.sap.model.ConverterSAP;
import gft.mentoring.sap.model.ExcelException;
import gft.mentoring.sap.model.SAPInputReader;
import gft.mentoring.sap.model.SAPMentoringModel;
import gft.mentoring.trs.model.ConvertTRS;
import gft.mentoring.trs.model.TRSMentoringModel;
import lombok.val;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;


@DisplayName("6 - Main class to test Matcher Component")
class ModelMatcherTest {
    private static final LocalDate BASE_DATE = LocalDate.now();
    private static final String TRS_2_MENTEES_ASSIGNED = "TRS_2MenteesAssigned.xlsx";
    private static final String SAP_2_MENTEES_ASSIGNED = "SAP_2MenteesAssigned.xlsx";
    private static final String SAP_AGE_EXAMPLES = "SAP_age_conversion_check.xlsx";
    private static final String SAP_SENIORITY_EXAMPLE = "SAP_seniority_conversion_check.xlsx";
    private static final String TRS_LEAVER_EXAMPLES = "TRS_Leaver_Examples.xlsx";
    private static final String SAP_RANDOM = "SAP_random.xlsx";
    private static final String TRS_RANDOM = "TRS_random.xlsx";
    private static final String SAP_DUPLICATE = "SAP_duplicate_entires.xlsx";
    private static final String TRS_DUPLICATE = "TRS_duplicate_entires.xlsx";
    private static final String SAP_ONE_TO_ONE = "SAP_OneToOneMatch_solid_example.xlsx";
    private static final String TRS_ONE_TO_ONE = "TRS_OneToOneMatch_solid_example.xlsx";
    private static final String SAP_3_PEOPLE_TO_MATCH = "SAP_3people_toMatch.xlsx";
    private static final String TRS_3_PEOPLE_TO_MATCH = "TRS_3people_toMatch.xlsx";


    @Test
    @DisplayName("6.1.1 - validate that Matcher creates MentoringModels in 1:1 match")
    void shouldCreate3MentoringModelsFromSampleFiles() throws ExcelException, InvalidFormatException {
        //given
        List<SAPMentoringModel> sapMentoringModels = new ConverterSAP(BASE_DATE)
                .convertInputToSAPMentoringModel(SAP_3_PEOPLE_TO_MATCH);
        List<TRSMentoringModel> trsMentoringModels = new ConvertTRS(BASE_DATE)
                .convertInputToTRSMentoringModel(TRS_3_PEOPLE_TO_MATCH);
        //when
        List<MentoringModel> mentoringModels = new ModelMatcher()
                .createMatches(sapMentoringModels, trsMentoringModels);
        //then
        assertThat(mentoringModels).size().isEqualTo(3);
    }

    @Test
    @DisplayName("6.1.2 - check if age for Mentoring Model is converted properly")
    void shouldConvertModelWith35YearsOfAgeFromSAPsampleFile() throws ExcelException, InvalidFormatException {
        //given
        List<SAPMentoringModel> sapMentoringModels = new ConverterSAP(BASE_DATE)
                .convertInputToSAPMentoringModel(SAP_AGE_EXAMPLES);
        List<TRSMentoringModel> trsMentoringModels = new ConvertTRS(BASE_DATE)
                .convertInputToTRSMentoringModel(TRS_RANDOM);
        //when
        List<MentoringModel> mentoringModels = new ModelMatcher().createMatches(sapMentoringModels, trsMentoringModels);
        MentoringModel mentoringModel = mentoringModels.get(0);
        val age = mentoringModel.getAge();
        val excelDate = LocalDate.of(1983, 9, 23);
        val years35 = ChronoUnit.YEARS.between(excelDate, BASE_DATE);

        //then
        Assertions.assertEquals(years35, age);
    }

    @Test
    @DisplayName("6.1.3 - check if seniority for Mentoring Model is converted properly")
    void shouldConvertModelWithOneYearOfSeniority() throws ExcelException, InvalidFormatException {
        //given
        List<SAPMentoringModel> sapMentoringModels = new ConverterSAP(BASE_DATE)
                .convertInputToSAPMentoringModel(SAP_SENIORITY_EXAMPLE);
        List<TRSMentoringModel> trsMentoringModels = new ConvertTRS(BASE_DATE)
                .convertInputToTRSMentoringModel(TRS_RANDOM);
        //when
        List<MentoringModel> mentoringModels = new ModelMatcher().createMatches(sapMentoringModels, trsMentoringModels);
        MentoringModel mentoringModel = mentoringModels.get(0);
        val dateInExcel = LocalDate.of(2017, 11, 22);
        val seniority = mentoringModel.getSeniority();
        val daysSince = ChronoUnit.DAYS.between(dateInExcel, BASE_DATE);
        //then
        Assertions.assertEquals(daysSince, seniority);
    }

    @Test
    @DisplayName("6.1.4a - Notice Period in TRS marks as leaver")
    void shouldDetectLeaverFromTRSFileNoticePeriodStatus() throws ExcelException, InvalidFormatException {
        //given
        List<SAPMentoringModel> sapMentoringModels = new ConverterSAP(BASE_DATE)
                .convertInputToSAPMentoringModel(SAP_RANDOM);
        List<TRSMentoringModel> trsMentoringModels = new ConvertTRS(BASE_DATE)
                .convertInputToTRSMentoringModel(TRS_LEAVER_EXAMPLES);
        //when
        List<MentoringModel> mentoringModels = new ModelMatcher().createMatches(sapMentoringModels, trsMentoringModels);
        MentoringModel mentoringModel = mentoringModels.get(0);
        //when
        val leaver = mentoringModel.isLeaver();
        System.out.println(mentoringModel.toString());
        //then
        Assertions.assertTrue(leaver);
    }

    @Test
    @DisplayName("6.1.4b - missing info in TRS status Column doesn't mark Mentoring Model as leaver")
    void shouldIgnoreMissingLeaverInfoFromTRSFile() throws ExcelException, InvalidFormatException {
        //given
        List<SAPMentoringModel> sapMentoringModels = new ConverterSAP(BASE_DATE)
                .convertInputToSAPMentoringModel(SAP_RANDOM);
        List<TRSMentoringModel> trsMentoringModels = new ConvertTRS(BASE_DATE)
                .convertInputToTRSMentoringModel(TRS_LEAVER_EXAMPLES);
        //when
        List<MentoringModel> mentoringModels = new ModelMatcher().createMatches(sapMentoringModels, trsMentoringModels);
        MentoringModel mentoringModel = mentoringModels.get(1);
        val nonLeaver = mentoringModel.isLeaver();
        //then
        Assertions.assertFalse(nonLeaver);
    }

    @Test
    @DisplayName("6.1.4c - Hired in TRS marks as leaver")
    void shouldDetectLeaverFromTRSFileHiredStatus() throws ExcelException, InvalidFormatException {
        //given
        List<SAPMentoringModel> sapMentoringModels = new ConverterSAP(BASE_DATE)
                .convertInputToSAPMentoringModel(SAP_RANDOM);
        List<TRSMentoringModel> trsMentoringModels = new ConvertTRS(BASE_DATE)
                .convertInputToTRSMentoringModel(TRS_LEAVER_EXAMPLES);
        //when
        List<MentoringModel> mentoringModels = new ModelMatcher().createMatches(sapMentoringModels, trsMentoringModels);
        MentoringModel mentoringModel = mentoringModels.get(2);
        //then
        val leaver = mentoringModel.isLeaver();
        Assertions.assertTrue(leaver);
    }

    @Test
    @DisplayName("6.1.4d - Employee in TRS DOES NOT mark as leaver")
    void shouldIgnoreWorkingEmployeesFromTRSFile() throws ExcelException, InvalidFormatException {
        //given
        List<SAPMentoringModel> sapMentoringModels = new ConverterSAP(BASE_DATE)
                .convertInputToSAPMentoringModel(SAP_RANDOM);
        List<TRSMentoringModel> trsMentoringModels = new ConvertTRS(BASE_DATE)
                .convertInputToTRSMentoringModel(TRS_LEAVER_EXAMPLES);
        //when
        List<MentoringModel> mentoringModels = new ModelMatcher().createMatches(sapMentoringModels, trsMentoringModels);
        MentoringModel mentoringModel = mentoringModels.get(3);
        val nonLeaver = mentoringModel.isLeaver();
        //then
        Assertions.assertFalse(nonLeaver);
    }

    @Test
    @DisplayName("6.2.1 - validate that Matcher ignores duplicates ")
    void shouldIgnoreDuplicateEntriesAndCreate2MentoringModels()
            throws ExcelException, InvalidFormatException {
        //given
        List<SAPMentoringModel> sapMentoringModels = new ConverterSAP(BASE_DATE)
                .convertInputToSAPMentoringModel(SAP_DUPLICATE);
        List<TRSMentoringModel> trsMentoringModels = new ConvertTRS(BASE_DATE)
                .convertInputToTRSMentoringModel(TRS_DUPLICATE);
        //when
        List<MentoringModel> mentoringModels = new ModelMatcher()
                .createMatches(sapMentoringModels, trsMentoringModels);
        //then
        assertThat(mentoringModels).size().isEqualTo(2);
    }
}
