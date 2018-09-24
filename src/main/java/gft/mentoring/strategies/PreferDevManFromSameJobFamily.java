package gft.mentoring.strategies;

import gft.mentoring.*;

public class PreferDevManFromSameJobFamily implements VotingStrategy {
    @Override
    public VotingResult calculateSympathy(MentoringModel mentee, MentoringModel mentor) {
        if (mentee.getFamily().equals(mentor.getFamily()))
            return new Support(100);

        return Neutral.INSTANCE;
    }
}
