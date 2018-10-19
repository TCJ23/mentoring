package gft.mentoring.strategies;

import gft.mentoring.*;
import lombok.val;
import org.jetbrains.annotations.NotNull;

/**
 * @author tzje
 * @implNote you will notice some hardcoded values, in this place we are exlpainig why?
 * Assumption is that Mentee always prefers Mentor with less Mentees currently assigned
 */
public class PreferDevManThatIsOlderThan30YearsOldStrategy implements VotingStrategy {
    /**
     * @param mentor - we assumed that Mentor that is 30 years old is prefered as such Mentor is always more expierenced
     *               than younger colleauges, we take 40 years old as maximum boundary value.
     * @see MentoringModel#getAge()
     * given that maximum age of Mentor is 40 let's divide by this boundary value and return result times 25 supporting
     * points per mentee using Support type of VotingResult
     * @see Support#getSympathy()
     */

    @Override
    public VotingResult calculateSympathy(@NotNull MentoringModel mentee, @NotNull MentoringModel mentor) {

        if (mentor.getAge() > 30) {
            val points = (mentor.getAge() / 40) * 100;
            return new Support(points);
        }
        return Neutral.INSTANCE;
    }
}
