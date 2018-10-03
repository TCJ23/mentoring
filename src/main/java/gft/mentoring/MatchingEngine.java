package gft.mentoring;
/* This class implements logic to meet business requirements described in requirements.md*/

import gft.mentoring.strategies.*;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.val;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

class MatchingEngine {

    static VotingStrategy[] strategies = {
            new IgnoreLeaversStrategy(),
            new IgnoreContractorsStrategy(),
            new RejectDevManWithLimitOfMenteesAssignedStrategy(),
            new RejectLowerLevelDevManStrategy(),
            new RejectLowerSeniorityDevManStrategy(),
            new RejectOtherLocationsForLodzAndPoznan(),
            new DevManHasToBeAtLevel4AndAbove(),
            new DevManHasToWorkAtLeastOneYearInGFTStrategy(),
            new PreferDevManFromSameFamilyOrSimilarGroupStrategy(),
            new PreferDevManFromCorporateServicesBySameSpecializatonStrategy(),
            new PreferDevManFromSameLocalizationStrategy(),
            new PreferDevManFromSameSpecializationStrategy(),
            new PreferDevManWithHigherLevelStrategy(),
            new PreferDevManWithHigherSeniorityStrategy(),
            new PreferDevManWithLowerNumberOfAssignedMenteesStrategy(),
            new PreferDevManThatIsOlderThan30YearsOldStrategy(),
    };

    Stream<MentoringModel> findProposals(MentoringModel mentee, MentoringModel... candidates) {
        return Arrays.stream(candidates)
                .map(mentorCandidate -> new SympathyResultTuple(mentorCandidate, sympathy(mentee, mentorCandidate)))
                .filter(it -> it.sympathy != SympathyResult.None)
                .map(mentorCandidate -> new SymapthyLevelTuple(mentorCandidate.mentor,
                        ((SympathyResult.Some) mentorCandidate.sympathy).getValue()))
                .sorted((mentorCandidate1, mentorCandidate2) -> -(mentorCandidate1.sympathy - mentorCandidate2.sympathy))
                .map(it -> it.mentor)
                ;
    }

    @AllArgsConstructor
    private static class SympathyResultTuple {
        private MentoringModel mentor;
        private SympathyResult sympathy;
    }

    @AllArgsConstructor
    private static class SymapthyLevelTuple {
        private MentoringModel mentor;
        private int sympathy;
    }

    /**
     * This is 1 of 2 main methods in Matching Engine it takes
     *
     * @param mentee - mentee type of GFT employee
     * @param mentor - mentor type of GFT employee
     *               calculates level of sympathy between such proposed pair
     *               this should result with int value of
     * @return SympathyResult @see {@link gft.mentoring.MatchingEngine}
     */
    private static SympathyResult sympathy(MentoringModel mentee, MentoringModel mentor) {
        /**
         * first we need to collect VotingResults
         * @see {@link gft.mentoring.VotingResult}*/
        List<VotingResult> votings = new ArrayList<>();
        for (val strategy : strategies) {
            val result = strategy.calculateSympathy(mentee, mentor);
            votings.add(result);
        }
        /**
         * We aggregate only values from Support type of VotingResults points from classes
         * that implements VotingStrategy
         * @see VotingStrategy#calculateSympathy(MentoringModel, MentoringModel)
         * @see VotingResult
         * @see Support#sympathy
         * @return some sum of values
         * @see gft.mentoring.SympathyResult.Some
         * every other VotingResult is ignored and we
         * @return no value
         * @see gft.mentoring.SympathyResult.None*/

        Optional<Integer> sympathyLevel = Optional.empty();
        for (VotingResult vote : votings) {
            if (vote instanceof Support) {
                val current = sympathyLevel.orElse(0);
                sympathyLevel = Optional.of(current + ((Support) vote).getSympathy());
            }
            if (vote instanceof Rejected) {
                return SympathyResult.None;
            }
        }

        return sympathyLevel.isPresent()
                ? new SympathyResult.Some(sympathyLevel.get())
                : SympathyResult.None;
    }

}

/**
 * Sympathy result can contain value or not
 * Value should be aggregation of Support type of VotingResults points from classes
 * that implements VotingStrategy
 *
 * @see VotingStrategy#calculateSympathy(MentoringModel, MentoringModel)
 * @see VotingResult
 * @see Support#sympathy
 */
abstract class SympathyResult {

    static final SympathyResult None = new SympathyResult() {
    };

    @Value
    static class Some extends SympathyResult {
        private int value;
    }

}
