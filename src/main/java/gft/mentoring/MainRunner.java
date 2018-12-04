package gft.mentoring;


import gft.mentoring.sap.model.ConverterSAP;
import gft.mentoring.sap.model.ExcelException;
import gft.mentoring.sap.model.SAPMentoringModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainRunner {
    private static List<Path> pliki = new ArrayList<>();
    private static final LocalDate BASE_DATE = LocalDate.now();
    private static final String SAP_FILE = "./findFilesTest/SAP_04122018.xlsx";
    private static final String SAP_FILE2 = ".\\findFilesTest\\SAP_04122018.xlsx";

    public static void main(String[] args) throws ExcelException, InvalidFormatException {

        try (Stream<Path> filesInPath = Files.list(Paths.get("./findFilesTest"))) {
            List<String> fileNames =
            filesInPath.filter(path -> path.toString().endsWith(".xlsx"))
                    .filter(path -> StringUtils.contains(path.toString(), "SAP") ||
                            StringUtils.contains(path.toString(), "employees-basic-report"))
                    .map(Path::toString)
                    .collect(Collectors.toList());

            ConverterSAP converterSAP = new ConverterSAP(BASE_DATE);


            List<SAPMentoringModel> sapMentoringModels =
                    converterSAP.convertInputToSAPMentoringModel(getSAPfileName(fileNames));


            System.out.println(sapMentoringModels);
            sapMentoringModels.forEach(sapMentoringModel -> sapMentoringModel.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getSAPfileName(List<String> fileNames) {
        for (String file : fileNames) {
            if (file.contains("SAP")) {
                System.out.println("nazwa pliku metoda SAP " + file);
                return file;
            }
            System.out.println("nie znalazłem pliku");
        }
        return "";
    }

}
