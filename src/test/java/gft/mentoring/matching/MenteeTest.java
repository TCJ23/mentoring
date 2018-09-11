package gft.mentoring.matching;

import lombok.Value;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MenteeTest {
/*
    //Given
    private static List<Mentee> menteeList;
    private static List<Mentor> mentorList;
    private static int countDevMtr = 0;
    private static int countNoneDevMtr = 0;


    //Given
    @BeforeClass
    public static void setup() {
        MenteeTest menteeTest = new MenteeTest();
        menteeTest.setMentorList();
        menteeTest.setMenteeList();
        menteeTest.countFamilyMentors();
    }

    //When
    @Test
    public void getMentorsForDevelopmentMentorsShouldFind4() {
        int mentorsNumber = menteeList.get(0).findProposals(mentorList).size();
        //Then
        assertEquals(countDevMtr, mentorsNumber);
    }

    @Test
    public void shouldFindValidProposalsForDevelopmentGroupMentee() {
        for (Mentee mnt : menteeList
                ) {
            if (mnt.checkIfDev()) {
                int mentorsAmount = mnt.findProposals(mentorList).size();
                //Then
                assertEquals(countDevMtr, mentorsAmount);
            }
        }
    }

    *//*@Test
    public void shouldFindValidProposalsForNonDevelopmentGroupMentee() {
        for (Mentee mnt : menteeList
                ) {
            if (!mnt.checkIfDev()) {
                int mentorsAmount = mnt.findProposals(mentorList).size();
                //Then
                assertEquals(countNoneDevMtr, mentorsAmount);
            }
        }
        System.out.println("We should find 2 mentors that are not part of Development Family.");
    }

*//*
  *//*  @Parameterized.Parameters
    public static Collection<SingleMatchingParam> singleMatchingParams() {
        return Arrays.asList(
                new SingleMatchingParam(Family.DEVELOPMENT, Family.DEVELOPMENT, true),
                new SingleMatchingParam(Family.DATA, Family.ARCHITECTURE, true),
                new SingleMatchingParam(Family.OTHER, Family.DEVELOPMENT, false)
        );
    }

    @Test
    public void testMatchingParams() {

    }*//*

    @Value
    static class SingleMatchingParam {

        private Family menteeFamily;
        private Family mentorCandidateFamily;
        private boolean accepted;
    }

    //    @Parameterized.Parameters
    private void setMenteeList() {
        menteeList = new ArrayList<>();
        System.out.println("We created 6 GFT mentees, 4 of them are part of larger development family");
        Mentee mentee = new Mentee(1, 1, Family.DEVELOPMENT);
        menteeList.add(mentee);
        Mentee mentee2 = new Mentee(2, 2, Family.ARCHITECTURE);
        menteeList.add(mentee2);
        Mentee mentee3 = new Mentee(3, 3, Family.DATA);
        menteeList.add(mentee3);
        Mentee mentee4 = new Mentee(4, 2, Family.DIGITAL);
        menteeList.add(mentee4);
        Mentee mentee5 = new Mentee(5, 3, Family.OTHER);
        menteeList.add(mentee5);
        Mentee mentee6 = new Mentee(6, 1, Family.OTHER);
    }

    private void setMentorList() {
        mentorList = new ArrayList<>();
        System.out.println("We created 6 GFT mentors, 4 of them are part of larger development family");
        Mentor mentor = new Mentor(1, 4, Family.DEVELOPMENT, 6);
        mentorList.add(mentor);
        Mentor mentor2 = new Mentor(2, 5, Family.ARCHITECTURE, 4);
        mentorList.add(mentor2);
        Mentor mentor3 = new Mentor(3, 6, Family.DATA, 6);
        mentorList.add(mentor3);
        Mentor mentor4 = new Mentor(4, 5, Family.DIGITAL, 6);
        mentorList.add(mentor4);
        Mentor mentor5 = new Mentor(5, 6, Family.OTHER, 3);
        mentorList.add(mentor5);
        Mentor mentor6 = new Mentor(6, 5, Family.OTHER, 2);
        mentorList.add(mentor6);
    }

    private void countFamilyMentors() {
        for (Mentor mtr : mentorList
                ) {
            if (mtr.checkIfDev()) {
                countDevMtr++;
            } else if (!mtr.checkIfDev()) {
                countNoneDevMtr++;
            }
        }
    }*/
}