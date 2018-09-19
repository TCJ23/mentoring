package gft.mentoring.strategies;

import gft.mentoring.*;
import lombok.NonNull;

import java.util.Objects;
/*1.3
* Corporate Services separation by Specialisation
  @author(Sławomir Siudek)
  For Corporate Services there are too many different specialisations
  so Specialisation fields - which contains specialisations (like Finance, HR etc) is used
  to separate matching of mentor <--> mentee.
  In other words, if the person is from Corporate Services JobFamily
  we take into account matching by specialisation as required.
  An example: HR person can't be mentor for IT Support person even they
  are both from Corporate Services.*/

public class PreferDevManFromCorporateServicesWithSameSpecializaton implements VotingStrategy {

    /**
     * Calculates sympathy between mentee and corresponding mentor or mentors
     * in Corporate Services
     *
     * @return value should be Support @see VotingResult
     * @param mentee is in one of 4 families considered as larger Development Group
     * @param mentor(s)is in one of 4 families considered as larger Development Group
     * @return value should be Neutral @see VotingResult*/
    @Override
    public VotingResult calculateSympathy(@NonNull MentoringModel mentee, @NonNull MentoringModel mentor) {

        if (!Family.CORPORATE_SERVICES.equals(mentee.getFamily())) return Neutral.INSTANCE;
        if (!Family.CORPORATE_SERVICES.equals(mentor.getFamily())) return Neutral.INSTANCE;

        if (Objects.equals(mentee.getSpecialization(), mentor.getSpecialization())) {
            return new Support(100);
        }
        return new Rejected();
    }
}
