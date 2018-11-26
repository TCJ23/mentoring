/*
package gft.mentoring.matcher;

import gft.mentoring.sap.model.ExcelException;
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


    @Test
    @Disabled
    @DisplayName("6.1.1 - validate that Mentees Assigned is properly converted from SAP file")
    void shouldFind2MenteesAssignedToMentorFromSAPfile() throws ExcelException, InvalidFormatException {
        //given

        val unifiedModels = new ModelMatcher().matchIntermediateModels(SAP_2_MENTEES_ASSIGNED, TRS_2_MENTEES_ASSIGNED,
                BASE_DATE);
        //when
        val firstModelFromFile = (UnifiedModel) unifiedModels.get(0);
        val unifiedModelHas2MenteesAssigned = firstModelFromFile.getMenteesAssigned();

        //then
        Assertions.assertEquals(2, unifiedModelHas2MenteesAssigned);
    }

    @Test
    @Disabled
    @DisplayName("6.1.2 - check if age for Unified Model is converted properly")
    void shouldConvertModelWith35YearsOfAgeFromSAPsampleFile() throws ExcelException, InvalidFormatException {
        //given
        val unifiedModels = new ModelMatcher().matchIntermediateModels(SAP_AGE_EXAMPLES, TRS_RANDOM,
                BASE_DATE);

        //when
        Assertions.assertTrue(unifiedModels.get(0) instanceof UnifiedModel);
        UnifiedModel model = (UnifiedModel) unifiedModels.get(0);
        val age = model.getAge();
        val excelDate = LocalDate.of(1983, 9, 23);
        val years35 = ChronoUnit.YEARS.between(excelDate, BASE_DATE);

        //then
        Assertions.assertEquals(years35, age);
    }

    @Test
    @Disabled
    @DisplayName("6.1.3 - check if seniority for Unified Model is converted properly")
    void shouldConvertModelWithOneYearOfSeniority() throws ExcelException, InvalidFormatException {
        //given
        val unifiedModels = new ModelMatcher().matchIntermediateModels(SAP_SENIORITY_EXAMPLE, TRS_RANDOM,
                BASE_DATE);
        Assertions.assertTrue(unifiedModels.get(0) instanceof UnifiedModel);
        val model = (UnifiedModel) unifiedModels.get(0);

        //when
        val dateInExcel = LocalDate.of(2017, 11, 22);
        val seniority = model.getSeniority();
        val daysSince = ChronoUnit.DAYS.between(dateInExcel, BASE_DATE);
        //then
        Assertions.assertEquals(daysSince, seniority);
    }

    @Test
    @DisplayName("6.1.4a - Notice Period in TRS marks as leaver")
    void shouldDetectLeaverFromTRSFileNoticePeriodStatus() throws ExcelException, InvalidFormatException {
        //given & when
        val unifiedModels = new ModelMatcher().matchIntermediateModels(SAP_RANDOM, TRS_LEAVER_EXAMPLES,
                BASE_DATE);

        //when
        Assertions.assertTrue(unifiedModels.get(0) instanceof UnifiedModel);
        val model = (UnifiedModel) unifiedModels.get(0);
        val leaver = model.isLeaver();

        //then
        Assertions.assertTrue(leaver);
    }

    @Test
    @DisplayName("6.1.4b - missing info in TRS status Column")
    void shouldIgnoreMissingLeaverInfoFromTRSFile() throws ExcelException, InvalidFormatException {
        //given
        val unifiedModels = new ModelMatcher().matchIntermediateModels(SAP_RANDOM, TRS_LEAVER_EXAMPLES,
                BASE_DATE);
        //when
        Assertions.assertTrue(unifiedModels.get(1) instanceof UnifiedModel);
        val unifiedModel = (UnifiedModel) unifiedModels.get(1);
        val nonLeaver = unifiedModel.isLeaver();
        //then
        Assertions.assertFalse(nonLeaver);
    }

    @Test
    @DisplayName("6.1.4c - Hired in TRS marks as leaver")
    void shouldDetectLeaverFromTRSFileHiredStatus() throws ExcelException, InvalidFormatException {
        //given & when
        List<SegregationModel> unifiedModels = new ModelMatcher().matchIntermediateModels(SAP_RANDOM, TRS_LEAVER_EXAMPLES,
                BASE_DATE);
        //when
        if (unifiedModels.get(2) instanceof UnifiedModel) {
            UnifiedModel model = (UnifiedModel) unifiedModels.get(2);
            //then
            val leaver = model.isLeaver();
            Assertions.assertTrue(leaver);
        }
    }

    @Test
    @DisplayName("6.1.4d - Employee in TRS DOES NOT mark as leaver")
    void shouldIgnoreWorkingEmployeesFromTRSFile() throws ExcelException, InvalidFormatException {
        //given
        val unifiedModels = new ModelMatcher().matchIntermediateModels(SAP_RANDOM, TRS_LEAVER_EXAMPLES,
                BASE_DATE);
        //when
        Assertions.assertTrue(unifiedModels.get(3) instanceof UnifiedModel);
        //then
        val model = (UnifiedModel) unifiedModels.get(3);
        val nonLeaver = model.isLeaver();
        Assertions.assertFalse(nonLeaver);
    }

    @Test
    @DisplayName("6.2.1 - Make Sure Matcher continues to work with abandoned entry")
    void shouldFind3UnifiedModelPlus1ZeroMatchModelContinueToWorkEvenWithDuplicateMatchingEntries()
            throws ExcelException, InvalidFormatException {
        //given & when
        val unifiedModels = new ModelMatcher().matchIntermediateModels(SAP_DUPLICATE, TRS_DUPLICATE,
                BASE_DATE);
        //then
        Assertions.assertTrue(unifiedModels.get(0) instanceof UnifiedModel);
        Assertions.assertTrue(unifiedModels.get(1) instanceof UnifiedModel);
        Assertions.assertTrue(unifiedModels.get(2) instanceof UnifiedModel);
//        Assertions.assertTrue(unifiedModels.get(3) instanceof ZeroMatchModel);
//        assertThat(unifiedModels).size().isEqualTo(4);
    }

    @Test
    @Disabled
    @DisplayName("6.2.2 - Make Sure Matcher continues to work with duplicate entries")
    void shouldFind2UnifiedModelPlus1MultiMatchModelContinueToWorkEvenWithDuplicateMatchingEntries()
            throws ExcelException, InvalidFormatException {
        //given & when
        val unifiedModels = new ModelMatcher().matchIntermediateModels(SAP_DUPLICATE, TRS_DUPLICATE,
                BASE_DATE);
        //then
        assertThat(unifiedModels).size().isEqualTo(3);
    }
}
*/
