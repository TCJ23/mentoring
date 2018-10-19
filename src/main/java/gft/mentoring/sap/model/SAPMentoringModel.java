package gft.mentoring.sap.model;

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
    private String family; //position
    private int level; //job
    private String specialization; //costCenter ?
    private String seniority; //initEntry
    private int lineManagerID; //persNrSuperior ?
    private int menteeID; //persNrMentor

     boolean isContractor() {
        return contractor;
    }

     void setContractor(String s) {
         this.contractor = s.trim().equalsIgnoreCase("Contractors");
    }

    int getLevel() {
        return level;
    }

    void setLevel(String s) {
        try {
            if (s.equalsIgnoreCase("LD")) {
                this.level = 8;
            } else {
                this.level = Integer.parseInt(s.trim().replaceAll("[\\D]", ""));
            }
        } catch (NumberFormatException e) {
            this.level = 0;
        }
    }
}
