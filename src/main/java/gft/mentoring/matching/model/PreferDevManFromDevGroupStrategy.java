package gft.mentoring.matching.model;

public class PreferDevManFromDevGroupStrategy implements VotingStrategy {
    /*this method*/
    @Override
    public VotingResult calculateSympathy(MentoringModel mentee, MentoringModel mentor) {
        if (isDevelopmentGroup(mentee.getFamily()) && isDevelopmentGroup(mentor.getFamily()))
            return new Support();

        return new Neutral();
    }

    public boolean isDevelopmentGroup(Family family) {
        switch (family) {
            case DIGITAL:
            case PROJECT_DEVELOPMENT:
            case ARCHITECTURE:
            case DATA:
                return true;
            default:
                return false;
        }
    }
}
