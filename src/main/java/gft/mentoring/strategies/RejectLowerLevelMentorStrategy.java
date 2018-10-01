package gft.mentoring.strategies;

import gft.mentoring.*;
import org.jetbrains.annotations.NotNull;

public class RejectLowerLevelMentorStrategy implements VotingStrategy {

    @Override
    public VotingResult calculateSympathy(@NotNull MentoringModel mentee, @NotNull MentoringModel mentor) {
        if (mentee.isOnlyMentee() && !mentor.isOnlyMentee() && mentee.getLevel() > mentor.getLevel())
            return Rejected.INSTANCE;
        return Neutral.INSTANCE;
    }
}
