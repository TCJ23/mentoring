/*
package gft.mentoring.matcher;

import gft.mentoring.MentoringModel;
import gft.mentoring.sap.model.ConverterSAP;
import gft.mentoring.sap.model.ExcelException;
import gft.mentoring.sap.model.SAPMentoringModel;
import gft.mentoring.trs.model.ConvertTRS;
import gft.mentoring.trs.model.TRSMentoringModel;
import lombok.val;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

*
 * This is main class to combine information from both systems, we are interested in finding 1:1 match between SAP & TRS
 * Intermediate Models. We would also like to store information for 0 and n+1 matching models.


class ModelMatcher {
    Map<SAPMentoringModel, List<TRSMentoringModel>> matchIntermediateModels(List<SAPMentoringModel> sapMentoringModels,
                                                                            List<TRSMentoringModel> trsMentoringModels,
                                                                            LocalDate now)
            throws ExcelException, InvalidFormatException {

        Map<SAPMentoringModel, List<TRSMentoringModel>> unifiedModels = new HashMap<>();

        sapMentoringModels.forEach(sapModel -> {
            List<TRSMentoringModel> matching = findMatching(sapModel, trsMentoringModels);

            if (matching.isEmpty()) {
                ZeroMatchModel zeroMatchModel = storeUnmatched(sapModel);
                unifiedModels.add(zeroMatchModel);

            } else if (matching.size() == 1) {
                Map<String, List<SAPMentoringModel>> menteesAssigned = mentorsOccurence(sapMentoringModels);

                unifiedModels.add
                        (combine(sapModel, matching.get(0), menteesAssigned.containsKey(sapModel.getSapID())
                                ? menteesAssigned.get(sapModel.getSapID()).size() : 0));
            } else {
                storeMultipleMatchOccurrences(sapModel);
        }
            unifiedModels.put(sapModel, matching);
        });
        return unifiedModels;
    }

    public List<MentoringModel> match(Map<SAPMentoringModel, List<TRSMentoringModel>> mapa) {

        return null;
    }

    Map<String, List<SAPMentoringModel>> mentorsOccurence(List<SAPMentoringModel> sapMentoringModels) {
        return sapMentoringModels.stream().collect
                (Collectors.groupingBy(SAPMentoringModel::getMentorID));
    }


move to static inner class

    private UnifiedModel combine(SAPMentoringModel priorityModel, TRSMentoringModel secondaryModel, int menteesAssigned) {
        // match 1:1
        // build UnifiedModel
        return new UnifiedModelBuilder()
                .setFirstName(priorityModel.getFirstName())
                .setLastName(priorityModel.getLastName())
                .setFamily(priorityModel.getFamily())
                //
                .setSpecialization(priorityModel.getSpecialization())
                .setLevel(priorityModel.getLevel())
                .setSeniority(priorityModel.getSeniority())
                .setLocalization(priorityModel.getOfficeLocation())
                .setContractor(priorityModel.isContractor())
                .setLeaver(secondaryModel != null && secondaryModel.isLeaver())
                .setMenteesAssigned(menteesAssigned)
                .setAge(priorityModel.getAge())
                .build();
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
*/
