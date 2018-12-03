package gft.mentoring.matcher;

import gft.mentoring.MentoringModel;
import gft.mentoring.sap.model.SAPMentoringModel;
import gft.mentoring.trs.model.TRSMentoringModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This is main class to combine information from both systems, we are interested in finding 1:1 match between SAP & TRS
 * Intermediate Models. We would also like to store information for 0 and n+1 matching models.
 */


class ModelMatcher {
    Map<SAPMentoringModel, List<TRSMentoringModel>> matchIntermediateModels(List<SAPMentoringModel> sapMentoringModels,
                                                                            List<TRSMentoringModel> trsMentoringModels)
    {
        Map<SAPMentoringModel, List<TRSMentoringModel>> unifiedModels = new HashMap<>();

        sapMentoringModels.forEach(sapModel -> {
            List<TRSMentoringModel> matchedModelsInTRS = findMatching(sapModel, trsMentoringModels);

            if (matchedModelsInTRS.isEmpty()) {
                ZeroMatchModel zeroMatchModel = storeUnmatched(sapModel);
                unifiedModels.put(sapModel, matchedModelsInTRS);

            } else if (matchedModelsInTRS.size() == 1) {
                MentoringModel mentoringModel = MentoringBuilder.combine(sapModel, trsMentoringModels.get(0));
//                unifiedModels.put(mentoringModel);
            } else {
                storeMultipleMatchOccurrences(sapModel);
            }
            unifiedModels.put(sapModel, matchedModelsInTRS);
        });
        return unifiedModels;
    }




    private static class MentoringBuilder {

        static MentoringModel combine(SAPMentoringModel priorityModel, TRSMentoringModel secondaryModel) {
            return MentoringModel.builder().
                    firstName(priorityModel.getFirstName())
                    .lastName(priorityModel.getLastName())
                    .family(priorityModel.getFamily())
                    .specialization(secondaryModel.getSpecialization())
                    .level(priorityModel.getLevel())
                    .seniority(priorityModel.getSeniority())
                    .localization(priorityModel.getOfficeLocation())
                    .contractor(priorityModel.isContractor())
                    .leaver(secondaryModel.isContractor())
                    .menteesAssigned(priorityModel.getMenteesAssigned())
                    .age(priorityModel.getAge())
                    .build();
        }
    }

    private ZeroMatchModel storeUnmatched(SAPMentoringModel saperMM) {
        // do something with unmatched entity
        // or check job family and grade and location
        return new ZeroMatchModel(
                saperMM.getFirstName(),
                saperMM.getLastName(),
                saperMM.getFamily(),
                saperMM.getSpecialization(),
                saperMM.getLevel());
    }


    private List<MultiMatchModel> storeMultipleMatchOccurrences(SAPMentoringModel multiMatch) {
        // do something with entity wchich is matched multiple times
        List<SAPMentoringModel> multimatches = new ArrayList<>();
        multimatches.add(multiMatch);
        return null;
    }

    private List<TRSMentoringModel> findMatching(SAPMentoringModel sap, List<TRSMentoringModel> trsList) {
        return trsList.stream().filter(trs -> matches(trs, sap)).collect(Collectors.toList());
    }

    private boolean matches(TRSMentoringModel treserMM, SAPMentoringModel saperMM) {
        // check first name and last name
        return saperMM.getFirstName().trim().equalsIgnoreCase(treserMM.getFirstName().trim())
                && saperMM.getLastName().trim().equalsIgnoreCase(treserMM.getLastName().trim());
    }
}
