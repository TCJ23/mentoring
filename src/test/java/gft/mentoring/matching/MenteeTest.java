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


    //Given
    @BeforeClass
    public static void setup() {
        MenteeTest menteeTest = new MenteeTest();
        menteeTest.setMentorList();
        menteeTest.setMenteeList();
    }

    //When
    @Test
    public void getMentorsForDevMenteeShouldFind4() {
        int mentorsNumber = menteeList.get(0).getMentors(mentorList).size();
        //Then
        assertEquals(4, mentorsNumber);
        System.out.println("In our data set we have 4 potential mentors for any mentee from Development Family");
    }

    private void setMenteeList() {
        menteeList = new ArrayList<>();
        System.out.println("We create 6 GFT mentees, 4 of them are part of larger development family");
        Mentee mentee = new Mentee(1, Family.DEVELOPMENT, 1);
        menteeList.add(mentee);
        Mentee mentee2 = new Mentee(2, Family.ARCHITECTURE, 2);
        menteeList.add(mentee2);
        Mentee mentee3 = new Mentee(3, Family.DATA, 3);
        menteeList.add(mentee3);
        Mentee mentee4 = new Mentee(4, Family.DIGITAL, 2);
        menteeList.add(mentee4);
        Mentee mentee5 = new Mentee(5, Family.OTHER, 3);
        menteeList.add(mentee5);
        Mentee mentee6 = new Mentee(6, Family.OTHER, 1);
        menteeList.add(mentee6);
    }

    private void setMentorList() {
        mentorList = new ArrayList<>();
        System.out.println("We create 6 GFT mentors, 4 of them are part of larger development family");
        Mentor mentor = new Mentor(1, 4, 6, Family.DEVELOPMENT);
        mentorList.add(mentor);
        Mentor mentor2 = new Mentor(2, 5, 6, Family.ARCHITECTURE);
        mentorList.add(mentor2);
        Mentor mentor3 = new Mentor(3, 6, 6, Family.DATA);
        mentorList.add(mentor3);
        Mentor mentor4 = new Mentor(4, 5, 6, Family.DIGITAL);
        mentorList.add(mentor4);
        Mentor mentor5 = new Mentor(5, 6, 6, Family.OTHER);
        mentorList.add(mentor5);
        Mentor mentor6 = new Mentor(6, 5, 6, Family.OTHER);
        mentorList.add(mentor6);
    }
}