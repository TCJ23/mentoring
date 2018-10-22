package gft.mentoring.trs.model;

import lombok.val;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.jetbrains.annotations.NotNull;
import org.junit.platform.commons.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

class TRSInput {

    private DataFormatter formatter = new DataFormatter();
    private Predicate<TRSModel> validator = new Validator();

    List<TRSModel> readExcelTRSfile(String trsFile) throws IOException, InvalidFormatException {
        Workbook workbook;
        workbook = WorkbookFactory.create(new File(trsFile));
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = sheet.iterator();
        /* first row is simply names of columns*/
        iterator.next();
        val result = readRows(iterator);
        workbook.close();
        return result;
    }

    private List<TRSModel> readRows(Iterator<Row> data) {
        /** We expect that number of columns will NOT change application mechanism*/
        List<BiConsumer<Cell, TRSModel>> trsModels = new ArrayList<>(11);
        trsModels.add((cell, trser) -> trser.setName(stringFromCell(cell)));
        trsModels.add((cell, trser) -> trser.setSurname(stringFromCell(cell)));
        trsModels.add((cell, trser) -> trser.setStatus(stringFromCell(cell)));
        trsModels.add((cell, trser) -> trser.setGrade(stringFromCell(cell)));
        trsModels.add((cell, trser) -> trser.setTechnology(stringFromCell(cell)));
        trsModels.add((cell, trser) -> trser.setJobFamily(stringFromCell(cell)));
        trsModels.add((cell, trser) -> trser.setStartDate(stringFromCell(cell)));
        trsModels.add((cell, trser) -> trser.setOfficeLocation(stringFromCell(cell)));
        trsModels.add((cell, trser) -> trser.setContractType(dateFromCell(cell)));
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(data, Spliterator.ORDERED), false)
                .map(row -> createSAPmodelFromRow(row, trsModels))
                .filter(validator)
                .collect(Collectors.toList());
    }

    class Validator implements Predicate<TRSModel> {
        @Override
        public boolean test(TRSModel trsModel) {
            return StringUtils.isNotBlank(trsModel.getName());
        }
    }

    private TRSModel createSAPmodelFromRow(@NotNull Row row, List<BiConsumer<Cell, TRSModel>> model) {
        TRSModel trser = new TRSModel();
        int i = 0;
        for (BiConsumer<Cell, TRSModel> f : model) {
            f.accept(getCell(row, i++), trser);
        }
        return trser;
    }

    private String dateFromCell(Cell cell) {
        return formatter.formatCellValue(cell);
    }

    private String stringFromCell(Cell cell) {
        return cell.getStringCellValue();
    }

    private Cell getCell(@NotNull Row row, int i) {
        return row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
    }

    long notNullRows(Workbook workbook) {
        Iterator<Row> rows = workbook.getSheetAt(0).iterator();
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(rows, Spliterator.ORDERED), false)
                .map(row -> StreamSupport.stream(row.spliterator(), false).anyMatch(cell -> isNonEmptyCell(cell)))
                .filter(c -> c)
                .count();
    }

    private boolean isNonEmptyCell(Cell cell) {
        return cell.getCellTypeEnum() != CellType.BLANK
                && (cell.getCellTypeEnum() != CellType.STRING ||
                stringFromCell(cell).length() > 0);
    }
}
