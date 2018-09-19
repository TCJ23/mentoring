package gft.mentoring.strategies;

import gft.mentoring.*;

/*1.4. Mentor has company experience
        (Sławomir Siudek)
        Person who is hired less them one year can't be a Mentor.*/

public class MentorHasToWorkAtLeastOneYearInGFT implements VotingStrategy {

    @Override
    public VotingResult calculateSympathy(MentoringModel mentee, MentoringModel mentor) {
        return Neutral.INSTANCE;
    }
}
