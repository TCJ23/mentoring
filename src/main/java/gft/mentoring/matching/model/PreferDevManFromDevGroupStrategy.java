package gft.mentoring.matching.model;

public class PreferDevManFromDevGroupStrategy implements VotingStrategy {
    /*this method*/
    @Override
    public int calculateSympathy(MentoringModel mentee, MentoringModel mentor) {
        if (isDevelopmentGroup(mentee.getFamily()) && isDevelopmentGroup(mentor.getFamily()))
            return 50;

        return 0;
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
