package gft.mentoring.matcher;

import gft.mentoring.sap.model.ConverterSAP;
import gft.mentoring.sap.model.ExcelException;
import gft.mentoring.sap.model.SAPMentoringModel;
import gft.mentoring.trs.model.ConvertTRS;
import gft.mentoring.trs.model.TRSMentoringModel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModelMatcher {
    private ConverterSAP converterSAP;
    private ConvertTRS convertTRS;
    private UnifiedModel unifiedModel;

    public UnifiedModel matchIntermediateModels(String sapFile, String trsFile) throws ExcelException, InvalidFormatException {
        List<SAPMentoringModel> sapMentoringModels = converterSAP.convertInputToSAPMentoringModel(sapFile);
        Map<String, List<SAPMentoringModel>> mentors = sapMentoringModels.stream().collect
                (Collectors.groupingBy(SAPMentoringModel::getMentorID));

        List<TRSMentoringModel> trsMentoringModels = convertTRS.convertInputToTRSMentoringModel(trsFile);
        SAPMentoringModel priorityModel = sapMentoringModels.get(0);
        TRSMentoringModel secondaryModel = trsMentoringModels.get(0);

        return new UnifiedModel(priorityModel.getFirstName(), priorityModel.getLastName(),
                priorityModel.getFamily(), priorityModel.getSpecialization(), priorityModel.getLevel(),
                priorityModel.getSeniority(), priorityModel.getOfficeLocation(), priorityModel.isContractor(),
                secondaryModel.isLeaver(),
                mentors.get(priorityModel.getSapID()).size(),
                priorityModel.getAge());
    }
}
