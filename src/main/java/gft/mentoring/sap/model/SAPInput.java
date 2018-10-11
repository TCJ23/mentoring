package gft.mentoring.sap.model;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.jetbrains.annotations.NotNull;
import org.junit.platform.commons.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Spliterator;
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
     * */
    private static final String SAP_FILE = "./Sample_SAP_DevMan_20180821.xlsx";

    private Predicate<SAPmodel> validator = new Validator();

    List<SAPmodel> readExcelSAPfile(String inputFile) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File(inputFile));
        Sheet sheet = workbook.getSheetAt(0);

        Spliterator<Row> spliterator = sheet.spliterator();

        /* first row contains simply names of columns */
        spliterator.tryAdvance(x -> {
        });

        List<Function<Cell, Consumer<SAPmodel>>> model = new ArrayList<>(10);
        model.add(cell -> model1 -> model1.setFirstName(stringFromCell(cell)));
        model.add(cell -> model1 -> model1.setLastName(stringFromCell(cell)));
        model.add(cell -> model1 -> model1.setInitials(stringFromCell(cell)));
        model.add(cell -> model1 -> model1.setPersonalNR(stringFromCell(cell)));
        model.add(cell -> model1 -> model1.setEmployeeSubGrp(stringFromCell(cell)));
        model.add(cell -> model1 -> model1.setPosition(stringFromCell(cell)));
        model.add(cell -> model1 -> model1.setJob(stringFromCell(cell)));
        model.add(cell -> model1 -> model1.setCostCenter(stringFromCell(cell)));
        model.add(cell -> model1 -> model1.setInitEntry(dateFromCell(cell)));
        model.add(cell -> model1 -> model1.setPersNrSuperior(stringFromCell(cell)));
        model.add(new Function<Cell, Consumer<SAPmodel>>() {
            @Override
            public Consumer<SAPmodel> apply(Cell cell) {
                return model1 -> model1.setPersNrMentor(SAPInput.this.stringFromCell(cell));
            }
        });

        List<SAPmodel> sapers = StreamSupport.stream(spliterator, false).map(row -> createSAPmodelFromRow(row, model))
                .filter(validator)
                .collect(Collectors.toList());

        workbook.close();
        return sapers;
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
