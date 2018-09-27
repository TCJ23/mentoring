package gft.mentoring.strategies;

import gft.mentoring.*;

public class PreferMentorWithHigherSeniorityStrategy implements VotingStrategy {
    @Override
    public VotingResult calculateSympathy(MentoringModel mentor1, MentoringModel mentor2) {
        if (mentor1.getSeniority() > mentor2.getSeniority()) return new Support(-25);
        if (mentor1.getSeniority() < mentor2.getSeniority()) return new Support(25);
        return Neutral.INSTANCE;
    }
}
