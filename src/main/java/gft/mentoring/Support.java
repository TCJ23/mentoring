package gft.mentoring;

/**
 * Represents sympathy level from 1 to 100
 */
public class Support extends VotingResult {

    private int sympathy;

    public Support(int sympathy) {
        if (sympathy < 1) throw new IllegalArgumentException("Must be between 1 and 100 inclusive - too low");
        if (sympathy > 100) throw new IllegalArgumentException("Must be between 1 and 100 inclusive - too high");

        this.sympathy = sympathy;
    }

    public int getSympathy() {
        return sympathy;
    }
}
