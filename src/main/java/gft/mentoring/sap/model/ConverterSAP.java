package gft.mentoring.sap.model;

import lombok.val;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ConverterSAP {

    private LocalDate baseDate;

    public ConverterSAP(LocalDate baseDate) {
        this.baseDate = baseDate;
    }

    public List<SAPMentoringModel> convertInputToSAPMentoringModel(String file) throws ExcelException, InvalidFormatException {
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
                /** redundant fields
                 * @see SAPMentoringModel*/
                .setFederationID(saper.getInitials())
                .setSapID(saper.getPersonalNR())
                .setSpecialization(saper.getCostCenter())
                .setLineManagerID(saper.getPersNrSuperior())
                .setMenteeID(saper.getPersNrMentor())
                .build()).collect(Collectors.toList());
    }
}
