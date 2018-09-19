package gft.mentoring.matching.model;

import lombok.Value;

@Value
class Rejected extends VotingResult {
    private int sympathy = -1;

    @Override
    public int getSympathy() {
        return sympathy;
    }
}
