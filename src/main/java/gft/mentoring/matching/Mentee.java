package gft.mentoring.matching;

import java.util.ArrayList;
import java.util.List;

public class Mentee {
    private long id;
    private int level;
    Family family;
    private Mentor devman;
    private List<Mentor> mtrCandidates;

    List<Mentor> getMentors(List<Mentor> mentorList, Mentee mnt) {
        ArrayList<Mentor> candidates = new ArrayList<>();
        for (Mentor mtr : mentorList
                ) {
            if (mnt.family.toString().equalsIgnoreCase(String.valueOf(mtr.family))) {
                candidates.add(mtr);
            }
        }
        return candidates;
    }

    public Mentee(long id, Family family, int level) {
        this.id = id;
        this.family = family;
        this.level = level;
    }

    public Mentor getDevman() {
        return devman;
    }

    public void setDevman(Mentor devman) {
        this.devman = devman;
    }

    @Override
    public String toString() {
        return "Mentee{" +
                "id=" + id +
                ", level=" + level +
                ", family=" + family +
//                ", devman=" + devman +
                '}';
    }

    @Override
    protected Object clone() {
        Mentee klon = new Mentee(this.id, this.family, this.level);
        return klon;
    }
}
