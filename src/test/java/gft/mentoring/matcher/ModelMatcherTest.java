package gft.mentoring.matcher;

import gft.mentoring.sap.model.ExcelException;
import lombok.val;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

@DisplayName("6 - Main class to test Matcher Component")
class ModelMatcherTest {
    private static final LocalDate BASE_DATE = LocalDate.now();
    private static final String TRS_2_MENTEES_ASSIGNED = "TRS_2MenteesAssigned.xlsx";
    private static final String SAP_2_MENTEES_ASSIGNED = "SAP_2MenteesAssigned.xlsx";

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
}