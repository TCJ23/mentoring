package gft.mentoring.sap.model;

import lombok.val;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

class ConverterSAP {

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

    List<SAPMentoringModel> getSapMentoringModels(List<SAPmodel> sapers) {
        return sapers.stream().map(saper -> new SAPMentoringModelBuilder()
                /** meaningful logic
                 * @see SAPMentoringModel*/
                .setLevel(saper.getJob())
                .setContractor(saper.getEmployeeSubGrp())
                .setFamily(saper.getJobFamily())
                /** redundant fields
                 * @see SAPMentoringModel*/
                .setFirstName(saper.getFirstName())
                .setLastName(saper.getLastName())
                .setFederationID(saper.getInitials())
                .setSapID(saper.getPersonalNR())
                .setSpecialization(saper.getCostCenter())
                .setSeniority(saper.getInitEntry())
                .setLineManagerID(saper.getPersNrSuperior())
                .setMenteeID(saper.getPersNrMentor())
                .build()).collect(Collectors.toList());
    }
}
