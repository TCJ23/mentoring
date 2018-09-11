package gft.mentoring.matching;

import lombok.Value;

@Value
public class Mentor {
    private long id;
    private int level;
    private Family family;
}