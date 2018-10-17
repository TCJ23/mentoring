package gft.mentoring.sap.model;

public class ExcelException extends Exception {
    public ExcelException(String reason, String statement) {
        super(reason = ": " + statement);
    }

    public ExcelException(String reason, String statement, Throwable cause) {
        super(reason = ": " + statement, cause);
    }
//    java.io.FileNotFoundException: .\Sample_SAP_DevMan_20180821.xlsx (The process cannot access the file because it is being used by another process)

}
