package gft.mentoring.matcher;

import gft.mentoring.sap.model.ConverterSAP;
import gft.mentoring.sap.model.ExcelException;
import gft.mentoring.sap.model.SAPMentoringModel;
import gft.mentoring.trs.model.ConvertTRS;
import gft.mentoring.trs.model.TRSMentoringModel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class ModelMatcher {
    private static final LocalDate BASE_DATE = LocalDate.now();

    List<UnifiedModel> matchIntermediateModels(String sapFile, String trsFile, LocalDate date)
            throws ExcelException, InvalidFormatException {

        ConvertTRS convertTRS = new ConvertTRS(date);
        List<TRSMentoringModel> trsMentoringModels = convertTRS.convertInputToTRSMentoringModel(trsFile);

        ConverterSAP converterSAP = new ConverterSAP(date);
        List<SAPMentoringModel> sapMentoringModels = converterSAP.convertInputToSAPMentoringModel(sapFile);

        List<UnifiedModel> unifiedModels = new ArrayList<>();

        sapMentoringModels.forEach(sapModel -> {
            List<TRSMentoringModel> matching = findMatching(sapModel, trsMentoringModels);
            if (matching.isEmpty()) {
                storeUnmatched(sapModel);
            } else if (matching.size() == 1) {
                Map<String, List<SAPMentoringModel>> menteesAssigned = mentorsOccurence(sapMentoringModels);
                unifiedModels.add(combine(sapModel, matching.get(0), menteesAssigned.containsKey(sapModel.getSapID())
                        ? menteesAssigned.get(sapModel.getSapID()).size() : 0));
            } else {
                storeMultipleMatchOccurrences(sapModel);
            }
        });
        return unifiedModels;
    }

    Map<String, List<SAPMentoringModel>> mentorsOccurence(List<SAPMentoringModel> sapMentoringModels) {
        return sapMentoringModels.stream().collect
                (Collectors.groupingBy(SAPMentoringModel::getMentorID));
    }


    private UnifiedModel combine(SAPMentoringModel priorityModel, TRSMentoringModel secondaryModel, int menteesAssigned) {
        // match 1:1
        // build UnifiedModel
        return new UnifiedModelBuilder(BASE_DATE)
                .setFirstName(priorityModel.getFirstName())
                .setLastName(priorityModel.getLastName())
                .setFamily(priorityModel.getFamily())
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


    private void storeUnmatched(SAPMentoringModel unmatched) {
        // do something with unmatched entity
        // or check job family and grade and location
    }

    private void storeMultipleMatchOccurrences(SAPMentoringModel unmatched) {
        // do something with entity wchich is matched multiple times
    }

    private List<TRSMentoringModel> findMatching(SAPMentoringModel sap, List<TRSMentoringModel> trsList) {
        return trsList.stream().filter(trs -> matches(trs, sap)).collect(Collectors.toList());
    }

    private boolean matches(TRSMentoringModel treserMM, SAPMentoringModel saperMM) {
        // check first name and last name
        return saperMM.getFirstName().trim().equalsIgnoreCase(treserMM.getFirstName().trim())
                && saperMM.getLastName().trim().equalsIgnoreCase(treserMM.getLastName().trim());
    }

   /* private static Map<SAPMentoringModel,List<TRSMentoringModel>> match(Stream<SAPMentoringModel>, Stream<TRSMentoringModel>) {
        //0,1,więcej modeli

        return null;
    }*/
}
