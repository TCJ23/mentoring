package gft.mentoring;

import gft.mentoring.sap.model.ExcelException;
import lombok.Value;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.time.LocalDate;

public class Main {

    private static final String DIRECTORY_TO_RUN_APP = ".";

    public static void main(String[] args) throws ExcelException, InvalidFormatException {
        new MainRunner(new DevManConfig(LocalDate.now(), DIRECTORY_TO_RUN_APP))
                .loadResources()
                .mergeDataFromSystems()
                .saveProposalsToFile();
    }
}

@Value
class DevManConfig {
    private LocalDate now;
    private final String path;
}
