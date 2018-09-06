package gft.mentoring.matching;

import java.util.ArrayList;
import java.util.List;

public class Mentor extends GFTEmployee {
    private int rating;


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

    public Mentor(long id, int level, gft.mentoring.matching.Family family, int rating) {
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
        Mentor klon = new Mentor(this.id = id, this.level = level, this.family = family, this.rating = rating);
        return klon;
    }
}