package gft.mentoring.matching.model;

/*@author tzje
 * this interface should be implemented for correct sympathy rate accordingly to voting strategy*/
@FunctionalInterface
public interface VotingStrategy {
    /*this method calculates sympathy between mentee and corresponding mentor
     * return value should be between -1 and 100
     * -1 means that we have veto
     * 0 means that sympathy remains neutral
     * 1 - 100 returns level of sympathy where 100 is max value*/
    int calculateSympathy(MentoringModel mentee, MentoringModel mentor);
}
