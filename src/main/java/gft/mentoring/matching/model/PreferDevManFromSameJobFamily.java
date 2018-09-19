package gft.mentoring.matching.model;

public class PreferDevManFromSameJobFamily implements VotingStrategy{
    @Override
    public VotingResult calculateSympathy(MentoringModel mentee, MentoringModel mentor) {
        if (mentee.getFamily().equals(mentor.getFamily()))
        return new Support();

        return new Neutral();
    }
}
