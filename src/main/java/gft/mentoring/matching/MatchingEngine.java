package gft.mentoring.matching;

/*
* @author tzje
* Context:
In GFT Career Model we have Job Families some of them are considered as one technical perspective.

Problem:
Completely separation of Job Families in context of creating Development management structure creates poorly
balanced(triaged) development career tree. Consequently some Mentors in small families i.e L5 DevMan will only have 1-2
Mentees, in big family like Development which consists of several other Families i.e L5 DevMan will have even 5 Mentees.

Solution:
Let's assume that families (thereafter Development Group):

Project Development
Architecture
Digital
Data
are cross functional in term of choosing DevMan and MentoringModel so that person from from Project Development Family
will not be constantly overburden by allowing Digital, Architecture & Data - Mentors to be assigned Mentees from above 4
Families altogether, considered as one common other Development Group.
------------------------------------------------------------------------------------------------------------------------
Matching Engine should assure that from mentoring candidates, mentoring proposals for mentee in Development Group
consist only of Mentors from same wide Development Group.
*/


import gft.mentoring.matching.model.MentoringModel;
import gft.mentoring.matching.model.PreferDevManFromDevGroupStrategy;
import gft.mentoring.matching.model.PreferDevManFromSameJobFamily;
import gft.mentoring.matching.model.VotingStrategy;
import lombok.AllArgsConstructor;
import lombok.val;

import java.util.Arrays;
import java.util.stream.Stream;

class MatchingEngine {

    static VotingStrategy[] strategies = {new PreferDevManFromDevGroupStrategy(), new PreferDevManFromSameJobFamily()};

    Stream<MentoringModel> findProposalsStream(MentoringModel mentee, MentoringModel... candidates) {
        return Arrays.stream(candidates)
                .map(it -> new MyTuple(it, sympathy(mentee, it)))
                .filter(it -> it.sympathy > 0)
                .sorted((v1, v2) -> -(v1.sympathy - v2.sympathy))
                .map(it -> it.mentor);

    }

    @AllArgsConstructor
    static class MyTuple {
        private MentoringModel mentor;
        private int sympathy;
    }

    static int sympathy(MentoringModel mentee, MentoringModel mentor) {
        int sympathy = -1;
        for (val strategy : strategies) {
            sympathy += strategy.calculateSympathy(mentee, mentor);
        }
        return sympathy;
    }
}