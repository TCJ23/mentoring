package gft.mentoring.matcher;

import gft.mentoring.MentoringModel;
import gft.mentoring.sap.model.SAPMentoringModel;
import gft.mentoring.trs.model.TRSMentoringModel;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author tzje
 * This is main class to combineSAPmmModelWithTRSmmModel information from both systems, we are interested in finding 1:1 match between SAP & TRS
 * Intermediate Models. We would also like to store information for 0 and n+1 matching models.
 */


class ModelMatcher {
    private static final Logger LOGGER = Logger.getLogger(ModelMatcher.class.getName());

    private List<TRSMentoringModel> findMatchingGFTPeople(SAPMentoringModel sap, @NotNull List<TRSMentoringModel> trsList) {
        return trsList.stream().filter(trs -> matchesFirstAndLastName(trs, sap)).collect(Collectors.toList());
    }

    /**
     * Matching between intermediate models should be based mainly on first and lastname
     * due to lack of common denominator
     */
    private boolean matchesFirstAndLastName(@NotNull TRSMentoringModel treserMM, @NotNull SAPMentoringModel saperMM) {
        // check first name and last name
        return saperMM.getFirstName().trim().equalsIgnoreCase(treserMM.getFirstName().trim())
                && saperMM.getLastName().trim().equalsIgnoreCase(treserMM.getLastName().trim());
    }

    private Map<SAPMentoringModel, List<TRSMentoringModel>> matchIntermediateModels(@NotNull List<SAPMentoringModel> sapMentoringModels,
                                                                                    List<TRSMentoringModel> trsMentoringModels) {

        Map<SAPMentoringModel, List<TRSMentoringModel>> unifiedModels = new LinkedHashMap<>();

        sapMentoringModels.forEach(sapModel -> {
            List<TRSMentoringModel> matchedModelsInTRS = findMatchingGFTPeople(sapModel, trsMentoringModels);
            unifiedModels.put(sapModel, matchedModelsInTRS);
        });

        return unifiedModels;
    }
    /** should be moved to seperate class or main */
    List<MentoringModel> createMentoringModelsFromMatchingGFTPeople(List<SAPMentoringModel> sapMentoringModels,
                                                                    List<TRSMentoringModel> trsMentoringModels) {

        Map<SAPMentoringModel, List<TRSMentoringModel>> matchedModels =
                matchIntermediateModels(sapMentoringModels, trsMentoringModels);

        List<MentoringModel> models = new ArrayList<>();
        for (Map.Entry<SAPMentoringModel, List<TRSMentoringModel>> entry : matchedModels.entrySet()) {

            if (entry.getValue().isEmpty()) {
                LOGGER.warning("No match for " + entry.getKey().getFirstName() + entry.getKey().getLastName());

            } else if (entry.getValue().size() == 1) {
                models.add(MentoringBuilder.combineSAPmmModelWithTRSmmModel(entry.getKey(), entry.getValue().get(0)));

            } else {
                LOGGER.warning("Multiple match for " + entry.getKey().getFirstName() + entry.getKey().getLastName());
            }
        }

        return models;
    }
    /** On 1:1 match between Intermediate models
     * @see SAPMentoringModel
     * @see TRSMentoringModel
     * we know how to combine them into
     * @see MentoringModel*/
    private static class MentoringBuilder {
     /** @param  priorityModel
      * takes precedence over
      * @param secondaryModel
      * in all but 2 cases for leaver and specialization*/

        static MentoringModel combineSAPmmModelWithTRSmmModel(@NotNull SAPMentoringModel priorityModel, @NotNull TRSMentoringModel secondaryModel) {
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
    }
}
