package gft.mentoring.matching;

import gft.mentoring.matching.model.Family;
import gft.mentoring.matching.model.Mentee;
import gft.mentoring.matching.model.Mentor;
import lombok.Value;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/*
@author tzje
* This class should test MatchingEngine().findProposals
*                   test MatchingEngine().findBestCandidate
* for Mentee in
Project Development
Architecture
Digital
Data
we can assign Mentors from above Families treated as one*/
@DisplayName("Main Class to test MatchingEngine")
public class TestMatching {


    /*This test if to meet requirement 1.1 in REQUIREMENTS.md
    in @ParameterizedTest you either keep
    your testmethod name and method source name the same or use parameters as below*/

    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("singleMatchingParam")
    @DisplayName("Check if MatchingEngine proposes mentors correctly per Family")
    void findSingleCandidateMentorFromProposals(SingleMatchingParam singleMatchingParam) {
        //given
        val proposal = new Mentor(1, singleMatchingParam.mentorCandidateFamily);
        val mentee = new Mentee(1, singleMatchingParam.menteeFamily);
        //when
        val candidate = new MatchingEngine().findProposals(mentee, proposal);
        //then
//        assertThat(candidate.count() == 1).isEqualTo(singleMatchingParam.accepted);
        assertThat(candidate.size() == 1).isEqualTo(singleMatchingParam.accepted);
    }

    static Stream<SingleMatchingParam> singleMatchingParam() {
        return Stream.of(
                new SingleMatchingParam("Scenario: Mentor & Mentee ARE BOTH in Development Group",
                        Family.PROJECT_DEVELOPMENT, Family.PROJECT_DEVELOPMENT, true),
                new SingleMatchingParam("Scenario: Mentor & Mentee: ONLY ONE IS in Development Group",
                        Family.ARCHITECTURE, Family.CORPORATE_SERVICES, false),
                new SingleMatchingParam("Scenario: Mentor & Mentee ONLY ONE IS in Development Group",
                        Family.DIGITAL, Family.AMS, false),
                new SingleMatchingParam("Scenario: Mentor & Mentee ARE BOTH in Development Group",
                        Family.DATA, Family.ARCHITECTURE, true),
                new SingleMatchingParam("Scenario: Mentor & Mentee NEITHER IS in Development Group",
                        Family.AMS, Family.BUSINESS_CONSULTING, true));
    }

    /*This test if to meet requirement 1.2 in REQUIREMENTS.md*/
    /*@Test
    void findBestCandidateFromManyMentorsStream() {
        //given
        Mentee mentee = new Mentee(1, Family.DATA);
        Mentor[] proposals = {new Mentor(1, Family.ARCHITECTURE), new Mentor(2, Family.DATA)};
        MatchingEngine matchingEngine = new MatchingEngine();
        //when
        Stream<Mentor> candidates = matchingEngine.findProposals(mentee, proposals);
        //then
        Mentor bestCandidate = candidates.findFirst().orElse(null);
        assertThat(bestCandidate.getFamily()).isEqualTo(mentee.getFamily());
//      Mentor bestCandidate = candidates.findFirst().orElse(new Mentor(999, Family.PROJECT_GOVERNANCE));
    }*/

    @Test
    @DisplayName("From 2 Mentors find Mentor from exact same Family as Mentee")
    void findBestCandidateFromManyMentors() {
        //given
        Mentee mentee = new Mentee(1, Family.DATA);
        Mentor[] proposals = {new Mentor(1, Family.ARCHITECTURE), new Mentor(2, Family.DATA)};
        MatchingEngine matchingEngine = new MatchingEngine();
        //when
//        List<Mentor> candidates = matchingEngine.findProposals(mentee, proposals);
        Mentor bestCandidate = matchingEngine.findBestCandidate(mentee, proposals);
        //then
//        Mentor bestCandidate = candidates.get(0);
        assertThat(bestCandidate.getFamily()).isEqualTo(mentee.getFamily());
//      Mentor bestCandidate = candidates.findFirst().orElse(new Mentor(999, Family.PROJECT_GOVERNANCE));
    }

    @Value
    static class SingleMatchingParam {
        private String scenario;
        private Family menteeFamily;
        private Family mentorCandidateFamily;
        private boolean accepted;

        @Override
        public String toString() {
            return scenario;
        }
    }

}
