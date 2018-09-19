package gft.mentoring.matching.voting.strategy;

import gft.mentoring.matching.model.MentoringModel;
import gft.mentoring.matching.voting.result.Neutral;
import gft.mentoring.matching.voting.result.Support;
import gft.mentoring.matching.voting.result.VotingResult;

public class PreferDevManFromSameJobFamily implements VotingStrategy {
    @Override
    public VotingResult calculateSympathy(MentoringModel mentee, MentoringModel mentor) {
        if (mentee.getFamily().equals(mentor.getFamily()))
            return new Support(100);

        return Neutral.INSTANCE;
    }
}
