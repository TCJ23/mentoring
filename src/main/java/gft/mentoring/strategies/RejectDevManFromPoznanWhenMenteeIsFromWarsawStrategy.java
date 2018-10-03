package gft.mentoring.strategies;

import gft.mentoring.*;
import org.jetbrains.annotations.NotNull;

public class RejectDevManFromPoznanWhenMenteeIsFromWarsawStrategy implements VotingStrategy {
    @Override
    public VotingResult calculateSympathy(@NotNull MentoringModel mentee, @NotNull MentoringModel mentor) {
        if (mentee.getLocalization().equalsIgnoreCase("Warsaw") &&
                mentor.getLocalization().equalsIgnoreCase("Poznan")) return Rejected.INSTANCE;
        return Neutral.INSTANCE;
    }
}
