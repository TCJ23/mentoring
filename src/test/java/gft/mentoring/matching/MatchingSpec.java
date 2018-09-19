package gft.mentoring.matching;

import gft.mentoring.matching.model.Family;
import gft.mentoring.matching.model.MentoringModel;
import lombok.Value;
import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Main Class to test MatchingEngine")
public class MatchingSpec {


    /* This test if to meet requirement 1.1 in REQUIREMENTS.md
     in @ParameterizedTest you either keep
     your testmethod name and method source name the same or use parameters as below
     This class should test MatchingEngine().findProposalsStream
     for MentoringModel in
     Project Development
     Architecture Digital
     Data
     we can assign Mentors from above Families treated as one*/
    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("singleMatchingParam")
    @DisplayName("Check if MatchingEngine proposes mentors correctly per Family")
    void findSingleCandidateMentorFromProposals(SingleMatchingParam singleMatchingParam) {
        //given
        val proposal = newMentor().family(singleMatchingParam.mentorCandidateFamily).build();
        val mentee = newMentee().family(singleMatchingParam.menteeFamily).build();
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
        MentoringModel mentee = newMentee().family(Family.DATA).build();
        val mentor1 = newMentor().family(Family.ARCHITECTURE).build();
        val mentor2 = newMentor().family(Family.DATA).build();
        MatchingEngine matchingEngine = new MatchingEngine();
        //when
        Stream<MentoringModel> bestMentorCandidate = matchingEngine.findProposalsStream(mentee, mentor1, mentor2);

        //then
        assertThat(bestMentorCandidate.limit(1))
                .containsExactly(mentor2);
    }

    @Test
    @DisplayName("From 2 Mentors find MentoringModel from exact same Family as MentoringModel")
    void findBestCandidateFromManyMentors() {
        //given
        MentoringModel mentee = newMentee().family(Family.DATA).build();
        MatchingEngine matchingEngine = new MatchingEngine();
        //when
        Stream<MentoringModel> bestCandidate = matchingEngine.findProposalsStream(mentee,
                newMentor().family(Family.ARCHITECTURE).build(),
                newMentor().family(Family.DATA).build());
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

////    @Test
//    public void RequireSameSpecializationForCorporateServices()
//    {
//        val mentee = newMentee().WithJs")obFamily("Corporate Service.WithSpecialisation("some specialization");
//        val mentor1 = newMentor().WithJobFamily("Corporate Services").WithSpecialisation("other specialization");
//        val mentor2 = newMentor().WithJobFamily("Corporate Services").WithSpecialisation("some specialization");
//        val candidates = Candidates(mentee, mentor1, mentor2);
//        val actual = candidates.FirstOrDefault();
//        var expected = mentor2;
//        Assert.That(actual, Is.EqualTo(expected));
//        Assert.That(candidates.Count(), Is.EqualTo(1));
//    }

//     [Category("Non business case")]
//    /// <summary>
//    /// For technical reason, I see posible to set someone as his/her Mentor. It could be limitation of technical tools
//    /// but for mentoring 'to search the best mentor' it is ofcourse invalid scenario.
//    /// </summary>
//        [Category("Non business case")]
//            [Test]
//    public void NotAcceptSelf()
//    {
//        var menteeCandidate = newMentor();
//        var mentorCandidate = newMentor();
//        // generally the mentor is acceptable for someone with identical mentoring model.
//        Assume.That(Candidates(menteeCandidate, mentorCandidate), Is.Not.Empty);
//        // let try to add one mor aspect - we need to add to the 'identical mentoring model' fact that this time it is the same person.
//        // not proposed candidate should not be accepted.
//        Assert.That(Candidates(mentorCandidate, mentorCandidate), Is.Empty);
//    }

    @Test
    @DisplayName("When we use helper methods : newMentee and newMentor we make sure these are valid")
    public void UseValidAssumptionsInTests() {
        // In all tests we use helper methods : newMentee and newMentor. They were created to simplify process of creation
        // MentoringModel definition so created mentor as by definition accepted as a mentor for created mentee.
        // So before starting tests, need to check if those methods are working as predicted.
        val mentee = newMentee().build();
        val mentor = newMentor().build();
        val matchingEngine = new MatchingEngine();

        val proposals = matchingEngine.findProposalsStream(mentee, mentor);

        Assertions.assertThat(proposals).containsExactly(mentor);
    }

    static MentoringModel.MentoringModelBuilder newMentor() {
        return new MentoringModel(Family.PROJECT_DEVELOPMENT, "JAVA").toBuilder();
    }

    static MentoringModel.MentoringModelBuilder newMentee() {
        return new MentoringModel(Family.PROJECT_DEVELOPMENT, "JAVA").toBuilder();
    }

    @Test
    @DisplayName("Check for specialization among metnor and mentee while BOTH in same specialization..")
    public void shouldFindMatchinSpecializationInCorporateServices() {
        val mentee = newMentee().family(Family.CORPORATE_SERVICES).specialization("HR").build();
        val mentor1 = newMentor().family(Family.CORPORATE_SERVICES).specialization("IT").build();
        val mentor2 = newMentor().family(Family.CORPORATE_SERVICES).specialization("HR").build();
        val matchingEngine = new MatchingEngine();

        val proposals = matchingEngine.findProposalsStream(mentee, mentor1, mentor2);

        Assertions.assertThat(proposals).containsExactly(mentor2);
    }
}
