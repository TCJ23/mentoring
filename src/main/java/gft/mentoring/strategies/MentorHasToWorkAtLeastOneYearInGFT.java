package gft.mentoring.strategies;

import gft.mentoring.*;
import org.jetbrains.annotations.NotNull;

/*1.4. Mentor has company experience
        (Sławomir Siudek)
        Person who is hired less them one year can't be a Mentor.*/

public class MentorHasToWorkAtLeastOneYearInGFT implements VotingStrategy {

    @Override
    public VotingResult calculateSympathy(@NotNull MentoringModel mentee, @NotNull MentoringModel mentor) {
        if (mentor.getSeniority() < 365) return new Rejected();
//        return new Support(100);
        return Neutral.INSTANCE;
    }
}
