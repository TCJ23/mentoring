package gft.mentoring;

import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("7 - This class will run E2E tests for this application")
class MainRunnerTest {
    private static final LocalDate BASE_DATE = LocalDate.now();
    private static final String SAP_FILE = "./findFilesTest/SAP_04122018.xlsx";
    private static final String SAP_FILE2 = ".\\findFilesTest\\SAP_04122018.xlsx";
    private static final String TESTING_DIRECTORY = "./findFilesTest";

    @Test
    @DisplayName("7.1- validate that loading resources finds proper files in testing folder")
    void shouldFindSAPandTRSfilesInTestingFolder() {
        //given & when
        val files = new MainRunner(TESTING_DIRECTORY).loadResources();
        Assertions.assertThat(files).containsExactlyInAnyOrder(".\\findFilesTest\\employees-basic-report.xlsx",
                ".\\findFilesTest\\SAP_04122018.xlsx");
    }
}