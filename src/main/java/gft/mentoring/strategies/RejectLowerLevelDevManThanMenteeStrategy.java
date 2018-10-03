package gft.mentoring.strategies;

import gft.mentoring.*;
import org.jetbrains.annotations.NotNull;

public class RejectLowerLevelDevManThanMenteeStrategy implements VotingStrategy {

    @Override
    public VotingResult calculateSympathy(@NotNull MentoringModel mentee, @NotNull MentoringModel mentor) {
        if (mentee.getLevel() > mentor.getLevel())
            return Rejected.INSTANCE;
        return Neutral.INSTANCE;
    }
}
