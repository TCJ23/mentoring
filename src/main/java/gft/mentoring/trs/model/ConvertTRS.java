package gft.mentoring.trs.model;

import gft.mentoring.sap.model.ExcelException;
import lombok.val;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ConvertTRS {

    private LocalDate baseDate;

    ConvertTRS(LocalDate baseDate) {
        this.baseDate = baseDate;
    }

    public List<TRSMentoringModel> convertInputToTRSMentoringModel(String file) throws ExcelException, InvalidFormatException {
        val input = new TRSInputReader();
        val tresers = input.readExcelTRSfile(file);
        val filteredTresers = input.filterInvalid(tresers);
        return createTRSMentoringModel(filteredTresers);
    }

    List<TRSMentoringModel> convertFilteredRowsTRS(Iterator<Row> data) {
        TRSInputReader input = new TRSInputReader();
        List<String> headers = input.getHeaders(data.next());
        List<TRSModel> tresers = input.readRowsTRS(headers, data);
        List<TRSModel> filteredTresers = input.filterInvalid(tresers);
        return createTRSMentoringModel(filteredTresers);
    }

    List<TRSMentoringModel> createTRSMentoringModel(List<TRSModel> tresers) {
        return tresers.stream().map(treser -> new TRSMentoringModelBuilder(baseDate)
                .setLeaver(treser.getStatus())
                .setLevel(treser.getGrade())
                .setFamily(treser.getJobFamily())
                .setSpecialization(treser.getTechnology())
                .setSeniority(treser.getStartDate())
                .setLocalization(treser.getOfficeLocation())
                .setContractor(treser.getContractType())
                .setFirstName(treser.getName())
                .setLastName(treser.getSurname())
                .build()).collect(Collectors.toList());
    }
}
