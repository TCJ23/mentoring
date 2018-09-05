package gft.mentoring.matching;

import java.util.ArrayList;
import java.util.List;

public class MatchingEngine {

    /*GENERATE SAMPLE DATA*/
    public static void main(String[] args) {

        List<Mentee> menteeList = new ArrayList<>();
        List<Mentor> mentorList = new ArrayList<>();

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

        System.out.println("Aktualna lista mentisów ");
        for (Mentee mnt : menteeList
                ) {
            System.out.println(mnt.toString());
        }
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

