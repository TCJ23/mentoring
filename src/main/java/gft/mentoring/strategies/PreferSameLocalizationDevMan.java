package gft.mentoring.strategies;

import gft.mentoring.*;
import org.jetbrains.annotations.NotNull;

public class PreferSameLocalizationDevMan implements VotingStrategy {

    @Override
    public VotingResult calculateSympathy(@NotNull MentoringModel mentee, @NotNull MentoringModel mentor) {
        if (mentee.getLocalization().equals(mentor.getLocalization()))
        {
            return new Support(100);
        }
        return Neutral.INSTANCE;
    }
}
