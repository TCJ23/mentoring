package gft.mentoring.matching.model;

import lombok.Value;

@Value
class Support extends VotingResult {
    private int sympathy = 100;

    @Override
    public int getSympathy() {
        return sympathy;
    }
}
