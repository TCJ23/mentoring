package gft.mentoring.matcher;

import gft.mentoring.sap.model.ExcelException;
import lombok.val;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

    @Test
    @DisplayName("6.1.1 - validate that Mentees Assigned is properly converted from SAP file")
    void shouldFind2MenteesAssignedToMentorFromSAPfile() throws ExcelException, InvalidFormatException {
        //given & when
        val unifiedModels = new ModelMatcher().matchIntermediateModels(SAP_2_MENTEES_ASSIGNED, TRS_2_MENTEES_ASSIGNED,
                BASE_DATE);
        val firstModelFromFile = unifiedModels.get(0);
        val unifiedModelHas2MenteesAssigned = firstModelFromFile.getMenteesAssigned();
        //then
        Assertions.assertEquals(2, unifiedModelHas2MenteesAssigned);
    }

    @Test
    @DisplayName("6.1.2 - check if age for Unified Model is converted properly")
    void shouldConvertModelWith35YearsOfAgeFromSAPsampleFile() throws ExcelException, InvalidFormatException {
        //given & when
        val unifiedModels = new ModelMatcher().matchIntermediateModels(SAP_AGE_EXAMPLES, TRS_RANDOM,
                BASE_DATE);
        val age = unifiedModels.get(0).getAge();
        val excelDate = LocalDate.of(1983, 9, 23);
        val years35 = ChronoUnit.YEARS.between(excelDate, BASE_DATE);
        //then
        Assertions.assertEquals(years35, age);
    }

    @Test
    @DisplayName("6.1.3 - check if age for Unified Model is converted properly")
    void shouldConvertModelWithOneYearOfSeniority() throws ExcelException, InvalidFormatException {
        //given & when
        val unifiedModels = new ModelMatcher().matchIntermediateModels(SAP_SENIORITY_EXAMPLE, TRS_RANDOM,
                BASE_DATE);
        val dateToCompareWith = LocalDate.of(2018, 11, 22);
        val dateInExcel = LocalDate.of(2017, 11, 22);
        val seniority = unifiedModels.get(0).getSeniority();
        val days365 = ChronoUnit.DAYS.between(dateInExcel, dateToCompareWith);
        //then
        Assertions.assertEquals(days365, seniority);
    }

    @Test
    @DisplayName("6.1.4a - Notice Period in TRS marks as leaver")
    void shouldDetectLeaverFromTRSFileNoticePeriodStatus() throws ExcelException, InvalidFormatException {
        //given & when
        val unifiedModels = new ModelMatcher().matchIntermediateModels(SAP_RANDOM, TRS_LEAVER_EXAMPLES,
                BASE_DATE);
        val leaver = unifiedModels.get(0).isLeaver();
        //then
        Assertions.assertTrue(leaver);
    }

    @Test
    @DisplayName("6.1.4b - missing info in TRS status Column")
    void shouldIgnoreMissingLeaverInfoFromTRSFile() throws ExcelException, InvalidFormatException {
        //given & when
        val unifiedModels = new ModelMatcher().matchIntermediateModels(SAP_RANDOM, TRS_LEAVER_EXAMPLES,
                BASE_DATE);
        val nonLeaver = unifiedModels.get(1).isLeaver();
        //then
        Assertions.assertFalse(nonLeaver);
    }

    @Test
    @DisplayName("6.1.4c - Hired in TRS marks as leaver")
    void shouldDetectLeaverFromTRSFileHiredStatus() throws ExcelException, InvalidFormatException {
        //given & when
        val unifiedModels = new ModelMatcher().matchIntermediateModels(SAP_RANDOM, TRS_LEAVER_EXAMPLES,
                BASE_DATE);
        val leaver = unifiedModels.get(2).isLeaver();
        //then
        Assertions.assertTrue(leaver);
    }

    @Test
    @DisplayName("6.1.4d - Employee in TRS DOES NOT mark as leaver")
    void should() throws ExcelException, InvalidFormatException {
        //given & when
        val unifiedModels = new ModelMatcher().matchIntermediateModels(SAP_RANDOM, TRS_LEAVER_EXAMPLES,
                BASE_DATE);
        val leaver = unifiedModels.get(2).isLeaver();
        //then
        Assertions.assertTrue(leaver);
    }
}