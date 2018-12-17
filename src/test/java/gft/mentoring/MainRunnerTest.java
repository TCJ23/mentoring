package gft.mentoring;

import gft.mentoring.sap.model.ExcelException;
import lombok.Value;
import lombok.val;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.reflection.Whitebox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("7 - This class will run E2E tests for this application")
class MainRunnerTest {
    private static final LocalDateTime BASE_DATE = LocalDateTime.now();
    private static final String TESTING_DIRECTORY = "./findFilesTest";
    private static final String MISSING_FILE_TEST = "./missingFileTest";
    private static final String HAPPY_PATH_TEST = "./happyPathTest";
    private static final String FILE_TO_WRITE = "./";
    private static final String IGNORE_MENTOR_FROM_FIRST_ITERATION = "./ignoringMentorFromFirstIteration";
    private static final String CORRECT_FILE = "ignoringMentorFromFirstIteration_expected/correct file.txt";
    private static final String NO_SUCH_DIRECTORY = "./trololololololo";

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
    @DisplayName("7.2a - validate that missing files will throw custom ExcelException ")
    void shouldThrowExcelExceptionWhenFilesAreMissingInTestingFolder() {
        //given & when
        //then
        Throwable exception = assertThrows(ExcelException.class, () -> new MainRunner(
                new DevManConfig(BASE_DATE, MISSING_FILE_TEST))
                .loadResources()
                .mergeDataFromSystems());
        assertThat(exception.getMessage())
                .isEqualToIgnoringCase("Files are missing or you have more than 2 files in same folder");
        System.out.println(exception.getMessage());
    }

    @Test
    @DisplayName("7.2b - validate that missing files will throw custom ExcelException ")
    void shouldThrowExcelExceptionWhenDirectoryIsCorrupted() {
        //given & when
        //then
        Throwable exception = assertThrows(ExcelException.class, () -> new MainRunner(
                new DevManConfig(BASE_DATE, NO_SUCH_DIRECTORY))
                .loadResources()
                .mergeDataFromSystems());
        assertThat(exception.getMessage())
                .isEqualToIgnoringCase("Directory in which we launch devman is corrupted");
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
        val pathStream = Files.walk(Paths.get(HAPPY_PATH_TEST))
                .filter(path -> path.toString().endsWith(".txt"));

        assertThat(Files.exists(Paths.get(HAPPY_PATH_TEST))).isTrue();
        assertThat(pathStream.findFirst().isPresent()).toString().startsWith("devman-proposals-");

        val devman = Files.list(Paths.get(HAPPY_PATH_TEST))
                .filter(path -> path.getFileName().toString().matches("devman-proposals-\\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}\\.txt"))
                .sorted().findFirst().orElseThrow(FileNotFoundException::new);
        val devmanfile = devman.toFile();
        Files.delete(devmanfile.toPath());

    }

    @Test
    @DisplayName("7.4 - Once Best Mentor is proposed to Mentee, second Mentee should have proposed Next Best Mentor in line")
    void shouldAssignBestMentorToFirstMenteeThenNextBestMentorToSecondMentee()
            throws ExcelException, InvalidFormatException, IOException {
        //given & when
        new MainRunner(new DevManConfig(BASE_DATE, IGNORE_MENTOR_FROM_FIRST_ITERATION))
                .loadResources()
                .mergeDataFromSystems()
                .saveProposalsToFile();
        //then


        val devman = Files.list(Paths.get(IGNORE_MENTOR_FROM_FIRST_ITERATION))
                .filter(path -> path.getFileName().toString().matches("devman-proposals-\\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}\\.txt"))
                .sorted().findFirst().orElseThrow(FileNotFoundException::new);
        val devmanfile = devman.toFile();

        assertThat(devmanfile).hasSameContentAs(new File(CORRECT_FILE));

        Files.delete(devmanfile.toPath());
    }
}