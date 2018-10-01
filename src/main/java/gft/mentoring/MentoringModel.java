package gft.mentoring;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

/**
 * @author tzje
 * This is our main model class, essentially everyone in GFT can be modelled by this abstraction
 */
@Builder(toBuilder = true)
@AllArgsConstructor
@Value
public class MentoringModel {
    /**
     * @param family is department you work in i.e Governance
     * @param specialization is your specialized skillset in Family i.e. Data -> Big Data
     * @param level - we have 7 levels in GFT , 7 is highest
     * @param seniority - days since you started work at GFT
     * @param localization - city of your GFT office
     * @param contractor - although model resembles all GFT people some are on permanent contract and they're employees
     * if this is B2B type of employment they're contractors.
     * @param leaver - person that has submitted resignation from work in GFT
     * @param - some lower level GFT people can only be mentees, it means that not everyone in GFT can me Mentor
     * @param menteesAssigned - for GFT Mentor number of current mentees assigned
     */

    private Family family;
    private String specialization;
    private int level;
    private int seniority;
    private String localization;
    private boolean contractor;
    private boolean leaver;
    private boolean onlyMentee;
    private int menteesAssigned;

}