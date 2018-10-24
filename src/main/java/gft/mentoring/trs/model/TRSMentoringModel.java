package gft.mentoring.trs.model;


import gft.mentoring.Family;
import lombok.Getter;
import lombok.Setter;

/**
 * @author tzje
 * This is a intermediate model we will use to convert infromation from SAP to create Mentoring Model
 */
@Getter
@Setter
class TRSMentoringModel {

    private boolean leaver; //status
    private int level; //grade
    private String specialization; //technology
    private Family family; //jobFamily
    private int seniority; // startDate
    private String localization; // officeLocation
    private boolean contractor;//contractType
    /**
     * Below 2 params will help to Match data between TRS & SAP
     */
    private String firstName; //name
    private String lastName; //surname

}
