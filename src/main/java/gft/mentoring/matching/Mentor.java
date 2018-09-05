package gft.mentoring.matching;

import java.util.ArrayList;
import java.util.List;

public class Mentor {
    private long id;
    private int level;
    private int rating;
    Family family;
    private List<Mentee> mentees;

    public Mentor(long id, int level, int rating, gft.mentoring.matching.Family family) {
        this.id = id;
        this.level = level;
        this.rating = rating;
        this.family = family;
    }

    public List<Mentee> getMentees() {
      /*  return mentees.stream().
                map(mentee -> (Mentee)mentee.clone()).collect(Collectors.toList());*/
        List<Mentee> myMnts = new ArrayList<>();
        for (Mentee mnt : this.mentees
                ) {
            myMnts.add((Mentee) mnt.clone());
        }
        return myMnts;
    }

    public void setMentees(List<Mentee> mentees) {
        this.mentees = mentees;
    }

    @Override
    public String toString() {
        return "Mentor{" +
                "id=" + id +
                ", level=" + level +
                ", rating=" + rating +
                ", Family='" + family + '\'' +
                '}';
    }

    @Override
    protected Object clone() {
        Mentor klon = new Mentor(this.id = id, this.level = level, this.rating = rating, this.family = family);
        return klon;
    }
}