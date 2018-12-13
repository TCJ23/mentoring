package gft.mentoring.strategies;

import gft.mentoring.*;
import org.jetbrains.annotations.NotNull;

public class PreferDevManWithHigherLevelStrategy implements VotingStrategy {
    @Override
    public VotingResult calculateSympathy(@NotNull MentoringModel mentee, @NotNull MentoringModel mentor) {
        /**
         *  Calibration -> Historically new Support with Value 100 or 50 would BRAKE test 1.14 Seniority over Level
         *  so we set it to 25, but after improving seniority calculation we are getting back to default 100*/
        if (mentee.getLevel() < mentor.getLevel()) return new Support(100);
        return Neutral.INSTANCE;
    }
}
