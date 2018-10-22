package gft.mentoring.trs.model;


import lombok.val;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

class TRSInput {

    List<TRSModel> readExcelTRSfile(String trsFile) {
        Workbook workbook;

        try {
            workbook = WorkbookFactory.create(new File(trsFile));
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = sheet.iterator();
            /* first row is simply names of columns*/
            iterator.next();
            val result = readRows(iterator);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<TRSModel> readRows(Iterator<Row> iterator) {
    }
}
