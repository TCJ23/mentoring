package gft.mentoring.strategies;

import gft.mentoring.*;
import org.jetbrains.annotations.NotNull;

public class RejectDevManBelow4thLevel implements VotingStrategy {
    @Override
    public VotingResult calculateSympathy(@NotNull MentoringModel mentee, @NotNull MentoringModel mentor) {
        if (mentor.getLevel() < 4) return Rejected.INSTANCE;
        return Neutral.INSTANCE;
    }
}
