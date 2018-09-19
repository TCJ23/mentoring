package gft.mentoring;

import lombok.Value;

@Value
public class Rejected extends VotingResult {
    private int sympathy = -1;

    public int getSympathy() {
        return sympathy;
    }
}
