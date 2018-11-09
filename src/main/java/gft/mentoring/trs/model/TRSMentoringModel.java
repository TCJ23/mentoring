package gft.mentoring.trs.model;


import gft.mentoring.Family;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author tzje
 * This is a intermediate model we will use to convert infromation from SAP to create Mentoring Model
 */
@Data
class TRSMentoringModel {

    private int level; //grade
    private int seniority; //startDate
    private boolean leaver; //status
    private boolean contractor;//contractType
    private Family family; //jobFamily
    private String specialization; //technology
    private String localization; // officeLocation
    /**
     * Below 2 params will help to Match data between TRS & SAP
     */
    private String firstName; //name
    private String lastName; //surname
}
