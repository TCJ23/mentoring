package gft.mentoring.matching;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MentorTest {

 /*   List<Mentee> menteeList = new ArrayList<>();
    List<Mentor> mentorList = new ArrayList<>();
    int countDevMnt = 0;
    int countNoneDevMnt = 0;


    @BeforeEach
    public void setMenteeList() {
        Mentee mentee = new Mentee(1, 1, Family.DEVELOPMENT);
        menteeList.add(mentee);
        Mentee mentee2 = new Mentee(2, 2, Family.ARCHITECTURE);
        menteeList.add(mentee2);
        Mentee mentee3 = new Mentee(3, 3, Family.DATA);
        menteeList.add(mentee3);
        Mentee mentee4 = new Mentee(4, 2, Family.DIGITAL);
        menteeList.add(mentee4);
        Mentee mentee5 = new Mentee(5, 3, Family.DEVELOPMENT);
        menteeList.add(mentee5);
        Mentee mentee6 = new Mentee(6, 1, Family.OTHER);
    }

    @BeforeEach
    public void setMentorList() {
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

    @BeforeEach
    public void countFamilyMentees() {
        for (Mentee mnt : menteeList
                ) {
            if (mnt.checkIfDev()) {
                countDevMnt++;
            } else if (!mnt.checkIfDev()) {
                countNoneDevMnt++;
            }
        }
    }

    @Test
    void getMenteesForDevelopmentMenteesShouldFind5() {
        int menteesNumber = mentorList.get(3).getMentees(menteeList).size();
        assertEquals(countDevMnt, menteesNumber);
    }

    @Test
    public void getAllDevelopmentMenteesForDevMentors() {
        for (Mentor mtr : mentorList
                ) {
            if (mtr.checkIfDev()) {
                int mentorsAmount = mtr.getMentees(menteeList).size();
                Assert.assertEquals(countDevMnt, mentorsAmount);
            }
        }
    }

    @Test
    public void getAllNoneDevelopmentMenteesForNoneDevMentors() {
        for (Mentor mtr : mentorList
                ) {
            if (!mtr.checkIfDev()) {
                int mentorsAmount = mtr.getMentees(menteeList).size();
                Assert.assertEquals(countNoneDevMnt, mentorsAmount);
            }
        }
    }*/
}