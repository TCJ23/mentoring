package gft.mentoring.matching;

import gft.mentoring.matching.model.Family;
import gft.mentoring.matching.model.MentoringModel;
import lombok.Value;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/*
@author tzje
* This class should test MatchingEngine().findProposals
*                   test MatchingEngine().findBestCandidate
* for MentoringModel in
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
        val proposal = new MentoringModel(1, singleMatchingParam.mentorCandidateFamily);
        val mentee = new MentoringModel(1, singleMatchingParam.menteeFamily);
        //when
        val candidate = new MatchingEngine().findProposalsStream(mentee, proposal);
        //then
        assertThat(candidate.count() == 1).isEqualTo(singleMatchingParam.accepted);
//        assertThat(candidate.size() == 1).isEqualTo(singleMatchingParam.accepted);
    }

    static Stream<SingleMatchingParam> singleMatchingParam() {
        return Stream.of(
                new SingleMatchingParam("Scenario: MentoringModel & MentoringModel ARE BOTH in Development Group",
                        Family.PROJECT_DEVELOPMENT, Family.PROJECT_DEVELOPMENT, true),
                new SingleMatchingParam("Scenario: MentoringModel & MentoringModel: ONLY ONE IS in Development Group",
                        Family.ARCHITECTURE, Family.CORPORATE_SERVICES, false),
                new SingleMatchingParam("Scenario: MentoringModel & MentoringModel ONLY ONE IS in Development Group",
                        Family.DIGITAL, Family.AMS, false),
                new SingleMatchingParam("Scenario: MentoringModel & MentoringModel ARE BOTH in Development Group",
                        Family.DATA, Family.ARCHITECTURE, true),
                new SingleMatchingParam("Scenario: MentoringModel & MentoringModel NEITHER IS in Development Group",
                        Family.AMS, Family.BUSINESS_CONSULTING, false));
    }

    /*This test if to meet requirement 1.2 in REQUIREMENTS.md*/
    @Test
    @DisplayName("STREAM -> From 2 Mentors prefers MentoringModel from exact same Family as MentoringModel")
    void findPreferedCandidateFromManyMentorsStream() {
        //given
        MentoringModel mentee = new MentoringModel(1, Family.DATA);
        MentoringModel[] candidates = {new MentoringModel(1, Family.ARCHITECTURE), new MentoringModel(2, Family.DATA)};
        MatchingEngine matchingEngine = new MatchingEngine();
        //when
        Stream<MentoringModel> bestMentorCandidate = matchingEngine.findProposalsStream(mentee, candidates);

        //then
        assertThat(bestMentorCandidate.limit(1))
                .containsExactly(new MentoringModel(2, Family.DATA));
    }

    @Test
    @DisplayName("From 2 Mentors find MentoringModel from exact same Family as MentoringModel")
    void findBestCandidateFromManyMentors() {
        //given
        MentoringModel mentee = new MentoringModel(1, Family.DATA);
        MatchingEngine matchingEngine = new MatchingEngine();
        //when
        Stream<MentoringModel> bestCandidate = matchingEngine.findProposalsStream(mentee, new MentoringModel(1, Family.ARCHITECTURE),
                new MentoringModel(2, Family.DATA));
        //then
        assertThat(bestCandidate.findFirst().get().getFamily()).isEqualTo(mentee.getFamily());
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
