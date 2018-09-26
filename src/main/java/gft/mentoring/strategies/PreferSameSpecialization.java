package gft.mentoring.strategies;

import gft.mentoring.*;

import java.util.Objects;

/*1.6. Mentor prefers the same specialisation as his mentee
  @author(Sławomir Siudek)
  Matching process prefers the same specialisation for Mentor and Mentee.
  Different specialisation is not an obstacle in setting Mentor-Mentee relation, it is only
  not preferred.*/
public class PreferSameSpecialization implements VotingStrategy {

    @Override
    public VotingResult calculateSympathy(MentoringModel mentee, MentoringModel mentor) {

        if (!mentee.getFamily().equals(mentor.getFamily())) return Rejected.INSTANCE;
        if (Objects.equals(mentee.getSpecialization(), mentor.getSpecialization())) return new Support(25);

        return Neutral.INSTANCE;
    }
}
