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
            SAPMentoringModel sapMM = new SAPMentoringModel();
            sapMM.setLevel(saper.getJob());
            sapMMs.add(sapMM);
        }
        return sapMMs;
    }
}
