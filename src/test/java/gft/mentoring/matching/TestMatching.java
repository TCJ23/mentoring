package gft.mentoring.matching;

import gft.mentoring.matching.model.Family;
import gft.mentoring.matching.model.Mentee;
import gft.mentoring.matching.model.Mentor;
import lombok.Value;
import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

/*
@author tzje
* This class should test MatchingEngine().findProposals
* for Mentee in
Project Development
Architecture
Digital
Data
we can assign Mentors from above Families treated as one*/

public class TestMatching {

    /* in @ParameterizedTest you either keep
    your testmethod name and method source name the same or use parameters as below*/

    @ParameterizedTest(name = "{0}")
    @MethodSource("singleMatchingParam")
    @DisplayName("Check if MatchingEngine proposes mentors correctly per Family")
    void findSingleCandidateMentorFromProposals(SingleMatchingParam singleMatchingParam) {
        //given
        val proposal = new Mentor(1, singleMatchingParam.mentorCandidateFamily);
        val mentee = new Mentee(1, singleMatchingParam.menteeFamily);
        //when
        val candidate = new MatchingEngine().findProposals(mentee, proposal);
        //then
        Assertions.assertThat(candidate.count() == 1).isEqualTo(singleMatchingParam.accepted);
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
