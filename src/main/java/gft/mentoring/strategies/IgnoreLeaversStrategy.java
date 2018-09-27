package gft.mentoring.strategies;

import gft.mentoring.*;

public class IgnoreLeaversStrategy implements VotingStrategy {
    @Override
    public VotingResult calculateSympathy(MentoringModel mentee, MentoringModel mentor) {
        if (mentor.isLeaver()) return Rejected.INSTANCE;
        return Neutral.INSTANCE;
    }
}
