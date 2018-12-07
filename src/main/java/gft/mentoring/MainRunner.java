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
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class will lanuch program
 */

class MainRunner {

    private final LocalDate currentDate;
    private final String directory;
    private List<String> filenames;

    MainRunner(DevManConfig conf) {
        this.directory = conf.getPath();
        this.currentDate = conf.getNow();
    }

    public DataMerger loadResources() throws ExcelException {
        try (Stream<Path> filesInPath = Files.list(Paths.get(this.directory))) {
            return new DataMerger(currentDate, filesInPath.filter(path -> path.toString().endsWith(".xlsx"))
                    .filter(path -> StringUtils.contains(path.toString(), "SAP") ||
                            StringUtils.contains(path.toString(), "employees-basic-report"))
                    .map(Path::toString)
                    .collect(Collectors.toList()));
        } catch (IOException e) {
            throw new ExcelException("missing files", e.getCause());
        }
    }

    static class DataMerger {

        private final LocalDate currentDate;
        private final List<String> filenames;

        DataMerger(LocalDate currentDate, List<String> filenames) {
            this.currentDate = currentDate;
            this.filenames = filenames;
        }

        public List<MentoringModel> mergeDataFromSystems() throws ExcelException, InvalidFormatException {

            List<SAPMentoringModel> sapMentoringModels =
                    new ConverterSAP(currentDate).convertInputToSAPMentoringModel(getSAPfileName(filenames));

            List<TRSMentoringModel> trsMentoringModels =
                    new ConvertTRS(currentDate).convertInputToTRSMentoringModel(getTRSfileName(filenames));

            return new ModelMatcher().createMentoringModelsFromMatchingGFTPeople(sapMentoringModels, trsMentoringModels);
        }
    }

    private static String getSAPfileName(List<String> fileNames) {
        for (String file : fileNames) {
            if (file.contains("SAP")) {
                return file;
            }
        }
        return "";//TO DO return SAP SAMPLE GOLDEN FILE
    }

    private static String getTRSfileName(List<String> fileNames) {
        for (String file : fileNames) {
            if (file.contains("employees")) {
                return file;
            }
        }
        return "";//TO DO return TRS SAMPLE GOLDEN FILE
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
       /* ConverterSAP converterSAP = new ConverterSAP(BASE_DATE);
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
        System.out.println(proposals.count());

    } catch(
    IOException e)

    {
        e.printStackTrace();
    }

}
*/
