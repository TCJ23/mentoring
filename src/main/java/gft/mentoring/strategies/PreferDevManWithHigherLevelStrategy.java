package gft.mentoring.strategies;

import gft.mentoring.*;
import org.jetbrains.annotations.NotNull;

public class PreferDevManWithHigherLevelStrategy implements VotingStrategy {
    @Override
    public VotingResult calculateSympathy(@NotNull MentoringModel mentee, @NotNull MentoringModel mentor) {
        /** Calibration -> new Support with Value 100 or 50 will BRAKE test 1.14 Seniority over Level */
        if (mentee.getLevel() < mentor.getLevel()) return new Support(25);
        return Neutral.INSTANCE;
    }
}
