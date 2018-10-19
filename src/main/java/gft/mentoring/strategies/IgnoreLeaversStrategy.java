package gft.mentoring.strategies;

import gft.mentoring.*;
import org.jetbrains.annotations.NotNull;

public class IgnoreLeaversStrategy implements VotingStrategy {
    @Override
    public VotingResult calculateSympathy(@NotNull MentoringModel mentee, @NotNull MentoringModel mentor) {
        if (mentor.isLeaver()) return Rejected.INSTANCE;
        return Neutral.INSTANCE;
    }
}
