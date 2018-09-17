package gft.mentoring.matching.model;

public class PreferDevManFromSameJobFamily implements VotingStrategy{
    @Override
    public int calculateSympathy(MentoringModel mentee, MentoringModel mentor) {
        if (mentee.getFamily().equals(mentor.getFamily()))
        return 100;

        return 0;
    }
}
