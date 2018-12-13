package gft.mentoring;


import gft.mentoring.matcher.ModelMatcher;
import gft.mentoring.sap.model.ConverterSAP;
import gft.mentoring.sap.model.ExcelException;
import gft.mentoring.trs.model.ConvertTRS;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class will launch program
 * we will use static nested classes to achieve fluent API in
 *
 * @see Main
 */

class MainRunner {

    private LocalDateTime currentDate;
    private String directory;

    MainRunner(DevManConfig conf) {
        directory = conf.getPath();
        currentDate = conf.getNow();
    }

    DataMerger loadResources() throws ExcelException {
        try (Stream<Path> filesInPath = Files.list(Paths.get(directory))) {
            return new DataMerger(currentDate, filesInPath.filter(path -> path.toString().endsWith(".xlsx"))
                    .filter(path -> StringUtils.contains(path.toString(), "SAP") ||
                            StringUtils.contains(path.toString(), "employees-basic-report"))
                    .map(Path::toString)
                    .collect(Collectors.toList()));
        } catch (IOException e) {
            throw new ExcelException("File not found or inaccessible", e.getCause());
//            throw new ExcelException("Files are missing or you have more than 2 files in same folder", e.getCause());
        }
    }

    class DataMerger {

        private final LocalDate currentDate;
        private final List<String> filenames;

        DataMerger(LocalDateTime currentDate, List<String> filenames) {
            this.currentDate = LocalDate.from(currentDate);
            this.filenames = filenames;
        }

        DataSaver mergeDataFromSystems() throws ExcelException, InvalidFormatException {

            val sapMentoringModels =
                    new ConverterSAP(currentDate).convertInputToSAPMentoringModel(getSAPfileName(filenames));

            val trsMentoringModels =
                    new ConvertTRS(currentDate).convertInputToTRSMentoringModel(getTRSfileName(filenames));

            val mentoringModels = new ModelMatcher().
                    createMentoringModelsFromMatchingGFTPeople(sapMentoringModels, trsMentoringModels);

            return new DataSaver(mentoringModels);
        }

        class DataSaver {

            private final List<MentoringModel> mentoringModels;

            DataSaver(List<MentoringModel> mentoringModels) {
                this.mentoringModels = mentoringModels;
            }

            void saveProposalsToFile() throws ExcelException {

                val mentees = mentoringModels
                        .stream()
                        .filter(MentoringModel::isMentee)
                        .collect(Collectors.toList());


                val matchingEngine = new MatchingEngine();
                List<String> devmanAssignmentsInfo = new ArrayList<>();
                for (MentoringModel mentee : mentees) {
                    devmanAssignmentsInfo.add(createDevmanInformationLines(mentee, matchingEngine.findProposals(mentee,
                            mentoringModels.toArray(new MentoringModel[0]))));
                }

                try {
                    Files.write(Paths.get(directory, "devman-proposals-" +
                            MainRunner.this.currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm"))
                            + ".txt"), devmanAssignmentsInfo);
                } catch (IOException e) {
                    throw new ExcelException("Could not write to txt file ", e.getCause());
                }
            }

            String createDevmanInformationLines(MentoringModel mentee, Stream<MentoringModel> candidates) {
                val menteeLine = formatMentee(mentee);
                val index = new AtomicInteger(1);
                val mentors = candidates.map(mentoringModel -> formatMentor(mentoringModel, index.getAndIncrement()))
                        .collect(Collectors.joining(" \n"));

                return menteeLine + "\n" + mentors + "\n";
            }

            @NotNull
            private String formatMentor(MentoringModel mentor, int candidateOrder) {
                return "we propose as number " + candidateOrder + " following candidate: "
                        + mentor.getFirstName() + " " + mentor.getLastName()
                        + "  LVL:" + mentor.getLevel() + " location:" + mentor.getLocalization()
                        + " Family:" + mentor.getFamily() + " Mentees: " + mentor.getMenteesAssigned() +
                        " spec:" + mentor.getSpecialization();
            }

            @NotNull
            private String formatMentee(MentoringModel mentee) {
                return "For mentee *" + mentee.getFirstName() + "* " + "*" + mentee.getLastName() + "*" +
                        " from Family:" + mentee.getFamily() + " location: " + mentee.getLocalization()
                        + " spec: " + mentee.getSpecialization();
            }
        }

        private String getSAPfileName(List<String> fileNames) {
            for (String file : fileNames) {
                if (file.contains("SAP")) {
                    return file;
                }
            }
            return "";//TO DO return SAP SAMPLE GOLDEN FILE
        }

        private String getTRSfileName(List<String> fileNames) {
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

