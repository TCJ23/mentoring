package gft.mentoring.sap.model;

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

/**
 * @author tzje
 * This is main class to turn Excel data delivered from SAP in intermediate model
 */
class SAPInput {
    /**
     * We assume that SAP Excel file will be stored in agreed folder
     */
    private DataFormatter formatter = new DataFormatter();
    private Predicate<SAPmodel> validator = new Validator();

    List<SAPmodel> readExcelSAPfile(String inputFile) throws ExcelException {
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(new File(inputFile));
        } catch (IOException e) {
            throw new ExcelException("IOExc", "IO", e.getCause());
        } catch (InvalidFormatException e) {
            throw new ExcelException("INVform", "IV", e.getCause());
        }

        Sheet sheet = workbook.getSheetAt(0);
        /* first row contains simply names of columns */
        Iterator<Row> iterator = sheet.iterator();
        iterator.next();
        val result = readRows(iterator);
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    List<SAPmodel> readRows(Iterator<Row> data) {
        /** We don't expect that number of columns will change, if so this will not affect application */
        List<BiConsumer<Cell, SAPmodel>> sapModels = new ArrayList<>(11);
        sapModels.add((cell, saper) -> saper.setFirstName(stringFromCell(cell)));
        sapModels.add((cell, saper) -> saper.setLastName(stringFromCell(cell)));
        sapModels.add((cell, saper) -> saper.setInitials(stringFromCell(cell)));
        sapModels.add((cell, saper) -> saper.setPersonalNR(stringFromCell(cell)));
        sapModels.add((cell, saper) -> saper.setEmployeeSubGrp(stringFromCell(cell)));
        sapModels.add((cell, saper) -> saper.setPosition(stringFromCell(cell)));
        sapModels.add((cell, saper) -> saper.setJob(stringFromCell(cell)));
        sapModels.add((cell, saper) -> saper.setCostCenter(stringFromCell(cell)));
        sapModels.add((cell, saper) -> saper.setInitEntry(dateFromCell(cell)));
        sapModels.add((cell, saper) -> saper.setPersNrSuperior(stringFromCell(cell)));
        sapModels.add(new BiConsumer<Cell, SAPmodel>() {
            @Override
            public void accept(Cell cell, SAPmodel saper) {
                saper.setPersNrMentor(stringFromCell(cell));
            }
        });
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(data, Spliterator.ORDERED), false)
                .map(row -> createSAPmodelFromRow(row, sapModels))
                .filter(validator)
                .collect(Collectors.toList());
    }

    class Validator implements Predicate<SAPmodel> {
        @Override
        public boolean test(SAPmodel sapModel) {
            return StringUtils.isNotBlank(sapModel.getFirstName());
        }
    }

    private SAPmodel createSAPmodelFromRow(@NotNull Row row, List<BiConsumer<Cell, SAPmodel>> model) {
        SAPmodel saper = new SAPmodel();
        int i = 0;
        for (BiConsumer<Cell, SAPmodel> f : model) {
            f.accept(getCell(row, i++), saper);
        }
        return saper;
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
