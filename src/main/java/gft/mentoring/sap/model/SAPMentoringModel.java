package gft.mentoring.sap.model;

import gft.mentoring.Family;
import gft.mentoring.trs.model.TRSModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author tzje
 * This is a intermediate model we will use to convert infromation from SAP to create Mentoring Model
 */
@Getter
@Setter
@ToString
public class SAPMentoringModel {
    /**
     * At the time of writing we only see potential in only 3 parameters that hold any business logic value
     *
     * @param contractor - will help determine if person is an employee or contractor
     * @param family - this enum holds 10 values that represent 10 Families in GFT
     * @param level - this will help to convert to correct level per GFT employee
     * @param seniority - although a lot of GFT people have same init entry, ignoring fact that they started to work
     * in different moment in Rule Financial, since GFT acquisition this date gives true value. This
     * can be recalculated with start date from TRS data
     * @param officeLocation - this should match TRS Office Location info although values are in English here
     * @param menteesAssigned - calculate mentees assigned to mentor using SAP file information by taking
     * @param sapID
     * @param mentorID
     * @see TRSModel#getStartDate()
     * @see TRSModel#getOfficeLocation() *
     */

    private boolean contractor; //employeeSubGrp
    private Family family; //jobFamily
    private int level; //job
    private int seniority; //initEntry
    private int age;//date of birth
    /**
     * Below 2 params will help to Match data between TRS & SAP
     */
    private String firstName;
    private String lastName;
    private String officeLocation; //personnelSubarea
    /**
     * We calculate mentees assigned to mentor using SAP file information by taking sapID and its occurrence
     * in column with MentorID
     */
    private int menteesAssigned; // combination of below 2 params
    private String sapID; //Pers.No.
    private String mentorID; //persNrMentor
    /**
     * At the time of writing below values from SAP do not hold useful information
     * it might occur in future that some of these will be used to match SAP information with TRS information
     */
    private String federationID; //Initials
    private String specialization; //costCenter ?
    private String lineManagerID; //persNrSuperior ?
}
