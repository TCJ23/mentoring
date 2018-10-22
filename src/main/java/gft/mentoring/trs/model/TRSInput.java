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

        List<String> headers = getHeaders(iterator.next());
        val result = readRows(headers, iterator);
        workbook.close();
        return result;
    }

    private List<String> getHeaders(Row row) {
        return StreamSupport.stream(row.spliterator(), false)
                .map(cell -> cell.getStringCellValue().trim().toLowerCase())
                .collect(Collectors.toList());
    }

    private List<TRSModel> readRows(List<String> headers, Iterator<Row> data) {
        HashMap<Integer, BiConsumer<Cell, TRSModel>> trsModels = new HashMap<>();
        trsModels.put(headerIndex(headers, "name"), (cell, treser) -> treser.setName(stringFromCell(cell)));
        trsModels.put(headerIndex(headers, "surname"), (cell, treser) -> treser.setSurname(stringFromCell(cell)));
        trsModels.put(headerIndex(headers, "status"), (cell, treser) -> treser.setStatus(stringFromCell(cell)));
        trsModels.put(headerIndex(headers, "grade"), (cell, treser) -> treser.setGrade(stringFromCell(cell)));
        trsModels.put(headerIndex(headers, "technology"), (cell, treser) -> treser.setTechnology(stringFromCell(cell)));
        trsModels.put(headerIndex(headers, "job family"), (cell, treser) -> treser.setJobFamily(stringFromCell(cell)));
        trsModels.put(headerIndex(headers, "start date"), (cell, treser) -> treser.setStartDate(dateFromCell(cell)));
        trsModels.put(headerIndex(headers, "office location"), (cell, treser) -> treser.setOfficeLocation(stringFromCell(cell)));
        trsModels.put(headerIndex(headers, "contract type"), (cell, treser) -> treser.setContractType(dateFromCell(cell)));
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(data, Spliterator.ORDERED), false)
                .map(row -> createSAPmodelFromRow(row, trsModels))
                .filter(validator)
                .collect(Collectors.toList());
    }

    private int headerIndex(List<String> headers, String name) {
        return headers.indexOf(name);
    }

    class Validator implements Predicate<TRSModel> {
        @Override
        public boolean test(TRSModel trsModel) {
            return StringUtils.isNotBlank(trsModel.getName());
        }
    }

    private TRSModel createSAPmodelFromRow(@NotNull Row row, Map<Integer, BiConsumer<Cell, TRSModel>> model) {
        TRSModel treser = new TRSModel();
        for (Map.Entry<Integer, BiConsumer<Cell, TRSModel>> entry : model.entrySet()) {
            entry.getValue().accept(getCell(row, entry.getKey()), treser);
        }
        return treser;
    }

    private String dateFromCell(@NotNull Cell cell) {
        return formatter.formatCellValue(cell);
    }

    private String stringFromCell(@NotNull Cell cell) {
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
