package gft.mentoring.sap.model;

import gft.mentoring.Family;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
class SAPMentoringModel {

    private String firstName;
    private String lastName;
    private int federationID; //personalNR
    private long sapID;
    private boolean contractor; //employeeSubGrp
    private Family family; //position
    private int level; //job
    private String specialization; //costCenter ?
    private String seniority; //initEntry
    private int lineManagerID; //persNrSuperior ?
    private int menteeID; //persNrMentor

}
