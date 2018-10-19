package gft.mentoring.strategies;

import gft.mentoring.*;
import org.jetbrains.annotations.NotNull;

public class RejectDevManWithLimitOfMenteesAssignedStrategy implements VotingStrategy {
    @Override
    public VotingResult calculateSympathy(@NotNull MentoringModel mentee, @NotNull MentoringModel mentor) {
        if (mentor.getLevel() == 4 && mentor.getMenteesAssigned() >= 2) return Rejected.INSTANCE;
        if (mentor.getLevel() == 5 && mentor.getMenteesAssigned() >= 3) return Rejected.INSTANCE;
        if (mentor.getLevel() == 6 && mentor.getMenteesAssigned() >= 4) return Rejected.INSTANCE;
        if (mentor.getLevel() == 7 && mentor.getMenteesAssigned() >= 5) return Rejected.INSTANCE;
        return Neutral.INSTANCE;
    }
}
