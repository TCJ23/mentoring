package gft.mentoring.strategies;

import gft.mentoring.*;
import org.jetbrains.annotations.NotNull;

public class RejectLowerSeniorityDevManStrategy implements VotingStrategy {
    @Override
    public VotingResult calculateSympathy(@NotNull MentoringModel mentee, @NotNull MentoringModel mentor) {
        if (mentee.isOnlyMentee() && !mentor.isOnlyMentee() && mentee.getSeniority() > mentor.getSeniority())
            return Rejected.INSTANCE;
        return Neutral.INSTANCE;    }
}
