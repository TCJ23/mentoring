package gft.mentoring;

import lombok.Value;
import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Main Class to test MatchingEngine")
public class MatchingSpec {

    /* This test if to meet requirement 1.1 in REQUIREMENTS.md
     in @ParameterizedTest you either keep
     your testmethod name and method source name the same or use parameters as below
     This class should test MatchingEngine().findProposals
     for MentoringModel in
     Project Development
     Architecture Digital
     Data
     we can assign Mentors from above Families treated as one*/
    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("singleMatchingParam")
    @DisplayName("Check if MatchingEngine proposes mentors correctly per Family")
    void findSingleProposedMentorFromCandidates(SingleMatchingParam singleMatchingParam) {
        //given
        val proposal = newMentor().family(singleMatchingParam.mentorCandidateFamily)
                .contractor(singleMatchingParam.contractor).build();
        val mentee = newMentee().family(singleMatchingParam.menteeFamily)
                .contractor(singleMatchingParam.contractor).build();
        //when
        val candidate = new MatchingEngine().findProposals(mentee, proposal);
        //then
        assertThat(candidate.count() == 1).isEqualTo(singleMatchingParam.accepted);
    }

    static Stream<SingleMatchingParam> singleMatchingParam() {
        return Stream.of(
                new SingleMatchingParam("Scenario: MentoringModel & MentoringModel ARE BOTH in Development Group & Exact SAME FAMILY",
                        Family.PROJECT_DEVELOPMENT, Family.PROJECT_DEVELOPMENT, false, true),
                new SingleMatchingParam("Scenario: MentoringModel & MentoringModel: ONLY ONE IS in Development Group",
                        Family.ARCHITECTURE, Family.CORPORATE_SERVICES, false, false),
                new SingleMatchingParam("Scenario: MentoringModel & MentoringModel ONLY ONE IS in Development Group",
                        Family.DIGITAL, Family.AMS, false, false),
                new SingleMatchingParam("Scenario: MentoringModel & MentoringModel ARE BOTH in Development Group, But DIFFERENT FAMILIES",
                        Family.DATA, Family.ARCHITECTURE, false, true),
                new SingleMatchingParam("Scenario: MentoringModel & MentoringModel NEITHER IS in Development Group",
                        Family.AMS, Family.BUSINESS_CONSULTING, false, false),
                new SingleMatchingParam("Scenario: One of candidates is Contractor ",
                        Family.AMS, Family.AMS, true, false));
    }

    @Value
    static class SingleMatchingParam {
        private String scenario;
        private Family menteeFamily;
        private Family mentorCandidateFamily;
        private boolean contractor;
        private boolean accepted;

        @Override
        public String toString() {
            return scenario;
        }
    }

    /*This test if to meet requirement 1.2 in REQUIREMENTS.md*/
    @Test
    @DisplayName("From 2 Mentors prefer Mentor from exact same Family as Mentee")
    void findPreferedCandidateFromManyMentors() {
        //given
        MentoringModel mentee = newMentee().family(Family.DATA).build();
        val mentor1 = newMentor().family(Family.ARCHITECTURE).build();
        val mentor2 = newMentor().family(Family.DATA).build();
        MatchingEngine matchingEngine = new MatchingEngine();
        //when
        val bestMentorCandidates = matchingEngine.findProposals(mentee, mentor1, mentor2).collect(Collectors.toList());
        val mentorProposed = bestMentorCandidates.get(0);
        //then
        assertThat(mentorProposed).isEqualTo(mentor2);
        assertThat(bestMentorCandidates).size().isEqualTo(2);
    }

    @Test
    @DisplayName("From 2 Mentors propose only Mentor from exact same Family as Mentee")
    void findBestProposalFromManyMentors() {
        //given
        MentoringModel mentee = newMentee().family(Family.AMS).build();
        MatchingEngine matchingEngine = new MatchingEngine();
        //when
        val bestCandidate = matchingEngine.findProposals(mentee,
                newMentor().family(Family.ARCHITECTURE).build(),
                newMentor().family(Family.AMS).build()).collect(Collectors.toList());
        //then
        assertThat(bestCandidate.get(0).getFamily()).isEqualTo(mentee.getFamily());
        assertThat(bestCandidate).size().isEqualTo(1);
    }


