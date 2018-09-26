package gft.mentoring;

import lombok.Value;

@Value
public class Rejected extends VotingResult {
    public static  final Rejected INSTANCE = new Rejected();

    private Rejected() {
    }

}
