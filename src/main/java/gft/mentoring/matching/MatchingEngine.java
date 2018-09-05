package gft.mentoring.matching;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MatchingEngine {

    static List<Mentee> menteeList = new ArrayList<>();
    static List<Mentor> mentorList = new ArrayList<>();
    static List<Mentee> devmans = new ArrayList<>();

    /*GENERATE SAMPLE DATA*/
    public static void main(String[] args) {

        Mentee mentee = new Mentee(1, Family.DEVELOPMENT, 1);
        menteeList.add(mentee);
        Mentee mentee2 = new Mentee(2, Family.ARCHITECTURE, 2);
        menteeList.add(mentee2);
        Mentee mentee3 = new Mentee(3, Family.DATA, 3);
        menteeList.add(mentee3);
        Mentee mentee4 = new Mentee(4, Family.DIGITIAL, 2);
        menteeList.add(mentee4);
        Mentee mentee5 = new Mentee(5, Family.OTHER, 3);
        menteeList.add(mentee5);
        Mentee mentee6 = new Mentee(6, Family.OTHER, 1);
        menteeList.add(mentee6);

        /*menteeList.add(new Mentee(1, Family.DEVELOPMENT, 1));
        menteeList.add(new Mentee(2, Family.ARCHITECTURE, 2));
        menteeList.add(new Mentee(3, Family.DATA, 3));
        menteeList.add(new Mentee(4, Family.DIGITIAL, 2));
        menteeList.add(new Mentee(5, Family.OTHER, 3));
        menteeList.add(new Mentee(6, Family.OTHER, 1));*/

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
        Mentor mentor4 = new Mentor(4, 5, 6, Family.DIGITIAL);
        mentorList.add(mentor4);
        Mentor mentor5 = new Mentor(5, 6, 6, Family.OTHER);
        mentorList.add(mentor5);
        Mentor mentor6 = new Mentor(6, 5, 6, Family.OTHER);
        mentorList.add(mentor6);

        System.out.println("Aktualna lista mentorów ");
        mentorList.forEach(System.out::println);
        System.out.println(mentor2.family);

        System.out.println(Arrays.asList(mentee2.getMentors(mentorList, mentee2)));
        System.out.println(getMentors2(mentorList, mentee3));
    }

    private static List<Mentor> findMentors(List<Mentor> mentorList, Predicate<Mentor> mentorPredicate) {
        return mentorList.stream().filter(mentorPredicate)
                .collect(Collectors.<Mentor>toList());
    }

    private static List<Mentor> getMentors2(List<Mentor> mentorList, Mentee mnt) {
        ArrayList<Mentor> candidates = new ArrayList<>();
        for (Mentor mtr : mentorList
                ) {
            if (mnt.family.toString().equalsIgnoreCase(String.valueOf(mtr.family))) {
                candidates.add(mtr);
            }
        }
        return candidates;
    }

    /*private static List<Mentee> findMentees(List<Mentee> menteeList, Mentor mentor) {
        if (mentor.family)
    }*/
}
