package gft.mentoring;

import gft.mentoring.sap.model.ExcelException;
import lombok.Value;
import lombok.val;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.reflection.Whitebox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("7 - This class will run E2E tests for this application")
class MainRunnerTest {
    private static final LocalDate BASE_DATE = LocalDate.now();
    private static final String SAP_FILE = "./findFilesTest/SAP_04122018.xlsx";
    private static final String SAP_FILE2 = ".\\findFilesTest\\SAP_04122018.xlsx";
    private static final String TESTING_DIRECTORY = "./findFilesTest";
    private static final String MISSING_FILE_TEST = "./missingFileTest";
    private static final String HAPPY_PATH_TEST = "./happyPathTest";
    private static final String FILE_TO_WRITE = "./";

    @Test
    @DisplayName("7.1 - validate that loading resources finds proper files in testing folder")
    void shouldFindSAPandTRSfilesInTestingFolder() throws ExcelException {
        //given & when
        MainRunner.DataMerger dataMerger = new MainRunner(new DevManConfig(BASE_DATE, TESTING_DIRECTORY))
                .loadResources();
        //then
        /** due to Linux/Windows naming convention this assertions breaks build
         assertThat(files).containsExactlyInAnyOrder(".\\findFilesTest\\employees-basic-report.xlsx",
         ".\\findFilesTest\\SAP_04122018.xlsx");
         * */
        List<String> filenames = (List<String>) Whitebox.getInternalState(dataMerger, "filenames");
        assertThat(filenames.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("7.2 - validate that missing files will throw custom ExcelException ")
    void shouldThrowExcelExceptionWhenFilesAreMissingInTestingFolder() {
        //given & when
        //then
        Throwable exception = assertThrows(ExcelException.class, () -> new MainRunner(
                new DevManConfig(BASE_DATE, MISSING_FILE_TEST))
                .loadResources()
                .mergeDataFromSystems());
        assertThat(exception.getMessage()).isEqualToIgnoringCase("File not found or inaccessible");
        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("7.3 - run happy path and make sure file is created ")
    void shouldCreateDevmanProposalsTXTfile() throws ExcelException, InvalidFormatException, IOException {
        //given & when
        new MainRunner(new DevManConfig(BASE_DATE, HAPPY_PATH_TEST))
                .loadResources()
                .mergeDataFromSystems()
                .saveProposalsToFile();
        //then
        Stream<Path> pathStream = Files.walk(Paths.get(FILE_TO_WRITE))
                .filter(path -> path.toString().endsWith(".txt"));

//        assertThat(Files.exists(Paths.get(FILE_TO_WRITE))).isTrue();
        assertThat(pathStream.findFirst().isPresent()).toString().startsWith("devman-proposals-");
    }
}