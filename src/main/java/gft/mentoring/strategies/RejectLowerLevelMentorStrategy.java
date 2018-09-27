package gft.mentoring.strategies;

import gft.mentoring.*;

public class RejectLowerLevelMentorStrategy implements VotingStrategy {
    @Override
    public VotingResult calculateSympathy(MentoringModel mentee, MentoringModel mentor) {
        if (mentee.isMentee() && !mentor.isMentee() && mentee.getLevel() > mentor.getLevel()) return Rejected.INSTANCE;
        return Neutral.INSTANCE;
    }
}