    /*This test if to meet requirement 1.3 in REQUIREMENTS.md*/
    @Test
    @DisplayName("Validate that in Corporate Services MatchingEngine prefers same specialization")
    public void shouldFindMatchingSpecializationInCorporateServices() {
        val mentee = newMentee().family(Family.CORPORATE_SERVICES).specialization("HR").build();
        val mentor1 = newMentor().family(Family.CORPORATE_SERVICES).specialization("IT").build();
        val mentor2 = newMentor().family(Family.CORPORATE_SERVICES).specialization("HR").build();
        val matchingEngine = new MatchingEngine();

        val proposals = matchingEngine.findProposals(mentee, mentor1, mentor2);

        Assertions.assertThat(proposals).containsExactly(mentor2);
    }

    /*This test if to meet requirement 1.4 in REQUIREMENTS.md*/
    @Test
    @DisplayName("Validate SENIORITY for mentor")
    public void shouldValidateThatMentorHasAtLeastOneYearOfSeniority() {
        val mentee = newMentee().build();
        //boundary values edge cases
        val toYoungToBeMentor = newMentor().seniority(364).build();
        val justSeniorMentor = newMentor().seniority(365).build();
        val matchingEngine = new MatchingEngine();
        val proposals = matchingEngine.findProposals(mentee, toYoungToBeMentor, justSeniorMentor);
        Assertions.assertThat(proposals).containsExactly(justSeniorMentor);
    }

    /*This test if to meet requirement 1.5 in REQUIREMENTS.md*/
    @Test
    @DisplayName("Validate same LOCALIZATION preference")
    public void shouldPreferSameLocalizationBetweenMenteeAndMentor() {
        //given
        val mentee = newMentee().localization("Warszawa").build();

        val differentLocalizationMentor = newMentor().localization("Lodz").build();
        val sameLocalizationMentor = newMentor().localization("Warszawa").build();
        val matchingEngine = new MatchingEngine();
        //when
        Stream<MentoringModel> proposals = matchingEngine.findProposals(mentee, differentLocalizationMentor, sameLocalizationMentor);

        assertThat(proposals.limit(1)).containsExactly(sameLocalizationMentor);
    }

    /*This test if to meet requirement 1.5 in REQUIREMENTS.md*/
    @Test
    @DisplayName("Validate that DIFFERENT LOCALIZATION doesn't REJECT candidate")
    public void shouldNotRejectDifferentLocalizationBetweenMenteeAndMentor() {
        //given
        val mentee = newMentee().localization("Warszawa").build();

        val differentLocalizationMentor = newMentor().localization("Lodz").build();
        val sameLocalizationMentor = newMentor().localization("Warszawa").build();
        val matchingEngine = new MatchingEngine();
        //when
        Stream<MentoringModel> proposals = matchingEngine.findProposals(mentee, differentLocalizationMentor, sameLocalizationMentor);

        assertThat(proposals.count()).isEqualTo(2);
    }

    /*This test if to meet requirement 1.6 in REQUIREMENTS.md*/
    @Test
    @DisplayName("Validate that same from same Family Matching Engine prefers the exact same specialization")
    public void shouldFindMatchingSpecialization() {
        //given
        val mentee = newMentee().family(Family.DATA).specialization("BIG Data").build();
        val diffSpecMentor = newMentor().family(Family.DATA).specialization("SQL").build();
        val sameSpecMentor = newMentor().family(Family.DATA).specialization("BIG Data").build();
        val matchingEngine = new MatchingEngine();
        //when
        val bestProposals = matchingEngine.findProposals(mentee, diffSpecMentor, sameSpecMentor).collect(Collectors.toList());
        //then
        val mentorProposed = bestProposals.get(0);
        assertThat(mentorProposed.getSpecialization()).isEqualTo(mentee.getSpecialization());
    }

    /*This test if to meet requirement 1.6 in REQUIREMENTS.md*/
    @Test
    @DisplayName("Validate similar Dev Group Matching Engine prefers the exact SAME SPECIALIZATION")
    public void shouldFindMatchingSpecializationInSimilarDevGroup() {
        //given
        val mentee = newMentee().family(Family.DATA).specialization("BIG Data").build();
        val diffSpecMentor = newMentor().family(Family.ARCHITECTURE).specialization("SQL").build();
        val sameSpecMentor = newMentor().family(Family.ARCHITECTURE).specialization("BIG Data").build();
        val matchingEngine = new MatchingEngine();
        //when
        val bestProposals = matchingEngine.findProposals(mentee, diffSpecMentor, sameSpecMentor).collect(Collectors.toList());
        //then
        val mentorProposed = bestProposals.get(0);
        assertThat(mentorProposed.getSpecialization()).isEqualTo(mentee.getSpecialization());
    }

