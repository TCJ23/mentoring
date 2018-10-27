package gft.mentoring.trs.model;

import gft.mentoring.sap.model.ExcelException;
import lombok.val;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

class ConvertTRS {
    List<TRSMentoringModel> convertInputToTRSMentoringModel(String file) throws ExcelException, InvalidFormatException {
        TRSInput input = new TRSInput();
        List<TRSModel> sapers = input.readExcelTRSfile(file);
        return getTRSMentoringModel(sapers);
    }

    List<TRSMentoringModel> convertFromRows(Iterator<Row> data) {
        TRSInput input = new TRSInput();
        val headers = input.getHeaders(data.next());
        val tresers = input.readRowsTRS(headers, data);
        return getTRSMentoringModel(tresers);
    }

    List<TRSMentoringModel> getTRSMentoringModel(List<TRSModel> tresers) {
        return tresers.stream().map(treser -> new TRSMentoringModelBuilder()
                .setleaver(treser.getStatus())
                .setlevel(treser.getGrade())
                .setfamily(treser.getJobFamily())
                .setspecialization(treser.getTechnology())
                .setseniority(treser.getStartDate())
                .setlocalization(treser.getOfficeLocation())
                .setcontractor(treser.getContractType())
                .setfirstName(treser.getName())
                .setlastName(treser.getSurname())
                .build()).collect(Collectors.toList());
    }
}
