package gft.mentoring.strategies;

import gft.mentoring.*;

public class IgnoreContractorsStrategy implements VotingStrategy {
    @Override
    public VotingResult calculateSympathy(MentoringModel mentee, MentoringModel mentor) {
        if (mentee.isContractor() || mentor.isContractor()) return Rejected.INSTANCE;
        return Neutral.INSTANCE;
    }
}
