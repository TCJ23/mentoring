package gft.mentoring.sap.model;

import lombok.val;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.jetbrains.annotations.NotNull;
import org.junit.platform.commons.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
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
    private static final String SAP_FILE = "./Sample_SAP_DevMan_20180821.xlsx";

    private Predicate<SAPmodel> validator = new Validator();

    List<SAPmodel> readExcelSAPfile(String inputFile) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(inputFile));
        Sheet sheet = workbook.getSheetAt(0);
        /* first row contains simply names of columns */
        Iterator<Row> iterator = sheet.iterator();
        iterator.next();
        val result = read(iterator);
        workbook.close();
        return result;
    }

    List<SAPmodel> read(Iterator<Row> data) {
        /** We don't expect that number of columns will change, if so this will not affect application */
        List<Function<Cell, Consumer<SAPmodel>>> sapModels = new ArrayList<>(10);
        sapModels.add(cell -> saper -> saper.setFirstName(stringFromCell(cell)));
        sapModels.add(cell -> saper -> saper.setLastName(stringFromCell(cell)));
        sapModels.add(cell -> saper -> saper.setInitials(stringFromCell(cell)));
        sapModels.add(cell -> saper -> saper.setPersonalNR(stringFromCell(cell)));
        sapModels.add(cell -> saper -> saper.setEmployeeSubGrp(stringFromCell(cell)));
        sapModels.add(cell -> saper -> saper.setPosition(stringFromCell(cell)));
        sapModels.add(cell -> saper -> saper.setJob(stringFromCell(cell)));
        sapModels.add(cell -> saper -> saper.setCostCenter(stringFromCell(cell)));
        sapModels.add(cell -> saper -> saper.setInitEntry(dateFromCell(cell)));
        sapModels.add(cell -> saper -> saper.setPersNrSuperior(SAPInput.this.stringFromCell(cell)));
        sapModels.add(new Function<Cell, Consumer<SAPmodel>>() {
            @Override
            public Consumer<SAPmodel> apply(Cell cell) {
                return new Consumer<SAPmodel>() {
                    @Override
                    public void accept(SAPmodel saper) {
                        saper.setPersNrMentor(SAPInput.this.stringFromCell(cell));
                    }
                };
            }
        });
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(data, Spliterator.ORDERED), false).map(row -> createSAPmodelFromRow(row, sapModels))
                .filter(validator)
                .collect(Collectors.toList());
    }


    class Validator implements Predicate<SAPmodel> {
        @Override
        public boolean test(SAPmodel sapModel) {
            return StringUtils.isNotBlank(sapModel.getFirstName());
        }
    }

    private SAPmodel createSAPmodelFromRow(@NotNull Row row, List<Function<Cell, Consumer<SAPmodel>>> model) {
        SAPmodel saper = new SAPmodel();

        int i = 0;
        for (Function<Cell, Consumer<SAPmodel>> f : model) {
            f.apply(getCell(row, i++)).accept(saper);
        }
        return saper;
    }

    private Date dateFromCell(Cell cell) {
        return cell.getDateCellValue();
    }

    private String stringFromCell(Cell cell) {
        return cell.getStringCellValue();
    }

    private Cell getCell(@NotNull Row row, int i) {
        return row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
    }

    int notNullRows(Workbook workbook) {
        int notNullCount = 0;
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getCellTypeEnum() != CellType.BLANK) {
                    if (cell.getCellTypeEnum() == CellType.STRING &&
                            stringFromCell(cell).length() <= 0) {
                        continue;
                    }
                    notNullCount++;
                    break;
                }
            }
        }
        return notNullCount;
    }
}
