package gft.mentoring.sap.model;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.util.ArrayList;
import java.util.List;

class ConverterSAP {

    List<SAPMentoringModel> convertInputToSAPMentoringModel(String file) throws ExcelException, InvalidFormatException {
        SAPInput input = new SAPInput();
        List<SAPMentoringModel> sapMMs = new ArrayList<>();
        List<SAPmodel> sapers = input.readExcelSAPfile(file);
        for (SAPmodel saper : sapers) {
            sapMMs.add(new SAPMentoringModelBuilder()
                    .setlevel(saper.getJob())
                    .setcontractor(saper.getEmployeeSubGrp())
                    .setfamily(saper.getPosition())
                    .build());
            //.setfirstName(saper.getFirstName())
        }
        return sapMMs;
    }
}
