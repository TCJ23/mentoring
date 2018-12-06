package gft.mentoring.strategies;

import gft.mentoring.*;
import org.jetbrains.annotations.NotNull;

public class RejectDevManThatIsMentee implements VotingStrategy {
    @Override
    public VotingResult calculateSympathy(@NotNull MentoringModel mentee, @NotNull MentoringModel mentor) {

        if (mentor.isMentee()) return Rejected.INSTANCE;

        return Neutral.INSTANCE;
    }
}