    /*This test if to meet requirement 1.7 in REQUIREMENTS.md*/
    @Test
    @DisplayName("Validate that Matching Engine ignores CONTRACTORS")
    public void shouldIgnoreContractors() {
        //given
        val mentee = newMentee().contractor(false).build();
        val contractor = newMentor().contractor(true).build();
        val employee = newMentor().contractor(false).build();
        val matchingEngine = new MatchingEngine();
        //when
        val proposals = matchingEngine.findProposals(mentee, contractor, employee).collect(Collectors.toList());
        //then
        assertThat(proposals.size()).isEqualTo(1);
    }

    /*This test if to meet requirement 1.8 in REQUIREMENTS.md*/
    @Test
    @DisplayName("Validate that Matching Engine prefers proposed Mentor with higher SENIORITY")
    public void shouldPreferHigherSeniority() {
        //given
        val mentee = newMentee().build();
        val juniorMentor = newMentor().seniority(1 * 365).build();
        val seniorMentor = newMentor().seniority(5 * 365).build();
        val matchingengine = new MatchingEngine();
        //when
        val proposals = matchingengine.findProposals(mentee, juniorMentor, seniorMentor);
        //then
        val mentorProposed = proposals.findFirst();
        assertThat(mentorProposed.isPresent()).isTrue();
        assertThat(mentorProposed.get().equals(seniorMentor)).isTrue();
    }

    /*This test if to meet requirement 1.9 in REQUIREMENTS.md*/
    @Test
    @DisplayName("Validate that Matching Engine ignores Mentor that will LEAVE GFT soon")
    public void shouldIgnoreLeavers() {
        //given
        val mentee = newMentee().build();
        val leaver = newMentor().leaver(true).build();
        val matchingMentor = newMentor().build();
        //when
        val proposals = new MatchingEngine().findProposals(mentee, leaver, matchingMentor);
        //then
        assertThat(proposals.count()).isEqualTo(1);

    }

    /*This test if to meet requirement 1.10 in REQUIREMENTS.md*/
    @Test
    @DisplayName("Mentee prefers Mentor with higher LEVEL")
    public void shouldProposeMentorWithHighestGrade() {
        //given
        val mentee = newMentee().level(3).build();
        val sameLevelMentor = newMentor().level(3).build();
        val higherLevelMentor = newMentor().level(4).build();
        //when
        val proposals = new MatchingEngine().findProposals(mentee, sameLevelMentor, higherLevelMentor);
        //then
        val proposedMentor = proposals.findFirst().get();
        assertThat(proposedMentor.equals(higherLevelMentor)).isTrue();
    }


    @Test
    @DisplayName("Helper methods with default test data should always be valid")
    public void UseValidAssumptionsInTests() {
        // In all tests we use helper methods : newMentee and newMentor. They were created to simplify process of creation
        // MentoringModel definition so created mentor as by definition accepted as a mentor for created mentee.
        // So before starting tests, need to check if those methods are working as predicted.
        val mentee = newMentee().build();
        val mentor = newMentor().build();
        val matchingEngine = new MatchingEngine();

        val proposals = matchingEngine.findProposals(mentee, mentor);

        Assertions.assertThat(proposals).containsExactly(mentor);
    }


    static MentoringModel.MentoringModelBuilder newMentor() {
        return new MentoringModel(Family.PROJECT_DEVELOPMENT, "JAVA", 3, 3 * 365,
                "Lodz", false, false).toBuilder();
    }

    static MentoringModel.MentoringModelBuilder newMentee() {
//        return new MentoringModel(Family.PROJECT_DEVELOPMENT, "JAVA", 30, "Warszawa").toBuilder();
        return new MentoringModel(Family.PROJECT_DEVELOPMENT, "JAVA", 3, 30,
                "Lodz", false, false).toBuilder();
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
