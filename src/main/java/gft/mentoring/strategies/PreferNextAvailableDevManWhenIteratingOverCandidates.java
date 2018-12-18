package gft.mentoring.strategies;

import gft.mentoring.*;
import lombok.val;
import org.jetbrains.annotations.NotNull;

/**
 * @author tzje
 * @implNote you will notice some hardcoded values, in this place we are exlpainig why?
 * Assumption is that Mentee always prefers Mentor with less Mentees currently assigned
 */
public class PreferNextAvailableDevManWhenIteratingOverCandidates implements VotingStrategy {
    /**
     * @param mentor - we assumed that 5 mentees for 1 mentor in GFT is maximum one person can manage for taking GFT Poland
     *               experience as live example
     *               seniority is counted in days
     * @see MentoringModel#getSeniority()
     * given that max amount of mentees is 5 let's divide by this boundary value and return result times 25 supporting
     * points per mentee using Support type of VotingResult
     * @see Support#getSympathy()
     */

    @Override
    public VotingResult calculateSympathy(@NotNull MentoringModel mentee, @NotNull MentoringModel mentor) {
        /**
         * we will use linear equation approach
         * if Mentor has 5 mentees he should score 0 points in Supporting Voting Result
         * if Mentor has 0 mentees he should score 100 points in Supporting Voting Result
         * because we want to keep original amount of mentees in print out for Business Owner
         * @weronika when we increment through proposals
         * @see MatchingEngine#findProposals(MentoringModel, MentoringModel...)
         * we are faking count based on new field
         * @see MentoringModel#newMenteesAssigned
         * */

        val points = (-20 * mentor.getNewMenteesAssigned()) + 100;
        if (points <= 0) {
            return Neutral.INSTANCE;
        }
        return new Support(points);
    }
}