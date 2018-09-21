package gft.mentoring.strategies;

import gft.mentoring.*;

public class PreferSameLocalizationDevMan implements VotingStrategy {
    @Override
    public VotingResult calculateSympathy(MentoringModel mentee, MentoringModel mentor) {
        if (mentee.getLocalization().equals(mentor.getLocalization())) {
            return new Support(100);
        }
        return Neutral.INSTANCE;
    }
}
