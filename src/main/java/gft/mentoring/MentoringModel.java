package gft.mentoring;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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
//     * @param onlyMentee - GFT people can only become mentors from Level 4 and above, it means that not everyone in GFT can me Mentor
     * @param menteesAssigned - for GFT Mentor number of current mentees assigned
     * @param age - we prefer experienced GFT Mentors starting from 30 years to 40
     */

    private Family family;
    private String specialization;
    private int level;
    private int seniority;
    private String localization;
    private boolean contractor;
    private boolean leaver;
    private int menteesAssigned;
    private int age;

   /* public boolean isOnlyMentee() {
        if (MentoringModel.builder().level < 4) return true;
        return false;
    }*/
    /*public void setLevel(int level) {
        if(level < 4){
            this.onlyMentee = true;
        }
        this.level = level;
    }*/
}