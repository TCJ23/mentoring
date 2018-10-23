package gft.mentoring.sap.model;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

class ConverterSAP {

    List<SAPMentoringModel> convertInputToSAPMentoringModel(String file) throws ExcelException, InvalidFormatException {
        SAPInput input = new SAPInput();
        List<SAPmodel> sapers = input.readExcelSAPfile(file);
        return getSapMentoringModels(sapers);
    }

    List<SAPMentoringModel> convertFromRows(Iterator<Row> data) {
        SAPInput input = new SAPInput();
//        List<String> headers = input.getHeaders(data.next());
//        List<SAPmodel> sapers = input.readRowsSAP(headers, data);
        List<SAPmodel> sapers = input.readRowsSAP(data);
        return getSapMentoringModels(sapers);
    }

    private List<SAPMentoringModel> getSapMentoringModels(List<SAPmodel> sapers) {
        return sapers.stream().map(saper -> new SAPMentoringModelBuilder()
                /** meaningful logic
                 * @see SAPMentoringModel*/
                .setlevel(saper.getJob())
                .setcontractor(saper.getEmployeeSubGrp())
                .setfamily(saper.getPosition())
                /** redundant fields
                 * @see SAPMentoringModel*/
                .setfirstName(saper.getFirstName())
                .setlastName(saper.getLastName())
                .setfederationID(saper.getInitials())
                .setsapID(saper.getPersonalNR())
                .setspecialization(saper.getCostCenter())
                .setseniority(saper.getInitEntry())
                .setlineManagerID(saper.getPersNrSuperior())
                .setmenteeID(saper.getPersNrMentor())
                .build()).collect(Collectors.toList());
    }
}
