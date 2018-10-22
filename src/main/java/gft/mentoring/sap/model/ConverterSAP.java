package gft.mentoring.sap.model;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class ConverterSAP {

    List<SAPMentoringModel> convertInputToSAPMentoringModel(String file) throws ExcelException, InvalidFormatException {
        SAPInput input = new SAPInput();
        List<SAPmodel> sapers = input.readExcelSAPfile(file);
        return getSapMentoringModels(sapers);
    }

    List<SAPMentoringModel> convertFromRows(Iterator<Row> data) {
        SAPInput input = new SAPInput();
        List<SAPmodel> sapers = input.readRows(data);
        return getSapMentoringModels(sapers);
    }

    private List<SAPMentoringModel> getSapMentoringModels(List<SAPmodel> sapers) {
        List<SAPMentoringModel> sapMMs = new ArrayList<>();
        for (SAPmodel saper : sapers) {
            sapMMs.add(new SAPMentoringModelBuilder()
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
                    .build());
        }
        return sapMMs;
    }
}
