package gft.mentoring.matcher;

import gft.mentoring.MentoringModel;
import gft.mentoring.sap.model.SAPMentoringModel;
import gft.mentoring.trs.model.TRSMentoringModel;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This is main class to combine information from both systems, we are interested in finding 1:1 match between SAP & TRS
 * Intermediate Models. We would also like to store information for 0 and n+1 matching models.
 */


class ModelMatcher {

    private List<TRSMentoringModel> findMatching(SAPMentoringModel sap, List<TRSMentoringModel> trsList) {
        return trsList.stream().filter(trs -> matches(trs, sap)).collect(Collectors.toList());
    }

    private boolean matches(TRSMentoringModel treserMM, SAPMentoringModel saperMM) {
        // check first name and last name
        return saperMM.getFirstName().trim().equalsIgnoreCase(treserMM.getFirstName().trim())
                && saperMM.getLastName().trim().equalsIgnoreCase(treserMM.getLastName().trim());
    }

    private Map<SAPMentoringModel, List<TRSMentoringModel>> matchIntermediateModels(List<SAPMentoringModel> sapMentoringModels,
                                                                                    List<TRSMentoringModel> trsMentoringModels) {

        Map<SAPMentoringModel, List<TRSMentoringModel>> unifiedModels = new LinkedHashMap<>();

        sapMentoringModels.forEach(sapModel -> {
            List<TRSMentoringModel> matchedModelsInTRS = findMatching(sapModel, trsMentoringModels);
            unifiedModels.put(sapModel, matchedModelsInTRS);
        });

        return unifiedModels;
    }

    List<MentoringModel> createMatches(List<SAPMentoringModel> sapMentoringModels,
                                       List<TRSMentoringModel> trsMentoringModels) {
        Map<SAPMentoringModel, List<TRSMentoringModel>> result = matchIntermediateModels(sapMentoringModels, trsMentoringModels);

        List<MentoringModel> models = new ArrayList<>();
        for (Map.Entry<SAPMentoringModel, List<TRSMentoringModel>> entry : result.entrySet()) {
            if (entry.getValue().isEmpty()) {
                System.out.println("No match for " + entry.getKey());
            } else if (entry.getValue().size() == 1) {
                models.add(MentoringBuilder.combine(entry.getKey(), entry.getValue().get(0)));
            } else {
                System.out.println("Multiple match for " + entry.getKey());
            }
        }

        return models;
    }

    private static class MentoringBuilder {

        static MentoringModel combine(@NotNull SAPMentoringModel priorityModel, @NotNull TRSMentoringModel secondaryModel) {
            return MentoringModel.builder().
                    firstName(priorityModel.getFirstName())
                    .lastName(priorityModel.getLastName())
                    .family(priorityModel.getFamily())
                    .specialization(secondaryModel.getSpecialization())
                    .level(priorityModel.getLevel())
                    .seniority(priorityModel.getSeniority())
                    .localization(priorityModel.getOfficeLocation())
                    .contractor(priorityModel.isContractor())
                    .leaver(secondaryModel.isLeaver())
                    .menteesAssigned(priorityModel.getMenteesAssigned())
                    .age(priorityModel.getAge())
                    .build();
        }

        static MentoringModel returnSAPMentoringModel(@NotNull SAPMentoringModel priorityModel, TRSMentoringModel trsMentoringModel) {
            return MentoringModel.builder().
                    firstName(priorityModel.getFirstName())
                    .lastName(priorityModel.getLastName())
                    .family(priorityModel.getFamily())
                    .specialization("")
                    .level(priorityModel.getLevel())
                    .seniority(priorityModel.getSeniority())
                    .localization(priorityModel.getOfficeLocation())
                    .contractor(priorityModel.isContractor())
                    .leaver(false)
                    .menteesAssigned(priorityModel.getMenteesAssigned())
                    .age(priorityModel.getAge())
                    .build();
        }
    }
}
