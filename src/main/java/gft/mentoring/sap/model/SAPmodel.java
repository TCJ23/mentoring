package gft.mentoring.sap.model;

import lombok.Data;

import java.util.Date;

@Data
public class SAPmodel {
    private String firstName;
    private String lastName;
    private String initials;
    private String personalNR;
    private String employeeSubGrp;
    private String position;
    private String job;
    private String costCenter;
    private Date initEntry;
    private String persNrSuperior;
    private String persNrMentor;
}
