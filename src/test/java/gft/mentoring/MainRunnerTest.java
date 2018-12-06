package gft.mentoring;

import gft.mentoring.sap.model.ExcelException;
import lombok.val;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("7 - This class will run E2E tests for this application")
class MainRunnerTest {
    private static final LocalDate BASE_DATE = LocalDate.now();
    private static final String SAP_FILE = "./findFilesTest/SAP_04122018.xlsx";
    private static final String SAP_FILE2 = ".\\findFilesTest\\SAP_04122018.xlsx";
    private static final String TESTING_DIRECTORY = "./findFilesTest";
    private static final String MISSING_FILE_TEST = "./missingFileTest";

    @Test
    @DisplayName("7.1- validate that loading resources finds proper files in testing folder")
    void shouldFindSAPandTRSfilesInTestingFolder() {
        //given & when
        val files = new MainRunner(TESTING_DIRECTORY).loadResources();
        //then
        assertThat(files).containsExactlyInAnyOrder(".\\findFilesTest\\employees-basic-report.xlsx",
                ".\\findFilesTest\\SAP_04122018.xlsx");
    }

    @Test
    @DisplayName("7.2- validate that missing files will throw custom ExcelException ")
    void shouldThrowExcelExceptionWhenFilesAreMissingInTestingFolder() {
        //given & when
        //then
        Throwable exception = assertThrows(ExcelException.class, () -> new MainRunner(MISSING_FILE_TEST).mergeDataFromSystems());
        assertThat(exception.getMessage()).isEqualToIgnoringCase("File not found or inaccessible");
        System.out.println(exception.getMessage());
    }
}