package gft.mentoring;
/* This class implements logic to meet business requirements described in requirements.md*/

import gft.mentoring.strategies.*;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.val;

import java.util.*;
import java.util.stream.Stream;

class MatchingEngine {

    static VotingStrategy[] strategies = {
            new IgnoreLeaversStrategy(),
            new IgnoreContractorsStrategy(),
            new MentorHasToWorkAtLeastOneYearInGFTStrategy(),
            new PreferDevManFromSameFamilyOrSimilarGroupStrategy(),
            new PreferDevManFromCorporateServicesBySameSpecializatonStrategy(),
            new PreferDevManFromSameLocalizationStrategy(),
            new PreferDevManFromSameSpecializationStrategy(),
            new PreferDevManWithHigherLevel()
    };

    Stream<MentoringModel> findProposals(MentoringModel mentee, MentoringModel... candidates) {
        return Arrays.stream(candidates)
                .map(it -> new SympathyResultTuple(it, sympathy(mentee, it)))
                .filter(it -> it.sympathy != SympathyResult.None)
                .map(it -> new SymapthyLevelTuple(it.mentor, ((SympathyResult.Some) it.sympathy).getValue()))
                .sorted((it1, it2) -> -(it1.sympathy - it2.sympathy))
                .map(it -> it.mentor)
                .sorted(bySeniortyASC);
    }

    @AllArgsConstructor
    static class SympathyResultTuple {
        private MentoringModel mentor;
        private SympathyResult sympathy;
    }

    @AllArgsConstructor
    static class SymapthyLevelTuple {
        private MentoringModel mentor;
        private int sympathy;
    }

    static SympathyResult sympathy(MentoringModel mentee, MentoringModel mentor) {
        List<VotingResult> votings = new ArrayList<>();
        for (val strategy : strategies) {
            val result = strategy.calculateSympathy(mentee, mentor);
            votings.add(result);
        }

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

    Comparator<MentoringModel> bySeniortyASC = (mentor1, mentor2) -> {
        if (mentor1.getSeniority() < mentor2.getSeniority()) return 1;
        if (mentor1.getSeniority() == mentor2.getSeniority()) return 0;
        else {
            return -1;
        }
    };
}

abstract class SympathyResult {

    static final SympathyResult None = new SympathyResult() {
    };

    @Value
    public static class Some extends SympathyResult {
        private int value;
    }

}
