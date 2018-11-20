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
     * @param family is department you work for GFT in i.e Governance
     * @param specialization is your specialized skillset in Family i.e. Data -> Big Data
     * @param level - we have 7 levels in GFT , 7 is highest
     * @param seniority - days since you started work at GFT
     * @param localization - city of your GFT office
     * @param contractor - although model resembles all GFT people some are on permanent contract and they're employees
     * if this is B2B type of employment they're contractors.
     * @param leaver - person that has submitted resignation from work in GFT
     * @param menteesAssigned - for GFT Mentor number of current mentees assigned
     * @param age - we prefer experienced GFT Mentors starting from 30 years to 40
     */

    private Family family;
    private String specialization;
    private int level;
    private int seniority;
    private String localization;
    private String firstName;
    private String lastName;
    private boolean contractor;
    private boolean leaver;
    private int menteesAssigned;
    private int age;
}