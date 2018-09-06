package gft.mentoring.matching;

import java.util.ArrayList;
import java.util.List;

public class MatchingEngine {

    /*GENERATE SAMPLE DATA*/
    public static void main(String[] args) {

        List<Mentee> menteeList = new ArrayList<>();
        List<Mentor> mentorList = new ArrayList<>();

        Mentee mentee = new Mentee(1,  1,Family.DEVELOPMENT);
        menteeList.add(mentee);
        Mentee mentee2 = new Mentee(2,  2,Family.ARCHITECTURE);
        menteeList.add(mentee2);
        Mentee mentee3 = new Mentee(3,  3,Family.DATA);
        menteeList.add(mentee3);
        Mentee mentee4 = new Mentee(4,  2,Family.DIGITAL);
        menteeList.add(mentee4);
        Mentee mentee5 = new Mentee(5,  3,Family.OTHER);
        menteeList.add(mentee5);
        Mentee mentee6 = new Mentee(6,  1,Family.OTHER);
        menteeList.add(mentee6);

        System.out.println("Aktualna lista mentisów ");
        for (Mentee mnt : menteeList
                ) {
            System.out.println(mnt);
        }
        Mentor mentor = new Mentor(1, 4, Family.DEVELOPMENT, 6);
        mentorList.add(mentor);
        Mentor mentor2 = new Mentor(2, 5, Family.ARCHITECTURE, 6);
        mentorList.add(mentor2);
        Mentor mentor3 = new Mentor(3, 6, Family.DATA,6);
        mentorList.add(mentor3);
        Mentor mentor4 = new Mentor(4, 5, Family.DIGITAL, 6);
        mentorList.add(mentor4);
        Mentor mentor5 = new Mentor(5, 6, Family.OTHER, 6);
        mentorList.add(mentor5);
        Mentor mentor6 = new Mentor(6, 5, Family.OTHER, 6);
        mentorList.add(mentor6);

        System.out.println("Aktualna lista mentorów ");
        mentorList.forEach(System.out::println);

        List<Mentor> mnt0 = mentee.getMentors(mentorList);
        System.out.println("For mentee #" + mentee.getId() + " I have found " + mnt0.size() + " mentors");
        List<Mentor> mnt1 = mentee2.getMentors(mentorList);
        System.out.println("For mentee #" + mentee2.getId() + " I have found " + mnt1.size() + " mentors");
        List<Mentee> mtr6 = mentor6.getMentees(menteeList);
        System.out.println("For mentor #" + mentor6.getId() + " I have found " + mtr6.size() + " mentees");
        List<Mentee> mtr4 = mentor4.getMentees(menteeList);
        System.out.println("For mentor #" + mentor4.getId() + " I have found " + mtr4.size() + " mentees");

        System.out.println(menteeList.get(0).getMentors(mentorList).size());
    }
}

