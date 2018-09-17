package gft.mentoring.matching.model;

import lombok.Value;

/*@author tzje
 * this interface should be implemented for correct sympathy rate accordingly to voting strategy*/
public interface VotingStrategy {
    /*this method calculates sympathy between mentee and corresponding mentor
     * return value should be between -1 and 100
     * -1 means that we have veto
     * 0 means that sympathy remains neutral
     * 1 - 100 returns level of sympathy where 100 is max value*/
    VotingResult calculateSympathy(MentoringModel mentee, MentoringModel mentor);
}

abstract class VotingResult { }

@Value
class Rejected extends VotingResult { }

@Value
class Neutral extends VotingResult { }

@Value
class Support extends VotingResult {
    private int value;
}

