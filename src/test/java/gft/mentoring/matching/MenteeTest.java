package gft.mentoring.matching;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MenteeTest {
    //Given
    private static List<Mentee> menteeList;
    private static List<Mentor> mentorList;
    private static int countMtr = 0;


    //Given
    @BeforeClass
    public static void setup() {
        MenteeTest menteeTest = new MenteeTest();
        menteeTest.setMentorList();
        menteeTest.setMenteeList();
        menteeTest.countDevFamMtr();
    }

    //When
    @Test
    public void getMentorsForDevelopmentMenteeShouldFind4() {
        int mentorsNumber = menteeList.get(0).getMentors(mentorList).size();
        //Then
        assertEquals(countMtr, mentorsNumber);
        System.out.println("In our data set we have 4 potential mentors for any mentee from Development Family");
    }

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

    private void countDevFamMtr() {
        for (Mentor mtr : mentorList
                ) {
            if (mtr.checkIfDev()) {
                countMtr++;
            }
        }
    }
}