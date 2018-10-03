package gft.mentoring.strategies;

import gft.mentoring.*;

public class PreferDevManWithHigherLevelStrategy implements VotingStrategy {
    @Override
    public VotingResult calculateSympathy(MentoringModel mentee, MentoringModel mentor) {
        if (mentee.getLevel() < mentor.getLevel()) return new Support(25);
        return Neutral.INSTANCE;
    }
}
