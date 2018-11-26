package gft.mentoring.sap.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author tzje
 * This is one to one model created while reading excel file delivered by SAP
 */
@NoArgsConstructor
@Data
class SAPModel {
    private String firstName;
    private String lastName;
    private String initials;
    private String employeeSubGrp;
    private String jobFamily; //position
    private String job;
    private String costCenter;
    private String initEntry;
    private String persNrSuperior;
    private String dateOfBirth;
    private String personnelSubarea;
    /** We calculate mentees assigned to mentor using SAP file information by taking sapID and its occurrence
     * in column with MentorID*/
    private String personalNR;
    private String persNrMentor;

}
