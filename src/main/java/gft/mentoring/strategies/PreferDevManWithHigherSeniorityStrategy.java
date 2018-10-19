package gft.mentoring.strategies;

import gft.mentoring.*;
import lombok.val;
import org.jetbrains.annotations.NotNull;

/**
 * @author tzje
 * @implNote you will notice some hardcoded values, in this place we are exlpainig why?
 * Assumption is that Mentee always prefers Mentor with higher Seniority
 */
public class PreferDevManWithHigherSeniorityStrategy implements VotingStrategy {
    /**
     * @param mentor - we assumed that 10 years in GFT is maximum years one person can work for taking GFT Poland
     *               experience as live example
     * seniority is counted in days
     * @see MentoringModel#getSeniority()
     * given that amount of days per year divided by this boundary value of 10 years we will return result of points
     * mulitplied by 100 per year using Support type of VotingResult
     * @see Support#getSympathy() */

    @Override
    public VotingResult calculateSympathy(@NotNull MentoringModel mentee, @NotNull MentoringModel mentor) {
        val points = (int) Math.round(mentor.getSeniority() / 365.0 / 10 * 100);
        return new Support(points);
    }
}
