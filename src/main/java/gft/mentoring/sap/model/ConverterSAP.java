package gft.mentoring.sap.model;

import lombok.val;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class ConverterSAP {

    private LocalDate baseDate;

    ConverterSAP(LocalDate baseDate) {
        this.baseDate = baseDate;
    }

    List<SAPMentoringModel> convertInputToSAPMentoringModel(String file) throws ExcelException, InvalidFormatException {
        val input = new SAPInputReader();
        val sapers = input.readExcelSAPfile(file);
        val filteredSapers = input.filterInvalid(sapers);
        return getSapMentoringModels(filteredSapers);
    }

    List<SAPMentoringModel> convertFilteredRowsSAP(Iterator<Row> data) {
        val input = new SAPInputReader();
        val headers = input.getHeaders(data.next());
        val sapers = input.readRowsSAP(headers, data);
        val filteredSapers = input.filterInvalid(sapers);
        return getSapMentoringModels(filteredSapers);
    }

    private Map<String, List<SAPModel>> findSAPidInMentorColumn(List<SAPModel> sapModels) {
        return sapModels.stream().collect(Collectors.groupingBy(SAPModel::getPersNrMentor));
    }

    private int countMenteesAssignedToMentor(SAPModel saper, List<SAPModel> sapModels) {
        Map<String, List<SAPModel>> menteesAssigned = findSAPidInMentorColumn(sapModels);
        return menteesAssigned.containsKey(saper.getPersonalNR())
                ? menteesAssigned.get(saper.getPersonalNR()).size() : 0;
    }

    List<SAPMentoringModel> getSapMentoringModels(List<SAPModel> sapers) {
        return sapers.stream().map(saper -> new SAPMentoringModelBuilder(baseDate)
                /** meaningful logic
                 * @see SAPMentoringModel*/
                .setLevel(saper.getJob())
                .setContractor(saper.getEmployeeSubGrp())
                .setFamily(saper.getJobFamily())
                .setLastName(saper.getLastName())
                .setFirstName(saper.getFirstName())
                .setSeniority(saper.getInitEntry())
                .setAge(saper.getDateOfBirth())
                .setOfficeLocation(saper.getPersonnelSubarea())
                .setSapID(saper.getPersonalNR())
                .setMenteeID(saper.getPersNrMentor())
                .setMenteesAssigned(countMenteesAssignedToMentor(saper, sapers))
                /** redundant fields
                 * @see SAPMentoringModel*/
                .setFederationID(saper.getInitials())
                .setSpecialization(saper.getCostCenter())
                .setLineManagerID(saper.getPersNrSuperior())
                .build()).collect(Collectors.toList());
    }
}
