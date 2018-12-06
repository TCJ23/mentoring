package gft.mentoring.matcher;

import gft.mentoring.MentoringModel;
import gft.mentoring.sap.model.ConverterSAP;
import gft.mentoring.sap.model.ExcelException;
import gft.mentoring.trs.model.ConvertTRS;
import lombok.val;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


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
    private static final String SAP_MULTI_AND_ZERO_MATCHES = "SAP_duplicate_with_zero_match_entires.xlsx";
    private static final String TRS_MULTI_AND_ZERO_MATCHES = "TRS_duplicate_with_zero_match_entires.xlsx";
    private static final String SAP_ONE_TO_ONE = "SAP_OneToOneMatch_solid_example.xlsx";
    private static final String TRS_ONE_TO_ONE = "TRS_OneToOneMatch_solid_example.xlsx";
    private static final String SAP_3_PEOPLE_TO_MATCH = "SAP_3people_toMatch.xlsx";
    private static final String TRS_3_PEOPLE_TO_MATCH = "TRS_3people_toMatch.xlsx";
    private static final String SAP_IS_MENTEE_EXAMPLE = "SAP_marking_mentees_from_MentorIDColumn.xlsx";
    private static final String TRS_MARKING_MENTEES = "TRS_marking_mentees_from_MentorIDColumn.xlsx";


    @Test
    @DisplayName("6.1.1 - validate that Matcher creates MentoringModels in 1:1 match")
    void shouldCreate3MentoringModelsFromSampleFiles() throws ExcelException, InvalidFormatException {
        //given
        val sapMentoringModels = new ConverterSAP(BASE_DATE)
                .convertInputToSAPMentoringModel(SAP_3_PEOPLE_TO_MATCH);
        val trsMentoringModels = new ConvertTRS(BASE_DATE)
                .convertInputToTRSMentoringModel(TRS_3_PEOPLE_TO_MATCH);
        //when
        val mentoringModels = new ModelMatcher()
                .createMentoringModelsFromMatchingGFTPeople(sapMentoringModels, trsMentoringModels);
        //then
        assertThat(mentoringModels).size().isEqualTo(3);
    }

    @Test
    @DisplayName("6.1.2 - check if age for Mentoring Model is converted properly")
    void shouldConvertModelWith35YearsOfAgeFromSAPsampleFile() throws ExcelException, InvalidFormatException {
        //given
        val sapMentoringModels = new ConverterSAP(BASE_DATE)
                .convertInputToSAPMentoringModel(SAP_AGE_EXAMPLES);
        val trsMentoringModels = new ConvertTRS(BASE_DATE)
                .convertInputToTRSMentoringModel(TRS_RANDOM);
        //when
        val mentoringModels = new ModelMatcher().createMentoringModelsFromMatchingGFTPeople(sapMentoringModels, trsMentoringModels);
        val mentoringModel = mentoringModels.get(0);
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
        val sapMentoringModels = new ConverterSAP(BASE_DATE)
                .convertInputToSAPMentoringModel(SAP_SENIORITY_EXAMPLE);
        val trsMentoringModels = new ConvertTRS(BASE_DATE)
                .convertInputToTRSMentoringModel(TRS_RANDOM);
        //when
        val mentoringModels = new ModelMatcher().createMentoringModelsFromMatchingGFTPeople(sapMentoringModels, trsMentoringModels);
        val mentoringModel = mentoringModels.get(0);

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
        val sapMentoringModels = new ConverterSAP(BASE_DATE)
                .convertInputToSAPMentoringModel(SAP_RANDOM);
        val trsMentoringModels = new ConvertTRS(BASE_DATE)
                .convertInputToTRSMentoringModel(TRS_LEAVER_EXAMPLES);
        //when
        val mentoringModels = new ModelMatcher().createMentoringModelsFromMatchingGFTPeople(sapMentoringModels, trsMentoringModels);
        val mentoringModel = mentoringModels.get(0);
        //when
        val leaver = mentoringModel.isLeaver();
        //then
        Assertions.assertTrue(leaver);
    }

    @Test
    @DisplayName("6.1.4b - missing info in TRS status Column doesn't mark Mentoring Model as leaver")
    void shouldIgnoreMissingLeaverInfoFromTRSFile() throws ExcelException, InvalidFormatException {
        //given
        val sapMentoringModels = new ConverterSAP(BASE_DATE)
                .convertInputToSAPMentoringModel(SAP_RANDOM);
        val trsMentoringModels = new ConvertTRS(BASE_DATE)
                .convertInputToTRSMentoringModel(TRS_LEAVER_EXAMPLES);
        //when
        val mentoringModels = new ModelMatcher().createMentoringModelsFromMatchingGFTPeople(sapMentoringModels, trsMentoringModels);
        val mentoringModel = mentoringModels.get(1);
        val nonLeaver = mentoringModel.isLeaver();
        //then
        Assertions.assertFalse(nonLeaver);
    }

    @Test
    @DisplayName("6.1.4c - Hired status in TRS marks as leaver")
    void shouldDetectLeaverFromTRSFileHiredStatus() throws ExcelException, InvalidFormatException {
        //given
        val sapMentoringModels = new ConverterSAP(BASE_DATE)
                .convertInputToSAPMentoringModel(SAP_RANDOM);
        val trsMentoringModels = new ConvertTRS(BASE_DATE)
                .convertInputToTRSMentoringModel(TRS_LEAVER_EXAMPLES);
        //when
        val mentoringModels = new ModelMatcher().createMentoringModelsFromMatchingGFTPeople(sapMentoringModels, trsMentoringModels);
        val mentoringModel = mentoringModels.get(2);
        //then
        val leaver = mentoringModel.isLeaver();
        Assertions.assertTrue(leaver);
    }

    @Test
    @DisplayName("6.1.4d - Employee in TRS DOES NOT mark as leaver")
    void shouldIgnoreWorkingEmployeesFromTRSFile() throws ExcelException, InvalidFormatException {
        //given
        val sapMentoringModels = new ConverterSAP(BASE_DATE)
                .convertInputToSAPMentoringModel(SAP_RANDOM);
        val trsMentoringModels = new ConvertTRS(BASE_DATE)
                .convertInputToTRSMentoringModel(TRS_LEAVER_EXAMPLES);
        //when
        val mentoringModels = new ModelMatcher().createMentoringModelsFromMatchingGFTPeople(sapMentoringModels, trsMentoringModels);
        val mentoringModel = mentoringModels.get(3);
        val nonLeaver = mentoringModel.isLeaver();
        //then
        Assertions.assertFalse(nonLeaver);
    }

    @Test
    @DisplayName("6.2.1 - validate that Matcher ignores duplicates ")
    void shouldIgnoreDuplicateEntriesAndCreate2MentoringModels()
            throws ExcelException, InvalidFormatException {
        //given
        val sapMentoringModels = new ConverterSAP(BASE_DATE)
                .convertInputToSAPMentoringModel(SAP_MULTI_AND_ZERO_MATCHES);
        val trsMentoringModels = new ConvertTRS(BASE_DATE)
                .convertInputToTRSMentoringModel(TRS_MULTI_AND_ZERO_MATCHES);
        //when
        val mentoringModels = new ModelMatcher()
                .createMentoringModelsFromMatchingGFTPeople(sapMentoringModels, trsMentoringModels);
        //then
        assertThat(mentoringModels).size().isEqualTo(2);
    }

    @Test
    @DisplayName("6.2.2 - validate that Matcher ignores multi match entries")
    void shouldIgnoreZeroMatchEntriesAndCreate2MentoringModels()
            throws ExcelException, InvalidFormatException {
        //given
        val sapMentoringModels = new ConverterSAP(BASE_DATE)
                .convertInputToSAPMentoringModel(SAP_MULTI_AND_ZERO_MATCHES);
        val trsMentoringModels = new ConvertTRS(BASE_DATE)
                .convertInputToTRSMentoringModel(TRS_MULTI_AND_ZERO_MATCHES);
        //when
        val mentoringModels = new ModelMatcher()
                .createMentoringModelsFromMatchingGFTPeople(sapMentoringModels, trsMentoringModels);
        //then
        assertThat(mentoringModels).size().isEqualTo(2);
    }

    @Test
    @DisplayName("6.2.3- create mentees when Pers.no. Mentor Column has 0 value")
    void shouldCreateMenteesWhenMentorColumnHas0value() throws ExcelException, InvalidFormatException {
        //given
        val sapMentoringModels = new ConverterSAP(BASE_DATE)
                .convertInputToSAPMentoringModel(SAP_IS_MENTEE_EXAMPLE);
        val trsMentoringModels = new ConvertTRS(BASE_DATE)
                .convertInputToTRSMentoringModel(TRS_MARKING_MENTEES);
        //when
        val mentoringModels = new ModelMatcher()
                .createMentoringModelsFromMatchingGFTPeople(sapMentoringModels, trsMentoringModels);
        MentoringModel mentee1 = mentoringModels.get(0);
        MentoringModel mentee2 = mentoringModels.get(1);
        MentoringModel mentor = mentoringModels.get(2);
        //then
        assertAll(
                () -> assertTrue(mentee1.isMentee()),
                () -> assertTrue(mentee2.isMentee()),
                () -> assertFalse(mentor.isMentee())
        );
    }
}
