package gft.mentoring.matching.model;

import lombok.Value;

@Value
class Neutral extends VotingResult {
    private int sympathy = 0;

    @Override
    public int getSympathy() {
        return sympathy;
    }
}
