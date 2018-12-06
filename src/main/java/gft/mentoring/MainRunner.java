package gft.mentoring;


import gft.mentoring.matcher.ModelMatcher;
import gft.mentoring.sap.model.ConverterSAP;
import gft.mentoring.sap.model.ExcelException;
import gft.mentoring.sap.model.SAPMentoringModel;
import gft.mentoring.trs.model.ConvertTRS;
import gft.mentoring.trs.model.TRSMentoringModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class will lanuch program
 */

public class MainRunner {
    private static final String TESTING_FOLDER = "./findFilesTest/SAP_04122018.xlsx";
    private static final String TESTING_FOLDER2 = ".\\findFilesTest\\SAP_04122018.xlsx";

    private static final LocalDate BASE_DATE = LocalDate.now();
    private static final String PATH = "./findFilesTest";


    public static void main(String[] args) throws ExcelException, InvalidFormatException {

        try (Stream<Path> filesInPath = Files.list(Paths.get(PATH))) {
            List<String> fileNames =
                    filesInPath.filter(path -> path.toString().endsWith(".xlsx"))
                            .filter(path -> StringUtils.contains(path.toString(), "SAP") ||
                                    StringUtils.contains(path.toString(), "employees-basic-report"))
                            .map(Path::toString)
                            .collect(Collectors.toList());

            ConverterSAP converterSAP = new ConverterSAP(BASE_DATE);
            List<SAPMentoringModel> sapMentoringModels =
                    converterSAP.convertInputToSAPMentoringModel(getSAPfileName(fileNames));

            ConvertTRS convertTRS = new ConvertTRS(BASE_DATE);
            List<TRSMentoringModel> trsMentoringModels =
                    convertTRS.convertInputToTRSMentoringModel(getTRSfileName(fileNames));

            sapMentoringModels.forEach(System.out::println);
            System.out.println();
            trsMentoringModels.forEach(System.out::println);
            System.out.println();

            ModelMatcher modelMatcher = new ModelMatcher();
            List<MentoringModel> mentoringModels =
                    modelMatcher.createMentoringModelsFromMatchingGFTPeople(sapMentoringModels, trsMentoringModels);

            mentoringModels.forEach(System.out::println);
            System.out.println();

            List<MentoringModel> mentees = seperateMenteesOnly(mentoringModels);
            List<MentoringModel> mentors = seperateMentorsOnly(mentoringModels);

            Stream<MentoringModel> proposals =
                    new MatchingEngine().findProposals(mentoringModels.iterator().next(), mentoringModels.iterator().next());

            System.out.println("PROPOZALE :)");
//            proposals.collect(Collectors.toList()).forEach(System.out::println);
            System.out.println(proposals.count());

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
            System.out.println("nie znalazłem innego pliku");
        }
        return "";//TO DO return SAP SAMPLE GOLDEN FILE
    }

    private static String getTRSfileName(List<String> fileNames) {
        for (String file : fileNames) {
            if (file.contains("employees")) {
                System.out.println("nazwa pliku metoda TRS " + file);
                return file;
            }
            System.out.println("nie znalazłem innego pliku ");
        }
        return "";//TO DO return TRS SAMPLE GOLDEN FILE
    }

    private static List<MentoringModel> seperateMenteesOnly(List<MentoringModel> peopleGFT) {
        return peopleGFT.stream()
                .filter(mentoringModel -> mentoringModel.getMenteesAssigned() == 0)
                .collect(Collectors.toList());
    }

    private static List<MentoringModel> seperateMentorsOnly(List<MentoringModel> peopleGFT) {
        return peopleGFT.stream()
                .filter(mentoringModel -> mentoringModel.getMenteesAssigned() > 0)
                .collect(Collectors.toList());
    }
}
  /*     new NewModelMatcher()
                .catchMatchedModels(matched -> {

                })
                .catchUnmatchedTrs(unmatchedTrs -> {

                })



    }*/
/*class NewModelMatcher {
    NewModelMatcher catchMatchedModels(Consumer<MentoringModel> handler) {
        return this;
    }

    NewModelMatcher catchUnmatchedTrs(Consumer<TRSMentoringModel> handler) {
        return this;
    }

    NewModelMatcher catchUnmatchedSap(Consumer<SAPMentoringModel> handler) {
        return this;
    }

}*/
