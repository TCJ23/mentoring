package gft.mentoring.strategies;

import gft.mentoring.*;
import lombok.val;
import org.jetbrains.annotations.NotNull;

/**
 * @author tzje
 * @implNote you will notice some hardcoded values, in this place we are exlpainig why?
 */
public class PreferDevManThatIsOlderThan30YearsOldStrategy implements VotingStrategy {
    /**
     * @param mentor - we assumed that Mentor that is 30 years old is prefered as such Mentor is always more experienced
     * @see MentoringModel#getAge()
     * given that valuable age of Mentor starts at 30 let's divide by this boundary value and return result times 25 supporting
     * points per mentee using Support type of VotingResult
     * 25 because person would have to be 120 years old to throw
     * IllegalArgumentException "Must be between 1 and 100 inclusive - too high""
     * @see Support#getSympathy()
     */

    @Override
    public VotingResult calculateSympathy(@NotNull MentoringModel mentee, @NotNull MentoringModel mentor) {

        if (mentor.getAge() > 30) {
            val points = (mentor.getAge() / 30.0) * 25;
            return new Support((int) points);
        }
        return Neutral.INSTANCE;
    }
}
