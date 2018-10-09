package gft.mentoring.sap.model;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReadSAPxlsx {
    static final String SAP_FILE = "./Sample_SAP_DevMan_20180821.xlsx";
//    static final String SAP_FILE = "D:\\Library\\devman-generator\\Sample_SAP_DevMan_20180821.xlsx";


    public static void main(String[] args) throws IOException, InvalidFormatException {
        File plik = new File(SAP_FILE);
        Path path = Paths.get(SAP_FILE);
        System.out.println(plik.exists());
        System.out.println(plik.canRead());
        System.out.println(plik.getAbsolutePath());
        System.out.println(plik.getAbsoluteFile());


        Workbook workbook = WorkbookFactory.create(new File(SAP_FILE));

        System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");

        System.out.println("Retrieving Sheets using Java 8 ");
        workbook.forEach(sheet -> {
            System.out.println("=> " + sheet.getSheetName());
        });

        // Getting the Sheet at index zero
        Sheet sheet = workbook.getSheetAt(0);

        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();

        System.out.println("\n\nIterating over Rows and Columns using Java 8 \n");
        sheet.forEach(row -> {
            row.forEach(cell -> {
                String cellValue = dataFormatter.formatCellValue(cell);
                System.out.print(cellValue + "\t");
            });
            System.out.println();
        });

        // Closing the workbook
        workbook.close();
    }
}
