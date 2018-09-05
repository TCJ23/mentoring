package gft.mentoring.matching;

import java.util.ArrayList;
import java.util.List;

public class Mentee {
    private long id;
    private int level;
    Family family;
    private Mentor devman;
    private boolean isDev;

    public boolean checkIfDev() {
        switch (family) {
            case DIGITAL:
                return isDev = true;
            case DEVELOPMENT:
                return isDev = true;
            case ARCHITECTURE:
                return isDev = true;
            case DATA:
                return isDev = true;
            default:
                return false;
        }
    }

    protected List<Mentor> getMentors(List<Mentor> mentorList) {
        List<Mentor> candidates = new ArrayList<>();
        for (Mentor mtr : mentorList
                ) {
            if (family.toString().equalsIgnoreCase(String.valueOf(mtr.family))) {
                candidates.add((Mentor) mtr.clone());
            } else if (checkIfDev() && mtr.checkIfDev()) {
                candidates.add((Mentor) mtr.clone());
            }
        }
        return candidates;
    }

    public Mentee(long id, Family family, int level) {
        this.id = id;
        this.family = family;
        this.level = level;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
