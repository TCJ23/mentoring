package gft.mentoring;

import gft.mentoring.sap.model.ExcelException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) throws ExcelException, InvalidFormatException {
        MainRunner mainRunner = new MainRunner(Configuration.PATH);
        mainRunner.loadResources();
        mainRunner.mergeDataFromSystems();
    }
    static class Configuration {
        private static final LocalDate BASE_DATE = LocalDate.now();
        private static final String PATH = ".";
    }
}
