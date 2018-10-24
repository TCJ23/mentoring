package gft.mentoring.trs.model;

import lombok.val;
import org.apache.poi.ss.usermodel.Row;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

class ConvertTRS {
    List<TRSMentoringModel> convertFromRows(Iterator<Row> data) {
        TRSInput input = new TRSInput();
        val headers = input.getHeaders(data.next());
        val tresers = input.readRowsTRS(headers, data);
        return getTRSMentoringModel(tresers);
    }

    private List<TRSMentoringModel> getTRSMentoringModel(List<TRSModel> tresers) {
        return tresers.stream().map(treser -> new TRSMentoringModelBuilder()
                .setleaver(treser.getStatus())
                .setlevel(treser.getGrade())
                .build()).collect(Collectors.toList());
    }
}
