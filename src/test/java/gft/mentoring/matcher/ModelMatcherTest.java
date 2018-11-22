package gft.mentoring.matcher;

import gft.mentoring.sap.model.ConverterSAP;
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
        val unifiedModels = new ModelMatcher().matchIntermediateModels(SAP_AGE_EXAMPLES, TRS_2_MENTEES_ASSIGNED,
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
        val unifiedModels = new ModelMatcher().matchIntermediateModels(SAP_SENIORITY_EXAMPLE, TRS_2_MENTEES_ASSIGNED,
                BASE_DATE);
        val dateToCompareWith = LocalDate.of(2018, 11, 22);
        val dateInExcel = LocalDate.of(2017, 11, 22);
        val seniority = unifiedModels.get(0).getSeniority();
        val days365 = ChronoUnit.DAYS.between(dateInExcel, dateToCompareWith);
        //then
        Assertions.assertEquals(days365, seniority);
    }
}