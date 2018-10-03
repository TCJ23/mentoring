package gft.mentoring.strategies;

import gft.mentoring.*;
import org.jetbrains.annotations.NotNull;

public class RejectOtherLocationsForLodzAndPoznan implements VotingStrategy {
    @Override
    public VotingResult calculateSympathy(@NotNull MentoringModel mentee, @NotNull MentoringModel mentor) {
        if (mentee.getLocalization().equalsIgnoreCase("Lodz") &&
                !mentor.getLocalization().equalsIgnoreCase("Lodz")) return Rejected.INSTANCE;
        if (mentee.getLocalization().equalsIgnoreCase("Poznan")
                && !mentor.getLocalization().equalsIgnoreCase("Poznan")) return Rejected.INSTANCE;
        return Neutral.INSTANCE;
    }
}
