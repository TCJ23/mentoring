package gft.mentoring.strategies;

import gft.mentoring.*;
import org.jetbrains.annotations.NotNull;

public class IgnoreContractorsStrategy implements VotingStrategy {
    @Override
    public VotingResult calculateSympathy(@NotNull MentoringModel mentee, @NotNull MentoringModel mentor) {
        if (mentee.isContractor() || mentor.isContractor()) return Rejected.INSTANCE;
        return Neutral.INSTANCE;
    }
}
