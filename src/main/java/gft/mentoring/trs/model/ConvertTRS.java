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
        val tresers = input.readExcelTRSfile(file);
        val filteredSapers = input.filterInvalid(tresers);
        return getTRSMentoringModel(filteredSapers);
    }

    List<TRSMentoringModel> convertFromRows(Iterator<Row> data) {
        TRSInput input = new TRSInput();
        val headers = input.getHeaders(data.next());
        val tresers = input.readRowsTRS(headers, data);
        val filteredTresers = input.filterInvalid(tresers);
        return getTRSMentoringModel(filteredTresers);
    }

    List<TRSMentoringModel> getTRSMentoringModel(List<TRSModel> tresers) {
        return tresers.stream().map(treser -> {
            return new TRSMentoringModelBuilder()
                    .setLeaver(treser.getStatus())
                    .setLevel(treser.getGrade())
                    .setFamily(treser.getJobFamily())
                    .setSpecialization(treser.getTechnology())
                    .setSeniority(treser.getStartDate())
                    .setLocalization(treser.getOfficeLocation())
                    .setContractor(treser.getContractType())
                    .setFirstName(treser.getName())
                    .setLastName(treser.getSurname())
                    .build();
        }).collect(Collectors.toList());
    }
}
