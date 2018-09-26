package gft.mentoring.strategies;

import gft.mentoring.MentoringModel;
import gft.mentoring.Neutral;
import gft.mentoring.VotingResult;
import gft.mentoring.VotingStrategy;
import org.jetbrains.annotations.NotNull;

public class PreferHigherSeniorityStrategy implements VotingStrategy {
    @Override
    public VotingResult calculateSympathy(@NotNull MentoringModel mentee, @NotNull MentoringModel mentor) {
        return Neutral.INSTANCE;
    }
}
