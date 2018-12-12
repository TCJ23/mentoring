package gft.mentoring;

import gft.mentoring.sap.model.ExcelException;
import lombok.Value;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {

    private static final String DIRECTORY_TO_RUN_APP = ".";
    private static final LocalDateTime NOW = LocalDateTime.now();

    public static void main(String[] args) throws ExcelException, InvalidFormatException {
        new MainRunner(new DevManConfig(NOW, DIRECTORY_TO_RUN_APP))
//        new MainRunner(new DevManConfig(LocalDate.parse("11-12-2018", DateTimeFormatter.ofPattern("dd-MM-yyyy")), DIRECTORY_TO_RUN_APP))
                .loadResources()
                .mergeDataFromSystems()
                .saveProposalsToFile();
    }
}

@Value
class DevManConfig {
    private LocalDateTime now;
    private final String path;
}
