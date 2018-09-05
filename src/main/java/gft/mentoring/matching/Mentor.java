package gft.mentoring.matching;

import java.util.ArrayList;
import java.util.List;

public class Mentor {
    private long id;
    private int level;
    private int rating;
    Family family;
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

    protected List<Mentee> getMentees(List<Mentee> menteeList) {
      /*  return mentees.stream().
                map(mentee -> (Mentee)mentee.clone()).collect(Collectors.toList());*/
        List<Mentee> mentosy = new ArrayList<>();
        for (Mentee mnt : menteeList
                ) {
            if (family.toString().equalsIgnoreCase(String.valueOf(mnt.family))) {
                mentosy.add((Mentee) mnt.clone());
            } else if (checkIfDev() && mnt.checkIfDev()) {
                mentosy.add((Mentee) mnt.clone());
            }
        }
        return mentosy;
    }

    public Mentor(long id, int level, int rating, gft.mentoring.matching.Family family) {
        this.id = id;
        this.level = level;
        this.rating = rating;
        this.family = family;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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