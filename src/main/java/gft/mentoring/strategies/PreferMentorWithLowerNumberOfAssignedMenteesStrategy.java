package gft.mentoring.strategies;

import gft.mentoring.MentoringModel;
import gft.mentoring.Support;
import gft.mentoring.VotingResult;
import gft.mentoring.VotingStrategy;
import lombok.val;
import org.jetbrains.annotations.NotNull;

/**
 * @author tzje
 * @implNote you will notice some hardcoded values, in this place we are exlpainig why?
 * Assumption is that Mentee always prefers Mentor with less Mentees currently assigned
 */
public class PreferMentorWithLowerNumberOfAssignedMenteesStrategy implements VotingStrategy {
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
        val points = mentor.getMenteesAssigned() / 5 * 25;
        return new Support(points);
    }
}
