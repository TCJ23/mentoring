package gft.mentoring.strategies;

import gft.mentoring.*;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/*1.6. Mentor prefers the same specialisation as his mentee
  @author(Sławomir Siudek)
  Matching process prefers the same specialisation for Mentor and Mentee.
  Different specialisation is not an obstacle in setting Mentor-Mentee relation, it is only
  not preferred.*/
public class PreferDevManFromSameSpecializationStrategy implements VotingStrategy {

    @Override
    public VotingResult calculateSympathy(@NotNull MentoringModel mentee, @NotNull MentoringModel mentor) {

        if (Objects.equals(mentee.getSpecialization(), mentor.getSpecialization())) return new Support(25);

        return Neutral.INSTANCE;
    }
}
