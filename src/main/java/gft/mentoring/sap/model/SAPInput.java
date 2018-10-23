package gft.mentoring.sap.model;

import lombok.val;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.jetbrains.annotations.NotNull;
import org.junit.platform.commons.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
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

    /**
     * We cannot avoid some of exceptions to be thrown, this might not be an issue when running application in production
     * due to formal instructions provided to user
     * we cannot replicate some of that behaviour, for details please check
     *
     * @param inputFile is an existing file (not a directory) which can be written.
     * @throws ExcelException         if FileNotFoundException or IOException
     * @throws InvalidFormatException - we cannot replicate this behaviour, for details please check
     * @throws FileNotFoundException  (The process cannot access the file because it is being used by another process)
     *                                if SAP file is open simultaneously with program being run this will cause above exception
     * @link SAPInputTest
     * <p>
     * Change the contents of a text file in its entirety, overwriting any
     * existing text.
     **/
    List<SAPmodel> readExcelSAPfile(@NotNull String inputFile) throws ExcelException, InvalidFormatException {
        Workbook workbook;
        try {
            workbook = WorkbookFactory.create(new File(inputFile));
            Sheet sheet = workbook.getSheetAt(0);
            /* first row contains simply names of columns */
            Iterator<Row> iterator = sheet.iterator();
            List<String> headers = getHeaders(iterator.next());
            val result = readRowsSAP(headers, iterator);
            workbook.close();
            return result;
        } catch (FileNotFoundException e) {
            throw new ExcelException("File not found or inaccessible", e);
            /**@link SAPInputTest code coverage decreased by disabling exceptionFileIsLocked test 3.1.2 */
        } catch (IOException e) {
            throw new ExcelException("Error reading file", e);
        }
    }

    List<String> getHeaders(@NotNull Row row) {
        return StreamSupport.stream(row.spliterator(), false)
                .map(cell -> cell.getStringCellValue().trim().toLowerCase())
                .collect(Collectors.toList());
    }

    List<SAPmodel> readRowsSAP(@NotNull List<String> headers, @NotNull Iterator<Row> data) {
        HashMap<Integer, BiConsumer<Cell, SAPmodel>> sapModels = new HashMap<>();
        sapModels.put(headerIndex(headers, "first name"), (cell, saper) -> saper.setFirstName(stringFromCell(cell)));
        sapModels.put(headerIndex(headers, "last name"), (cell, saper) -> saper.setLastName(stringFromCell(cell)));
        sapModels.put(headerIndex(headers, "initials"), (cell, saper) -> saper.setInitials(stringFromCell(cell)));
        sapModels.put(headerIndex(headers, "pers.no."), (cell, saper) -> saper.setPersonalNR(stringFromCell(cell)));
        sapModels.put(headerIndex(headers, "employee subgroup"), (cell, saper) -> saper.setEmployeeSubGrp(stringFromCell(cell)));
        sapModels.put(headerIndex(headers, "position"), (cell, saper) -> saper.setPosition(stringFromCell(cell)));
        sapModels.put(headerIndex(headers, "job"), (cell, saper) -> saper.setJob(stringFromCell(cell)));
        sapModels.put(headerIndex(headers, "cost center"), (cell, saper) -> saper.setCostCenter(stringFromCell(cell)));
        sapModels.put(headerIndex(headers, "init.entry"), (cell, saper) -> saper.setInitEntry(dateFromCell(cell)));
        sapModels.put(headerIndex(headers, "pers.no. superior"), (cell, saper) -> saper.setPersNrSuperior(stringFromCell(cell)));
        sapModels.put((headerIndex(headers, "pers.no. mentor")), (cell, saper) -> saper.setPersNrMentor(stringFromCell(cell)));
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(data, Spliterator.ORDERED), false)
                .map(row -> createSAPmodelFromRow(row, sapModels))
                .filter(validator)
                .collect(Collectors.toList());
    }

    /* please remain consistent and keep header name in LOWERCASE*/
    private int headerIndex(@NotNull List<String> headers, @NotNull String name) {
        return headers.indexOf(name);

    }

    class Validator implements Predicate<SAPmodel> {
        @Override
        public boolean test(SAPmodel sapModel) {
            return StringUtils.isNotBlank(sapModel.getFirstName());
        }
    }

    private SAPmodel createSAPmodelFromRow(@NotNull Row row, Map<Integer, BiConsumer<Cell, SAPmodel>> model) {
        SAPmodel saper = new SAPmodel();
        for (Map.Entry<Integer, BiConsumer<Cell, SAPmodel>> entry : model.entrySet()) {
            entry.getValue().accept(getCell(row, entry.getKey()), saper);
        }
        return saper;
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
