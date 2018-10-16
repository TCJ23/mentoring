package gft.mentoring.sap.model;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ExcelOpenException extends Exception {
    public ExcelOpenException(String message) {
        super(message);
    }
//    java.io.FileNotFoundException: .\Sample_SAP_DevMan_20180821.xlsx (The process cannot access the file because it is being used by another process)

}
