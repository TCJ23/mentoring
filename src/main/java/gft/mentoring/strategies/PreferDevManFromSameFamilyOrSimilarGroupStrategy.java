package gft.mentoring.strategies;

/*
1.1 JobFamily equivalent for Development
Context: In GFT Career Model we have Job Families some of them are considered as one technical perspective.

Problem: Completely separation of Job Families in context of creating Development management structure creates poorly
balanced(triaged) development career tree. Consequently some Mentors in small families i.e L5 DevMan will only have
1-2 Mentees, in big family like Development which consists of several other Families i.e L5 DevMan will have even 5 Mentees.

Solution: Let's assume that families (thereafter Development Group):
Project Development
Architecture
Digital
Data
are cross functional in term of choosing DevMan and Mentee so that person from from Project Development Family will
not be constantly overburden by allowing Digital, Architecture & Data - Mentors to be assigned Mentees from above 4 Families
altogether, considered as one common other Development Group.*/

import gft.mentoring.*;
import org.jetbrains.annotations.NotNull;

public class PreferDevManFromSameFamilyOrSimilarGroupStrategy implements VotingStrategy {
    /* this method will calculate sympathy between mentee and corresponding mentor or mentors
     * in Development Group
     * @return value should be Support @see VotingResult
     * IF
     * @param mentee is in one of 4 families considered as larger Development Group
     * AND
     * @param mentor(s)is in one of 4 families considered as larger Development Group
     * ELSE
     * @return value should be Neutral @see VotingResult*/
    @Override
    public VotingResult calculateSympathy(@NotNull MentoringModel mentee, @NotNull MentoringModel mentor) {
        if (mentee.getFamily().equals(mentor.getFamily())) return new Support(100);

        if (isDevelopmentGroup(mentee.getFamily()) && isDevelopmentGroup(mentor.getFamily())) return new Support(50);

        if (isDevelopmentGroup(mentee.getFamily()) ^ isDevelopmentGroup(mentor.getFamily())) return Rejected.INSTANCE;

        return Rejected.INSTANCE;

    }

    private boolean isDevelopmentGroup(Family family) {
        switch (family) {
            case DIGITAL:
            case PROJECT_DEVELOPMENT:
            case ARCHITECTURE:
            case DATA:
                return true;
            default:
                return false;
        }
    }
}
