package gft.mentoring.matching;

import java.util.ArrayList;
import java.util.List;

public class Mentee extends GFTEmployee {
    private Mentor devman;

    protected List<Mentor> findProposals(Mentor... mentorList) {
        List<Mentor> candidates = new ArrayList<>();
        for (Mentor mtr : mentorList
                ) {
             if (checkIfDev() && mtr.checkIfDev()) {
                candidates.add(mtr);
            }
        }
        return candidates;
    }
    // ASK Sławek!
   /* protected Iterator<Mentor> getMentorsIT(List<Mentor> mentorList) {
        List<Mentor> candidates = new ArrayList<>();
        for (Mentor mtr : mentorList
                ) {
            if (family.toString().equalsIgnoreCase(String.valueOf(mtr.family))) {
                candidates.add((Mentor) mtr.clone());
            } else if (checkIfDev() && mtr.checkIfDev()) {
                candidates.add((Mentor) mtr.clone());
            }
        }
        return candidates.iterator();
    }*/

    public Mentee(long id, int level, Family family) {
        this.id = id;
        this.family = family;
        this.level = level;
    }
}
