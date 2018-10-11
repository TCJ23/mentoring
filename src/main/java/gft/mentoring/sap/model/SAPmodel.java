package gft.mentoring.sap.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
class SAPmodel {
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
