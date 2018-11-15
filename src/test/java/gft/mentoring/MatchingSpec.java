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

@DisplayName("1 - Main Class to test MatchingEngine")
class MatchingSpec {
    private static final String NO_NAME = "";
    /* This test if to meet requirement 1.1 in REQUIREMENTS.md
     in @ParameterizedTest you either keep
     your testmethod name and method source name the same or use parameters as below
     This class should test MatchingEngine().findProposals
     for MentoringModel in
     Project Development
     Architecture Digital
     Data
     we can assign Mentors from above Families treated as one*/

    /*Base models for testing*/
    private static MentoringModel.MentoringModelBuilder newMentor() {
        return new MentoringModel(Family.PROJECT_DEVELOPMENT, "JAVA", 4, 3 * 365,
                "Lodz", NO_NAME, NO_NAME, false, false, 0, 23).toBuilder();
    }

    private static MentoringModel.MentoringModelBuilder newMentee() {
        return new MentoringModel(Family.PROJECT_DEVELOPMENT, "JAVA", 3, 30,
                "Lodz", NO_NAME, NO_NAME, false, true, 0, 23).toBuilder();
    }

    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("singleMatchingParam")
    @DisplayName("1.1 - Check if MatchingEngine proposes mentors correctly per Family")
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

    private static Stream<SingleMatchingParam> singleMatchingParam() {
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
                        Family.AMS, Family.AMS, true, false)
        );
    }

    @Value
    static class SingleMatchingParam {
        //input
        private String scenario;
        private Family menteeFamily;
        private Family mentorCandidateFamily;
        private boolean contractor;
        //output
        private boolean accepted;

        @Override
        public String toString() {
            return scenario;
        }
    }

    /*This test if to meet requirement 1.2 in REQUIREMENTS.md*/
    @Test
    @DisplayName("1.2 - From 2 Mentors prefer Mentor from exact same Family as Mentee")
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
    @DisplayName("1.2 - From 2 Mentors propose only Mentor from exact same Family as Mentee")
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
    @DisplayName("1.3 - Validate that in Corporate Services MatchingEngine prefers same specialization")
    void shouldFindMatchingSpecializationInCorporateServices() {
        val mentee = newMentee().family(Family.CORPORATE_SERVICES).specialization("HR").build();
        val mentor1 = newMentor().family(Family.CORPORATE_SERVICES).specialization("IT").build();
        val mentor2 = newMentor().family(Family.CORPORATE_SERVICES).specialization("HR").build();
        val matchingEngine = new MatchingEngine();

        val proposals = matchingEngine.findProposals(mentee, mentor1, mentor2);

        Assertions.assertThat(proposals).containsExactly(mentor2);
    }

    /*This test if to meet requirement 1.4 in REQUIREMENTS.md*/
    @Test
    @DisplayName("1.4 - Validate SENIORITY for mentor")
    void shouldValidateThatMentorHasAtLeastOneYearOfSeniority() {
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
    @DisplayName("1.5 - Validate same LOCALIZATION preference")
    void shouldPreferSameLocalizationBetweenMenteeAndMentor() {
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
    @DisplayName("1.5 - Validate that DIFFERENT LOCALIZATION doesn't REJECT candidate")
    void shouldNotRejectDifferentLocalizationBetweenMenteeAndMentor() {
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
    @DisplayName("1.6 - Validate that same from same Family Matching Engine prefers the exact same specialization")
    void shouldFindMatchingSpecialization() {
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
    @DisplayName("1.6 - Validate similar Dev Group Matching Engine prefers the exact SAME SPECIALIZATION")
    void shouldFindMatchingSpecializationInSimilarDevGroup() {
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
    @DisplayName("1.7 - Validate that Matching Engine ignores CONTRACTORS")
    void shouldIgnoreContractors() {
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
    @DisplayName("1.8 - Validate that Matching Engine prefers proposed Mentor with higher SENIORITY")
    void shouldPreferHigherSeniority() {
        //given
        val mentee = newMentee().build();
        val juniorMentor = newMentor().seniority(1 * 365).build();
        val seniorMentor = newMentor().seniority(5 * 365).build();
        val matchingengine = new MatchingEngine();
        //when
        val proposals = matchingengine.findProposals(mentee, juniorMentor, seniorMentor);
        //then
        val mentorProposed = proposals.findFirst();
        assertThat(mentorProposed.isPresent() && mentorProposed.get().equals(seniorMentor)).isTrue();
    }

    /*This test if to meet requirement 1.9 in REQUIREMENTS.md*/
    @Test
    @DisplayName("1.9 - Validate that Matching Engine ignores Mentor that will LEAVE GFT soon")
    void shouldIgnoreLeavers() {
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
    @DisplayName("1.10 - Mentee prefers Mentor with higher LEVEL")
    void shouldProposeMentorWithHighestGrade() {
        //given
        val mentee = newMentee().level(3).build();
        val sameLevelMentor = newMentor().level(3).build();
        val higherLevelMentor = newMentor().level(4).build();
        //when
        val proposals = new MatchingEngine().findProposals(mentee, sameLevelMentor, higherLevelMentor);
        //then
        val proposedMentor = proposals.findFirst();
        assertThat(proposedMentor.isPresent() && proposedMentor.get().equals(higherLevelMentor)).isTrue();
    }

    /*This test if to meet requirement 1.11 in REQUIREMENTS.md*/
    @Test
    @DisplayName("1.11 - Mentee is always at LOWER LEVEL than Mentor")
    void shouldRejectProposedMentorWithLowerGradeThanMentee() {
        //given
        val mentee = newMentee().level(4).build();
        val lowerLevelMentor = newMentor().level(3).build();
        val higherLevelMentor = newMentor().level(5).build();
        //when
        val proposals = new MatchingEngine().findProposals(mentee, lowerLevelMentor, higherLevelMentor)
                .collect(Collectors.toList());
        //then
        val proposedMentor = proposals.get(0);
        assertThat(proposals.size() == 1).isTrue();
        assertThat(proposedMentor.equals(higherLevelMentor)).isTrue();
    }

    /*This test if to meet requirement 1.11 in REQUIREMENTS.md*/
    @Test
    @DisplayName("1.11 - Mentee is always at LOWER LEVEL than Mentor group test")
    void shouldReject1outOf4ProposedMentorWithLowerGradeThanMentee() {
        //given
        val mentee = newMentee().level(4).build();
        val lowerLevelMentor = newMentor().level(3).build();
        val highLevelMentor = newMentor().level(4).build();
        val higherLevelMentor = newMentor().level(5).build();
        val highestLevelMentor = newMentor().level(6).build();
        //when
        val proposals = new MatchingEngine().findProposals(mentee, lowerLevelMentor, highLevelMentor, higherLevelMentor,
                highestLevelMentor).collect(Collectors.toList());
        //then
        assertThat(proposals.size() == 3).isTrue();
    }

    /*This test if to meet requirement 1.12 in REQUIREMENTS.md*/
    @Test
    @DisplayName("1.12 - Mentee prefers Mentor with lower number of Mentees assigned")
    void shouldPreferMentorWithLessMenteesAssigned() {
        //given
        val mentee = newMentee().build();
        val bigAmountofMentees = newMentor().menteesAssigned(5).build();
        val smallAmountofMentees = newMentor().menteesAssigned(3).build();
        val zeroMenteesAssigned = newMentor().menteesAssigned(0).build();
        //when
        val proposals = new MatchingEngine().findProposals(mentee, bigAmountofMentees, smallAmountofMentees, zeroMenteesAssigned
        ).collect(Collectors.toList());
        val proposalsStrm = new MatchingEngine().findProposals(mentee, bigAmountofMentees, smallAmountofMentees, zeroMenteesAssigned);
        //then
        val proposedMentor = proposals.get(0);
        val proposedMentorStrm = proposalsStrm.findFirst();

        assertThat(proposedMentor.equals(zeroMenteesAssigned)).isTrue();
        assertThat(proposedMentorStrm.isPresent() && proposedMentorStrm.get().equals(zeroMenteesAssigned)).isTrue();
    }

    /*This test if to meet requirement 1.13 in REQUIREMENTS.md*/
    @Test
    @DisplayName("1.13 - Mentors are only on level 4 and above")
    void shouldRejectAllBelowLevel4asMentor() {
        //given
        val mentee = newMentee().build();
        val below4thLevelNotMentor = newMentor().level(3).build();
//        below4thLevelNotMentor.setLevel(3);
        val above4thLevelIsMentor = newMentor().level(4).build();
        //when
        val proposals = new MatchingEngine().findProposals(mentee, below4thLevelNotMentor, above4thLevelIsMentor)
                .collect(Collectors.toList());
        //then
        assertThat(proposals.size() == 1).isTrue();
    }

    /*This test if to meet requirement 1.14 in REQUIREMENTS.md*/
    @Test
    @DisplayName("1.14 - Seniority is preferred over Level")
    void shouldPreferDevManWithHigherSeniorityInsteadofMentorWithHigherGrade() {
        //given
        val mentee = newMentee().build();
        val highLevelMentor = newMentor().seniority(1 * 365).level(5).build();
        val highSeniorityMentor = newMentor().seniority(3 * 365).level(5).build();
        val highestSeniorityMentor = newMentor().seniority(4 * 365).level(4).build();
        val highestLevelMentor = newMentor().seniority(3 * 365).level(6).build();
        //when
        val proposals = new MatchingEngine().findProposals(mentee, highLevelMentor, highSeniorityMentor,
                highestSeniorityMentor, highestLevelMentor).collect(Collectors.toList());
        //then
        val proposedMentor = proposals.get(0);
        System.out.println(proposedMentor);
        assertThat(proposedMentor.equals(highestSeniorityMentor)).isTrue();
    }

    @Test
    @DisplayName("1.14 - Seniority over Level")
    void shouldPreferDevManWithHigherSeniorityThanLevel() {
        //given
        val mentee = newMentor().build();
        val higherLevelMentor = newMentor().level(mentee.getLevel() + 1).build();
        val olderSeniorityMentor = newMentor().seniority(mentee.getSeniority() + 3 * 365).build();
        //when
        val proposals = new MatchingEngine().findProposals(mentee, higherLevelMentor, olderSeniorityMentor).collect(Collectors.toList());
        //then
        val proposedMentor = proposals.get(0);
        assertThat(proposedMentor).isEqualTo(olderSeniorityMentor);
    }

    /*This test if to meet requirement 1.14 in REQUIREMENTS.md*/
    @Test
    @DisplayName("1.14 - Equal Seniority then prefer Level")
    void shouldPreferDevManWithHigherLevelWhenSeniorityIsEqual() {
        //given
        val mentee = newMentee().build();
        val highLevelMentor = newMentor().level(6).build();
        val fourYearsSeniorityLevel5 = newMentor().seniority(4 * 365).level(5).build();
        val fourYearsSeniorityLevel4 = newMentor().seniority(4 * 365).level(4).build();
        val highestLevelMentor = newMentor().seniority(3 * 365).level(5).build();
        //when
        val proposals = new MatchingEngine().findProposals(mentee, highLevelMentor, fourYearsSeniorityLevel5,
                fourYearsSeniorityLevel4, highestLevelMentor).collect(Collectors.toList());
        //then
        val proposedMentor = proposals.get(0);
        assertThat(proposedMentor.equals(fourYearsSeniorityLevel5)).isTrue();
    }


    /*This test if to meet requirement 1.15 in REQUIREMENTS.md*/
    @Test
    @DisplayName("1.15 - Mentor needs to have longer Seniority than Mentee")
    void shouldRejectMentorWithLowerSeniorityThanMentee() {
        //given
        val mentee = newMentee().seniority(2 * 365).build();
        val lowerSeniorityMentor = newMentor().seniority(1 * 365).level(6).build();
        val higherSeniorityMentor = newMentor().seniority(4 * 365).level(5).build();
        //when
        val proposals = new MatchingEngine().findProposals(mentee, lowerSeniorityMentor, higherSeniorityMentor)
                .collect(Collectors.toList());
        //then
        val proposedMentor = proposals.get(0);
        assertThat(proposedMentor.equals(higherSeniorityMentor)).isTrue();
        assertThat(proposals.size() == 1).isTrue();
    }

    /*This test if to meet requirement 1.16 in REQUIREMENTS.md*/
    @Test
    @DisplayName("1.16 - Mentor needs to have longer Seniority than Mentee")
    void shouldPreferMentorBetweenAgeOf30to40() {
        //given
        val mentee = newMentee().build();
        val youngMentor = newMentor().age(20).build();
        val mentorAt30YearsOld = newMentor().age(30).build();
        val mentorAt40YearsOld = newMentor().age(40).build();
        //when
        val proposals = new MatchingEngine().findProposals(mentee, youngMentor, mentorAt30YearsOld, mentorAt40YearsOld)
                .collect(Collectors.toList());
        //then
        val proposedMentor = proposals.get(0);
        assertThat(proposedMentor.equals(mentorAt40YearsOld)).isTrue();
        assertThat(proposals.size() == 3).isTrue();
    }

    /*This test if to meet requirement 1.17 in REQUIREMENTS.md*/
    @Test
    @DisplayName("1.17 - there's limit of Mentees Assigned to Mentor per Level")
    void shouldRejectMentorWithMenteesAssignedOverLimit() {
        //given
        val mentee = newMentee().build();
        val mentorWithLimitReachL4 = newMentor().level(4).menteesAssigned(2).build();
        val mentorWithLimitReachL5 = newMentor().level(5).menteesAssigned(3).build();
        val mentorWithLimitReachL6 = newMentor().level(6).menteesAssigned(4).build();
        val mentorWithLimitReachL7 = newMentor().level(7).menteesAssigned(5).build();
        val mentorWithLimitReach6Mentees = newMentor().level(7).menteesAssigned(6).build();
        val mentorWithFreeSlotL4 = newMentor().level(4).menteesAssigned(1).build();
        //when
        val proposals = new MatchingEngine().findProposals(mentee, mentorWithLimitReachL4, mentorWithLimitReachL5,
                mentorWithLimitReachL6, mentorWithLimitReachL7, mentorWithFreeSlotL4, mentorWithLimitReach6Mentees).collect(Collectors.toList());
        //then
        val proposedMentor = proposals.get(0);
        assertThat(proposals.size() == 1).isTrue();
        assertThat(proposedMentor.equals(mentorWithFreeSlotL4)).isTrue();
    }

    /*This test if to meet requirement 2.1 in REQUIREMENTS.md*/
    @Test
    @DisplayName("2.1 - When Mentee is from Lodz or Poznan need to have Mentor from the same location")
    void shouldRejectMentorFromOtherLocationWhenMenteeIsFromLodz() {
        //given
        val menteeLodz = newMentee().localization("Lodz").build();
        val mentorFromWarsaw = newMentor().localization("Warsaw").build();
        val mentorFromPoznan = newMentor().localization("Poznan").build();
        val mentorFromLodz = newMentor().localization("Lodz").build();
        //when
        val proposals = new MatchingEngine().findProposals(menteeLodz, mentorFromLodz, mentorFromPoznan, mentorFromWarsaw)
                .collect(Collectors.toList());
        //then
        val proposedMentor = proposals.get(0);
        assertThat(proposedMentor.equals(mentorFromLodz)).isTrue();
        assertThat(proposals.size() == 1).isTrue();
    }

    /*This test if to meet requirement 2.1 in REQUIREMENTS.md*/
    @Test
    @DisplayName("2.1 - When Mentee is from Lodz or Poznan need to have Mentor from the same location")
    void shouldRejectMentorFromOtherLocationWhenMenteeIsFromPoznan() {
        //given
        val menteePoznan = newMentee().localization("Poznan").build();
        val mentorFromWarsaw = newMentor().localization("Warsaw").build();
        val mentorFromPoznan = newMentor().localization("Poznan").build();
        val mentorFromLodz = newMentor().localization("Lodz").build();
        //when
        val proposals = new MatchingEngine().findProposals(menteePoznan, mentorFromLodz, mentorFromPoznan, mentorFromWarsaw)
                .collect(Collectors.toList());
        //then
        val proposedMentor = proposals.get(0);
        assertThat(proposedMentor.equals(mentorFromPoznan)).isTrue();
        assertThat(proposals.size() == 1).isTrue();
    }

    /*This test if to meet requirement 2.1 in REQUIREMENTS.md*/
    @Test
    @DisplayName("2.1 - When mentee is from Warsaw his/her mentor can't be from Poznan")
    void shouldRejectMentorFromPoznanWhenMenteeIsFromWarsaw() {
        //given
        val menteeWarsaw = newMentee().localization("Warsaw").build();
        val mentorFromWarsaw = newMentor().localization("Warsaw").build();
        val mentorFromPoznan = newMentor().localization("Poznan").build();
        val mentorFromLodz = newMentor().localization("Lodz").build();
        //when
        val proposals = new MatchingEngine().findProposals(menteeWarsaw, mentorFromLodz, mentorFromPoznan, mentorFromWarsaw)
                .collect(Collectors.toList());
        //then
        val proposedMentor = proposals.get(0);
        assertThat(proposedMentor.equals(mentorFromWarsaw)).isTrue();
        assertThat(proposals.size() == 2).isTrue();
    }

    @Test
    @DisplayName("Helper methods with default test data should always be valid")
    void UseValidAssumptionsInTests() {
        // In all tests we use helper methods : newMentee and newMentor. They were created to simplify process of creation
        // MentoringModel definition so created mentor as by definition accepted as a mentor for created mentee.
        // So before starting tests, need to check if those methods are working as predicted.
        val mentee = newMentee().build();
        val mentor = newMentor().build();
        val matchingEngine = new MatchingEngine();

        val proposals = matchingEngine.findProposals(mentee, mentor);

        Assertions.assertThat(proposals).containsExactly(mentor);
    }
}