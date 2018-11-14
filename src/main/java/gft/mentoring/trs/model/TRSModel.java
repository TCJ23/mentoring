package gft.mentoring.trs.model;

import gft.mentoring.Family;
import gft.mentoring.MentoringModel;
import gft.mentoring.sap.model.SAPMentoringModel;
import lombok.*;

/**
 * @author tzje
 * This model should be created while reading excel file delivered by TRS only from valid and useful fields
 */
@NoArgsConstructor
@Data
public class TRSModel {
    /** At the time of writing only below fields hold useful information
     * @param name - will be used for Merging TRS and SAP models into final MentoringModel
     * @param surname - will be used for Merging TRS and SAP models into final MentoringModel
     * @param status - this field appears to hold intel about if GFT person is a leaver, only
     *               Employee can be considered as candidate in MentoringModel
     *               Hired will be ignored as these people will only be hired by GFT in future
     *               Notice Period - marks person as Leaver
     * @see MentoringModel#isLeaver()
     * @param grade - level
     * @see MentoringModel#getLevel()
     * @param technology - specialization
     * @see MentoringModel#getSpecialization()
     * @param jobFamily - simply Family of GFT person
     * @see Family
     * @param startDate - this field appears to hold intel about how long person is working at GFT company
     * @see MentoringModel#getSeniority()
     * @param officeLocation
     * @see MentoringModel#getLocalization()
     * @param contractType - this param should match between TRS & SAP models
     * @see MentoringModel#isContractor()
     * &&
     * @see SAPMentoringModel#isContractor()
     * */
    private String name;
    private String surname;
    private String status;//contains also leaver (is or not contractor)
    private String grade;
    private String technology;
    private String jobFamily;
    private String startDate;
    private String officeLocation;
    private String contractType;
}
