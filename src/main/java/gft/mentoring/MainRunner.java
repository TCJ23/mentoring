package gft.mentoring;


import gft.mentoring.matcher.ModelMatcher;
import gft.mentoring.sap.model.ConverterSAP;
import gft.mentoring.sap.model.ExcelException;
import gft.mentoring.sap.model.SAPMentoringModel;
import gft.mentoring.trs.model.ConvertTRS;
import gft.mentoring.trs.model.TRSMentoringModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class will launch program
 */

class MainRunner {

    private final LocalDate currentDate;
    private final String directory;

    MainRunner(DevManConfig conf) {
        this.directory = conf.getPath();
        this.currentDate = conf.getNow();
    }

    DataMerger loadResources() throws ExcelException {
        try (Stream<Path> filesInPath = Files.list(Paths.get(this.directory))) {
            return new DataMerger(currentDate, filesInPath.filter(path -> path.toString().endsWith(".xlsx"))
                    .filter(path -> StringUtils.contains(path.toString(), "SAP") ||
                            StringUtils.contains(path.toString(), "employees-basic-report"))
                    .map(Path::toString)
                    .collect(Collectors.toList()));
        } catch (IOException e) {
            throw new ExcelException("File not found or inaccessible", e.getCause());
        }
    }

    static class DataMerger {

        private final LocalDate currentDate;
        private final List<String> filenames;

        DataMerger(LocalDate currentDate, List<String> filenames) {
            this.currentDate = currentDate;
            this.filenames = filenames;
        }

        DataSaver mergeDataFromSystems() throws ExcelException, InvalidFormatException {

            List<SAPMentoringModel> sapMentoringModels =
                    new ConverterSAP(currentDate).convertInputToSAPMentoringModel(getSAPfileName(filenames));

            List<TRSMentoringModel> trsMentoringModels =
                    new ConvertTRS(currentDate).convertInputToTRSMentoringModel(getTRSfileName(filenames));

            List<MentoringModel> mentoringModels = new ModelMatcher().
                    createMentoringModelsFromMatchingGFTPeople(sapMentoringModels, trsMentoringModels);

            return new DataSaver(mentoringModels);
        }

        static class DataSaver {

            private final List<MentoringModel> mentoringModels;

            DataSaver(List<MentoringModel> mentoringModels) {
                this.mentoringModels = mentoringModels;
            }

            void saveProposalsToFile() throws ExcelException {
                String timeStamp = new SimpleDateFormat("yyyyMMddHHmm'.txt'").format(new Date());

                List<MentoringModel> mentees = mentoringModels
                        .stream()
                        .filter(MentoringModel::isMentee)
                        .collect(Collectors.toList());

                MatchingEngine matchingEngine = new MatchingEngine();

                //Map<MentoringModel, Stream<MentoringModel>> devmanAssignments = new LinkedHashMap<>();
                List<String> devmanAssignmentsInfo = new ArrayList<>();
                for (MentoringModel mentee : mentees) {
                    devmanAssignmentsInfo.add(createDevmanInformationLines(mentee, matchingEngine.findProposals(mentee, mentoringModels.toArray(new MentoringModel[0]))));
                }
                /*mentees.stream()
                        .collect(Collectors.toMap(
                                Function.identity(),
                                mentoringModel -> matchingEngine.findProposals(mentoringModel, mentoringModels.toArray(new MentoringModel[0]))));*/
                try {
                    Files.write(Paths.get("./devman-proposals-" + timeStamp + ".txt"), devmanAssignmentsInfo);
                } catch (IOException e) {
                    throw new ExcelException("Could not write to txt file ", e.getCause());
                }
            }

            String createDevmanInformationLines(MentoringModel mentee, Stream<MentoringModel> candidates) {
                String menteeLine = formatMentee(mentee);
                List<String> mentors = candidates.map(mentoringModel -> menteeLine + formatMentor(mentoringModel)).collect(Collectors.toList());
                return menteeLine + ": " + StringUtils.join(mentors);
            }

            @NotNull
            private String formatMentor(MentoringModel mentor) {
                return " we propose following candidates " + mentor.getFirstName() + " " + mentor.getLastName()
                        + " of level " + mentor.getLevel() + " from " + mentor.getFamily() +
                        " family with specialization " + mentor.getSpecialization();
            }

            @NotNull
            private String formatMentee(MentoringModel mentee) {
                return "For menteee " + mentee.getFirstName() + " " + mentee.getLastName() +
                        " that works in " + mentee.getFamily() + " family with specialization "
                        + mentee.getSpecialization() + " ";
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

