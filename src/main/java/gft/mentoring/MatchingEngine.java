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
            new PreferSameLocalizationDevMan(),
            new MentorHasToWorkAtLeastOneYearInGFT(),
            new PreferDevManFromSameJobFamily(),
            new PreferDevManFromDevGroupStrategy(),
            new PreferDevManFromCorporateServicesWithSameSpecializaton(),
    };

    Stream<MentoringModel> findProposals(MentoringModel mentee, MentoringModel... candidates) {
        return Arrays.stream(candidates)
                .map(it -> new MyTuple1(it, sympathy(mentee, it)))
                .filter(it -> it.sympathy != SympathyResult.None)
                .map(it -> new MyTuple2(it.mentor, ((SympathyResult.Some) it.sympathy).getValue()))
                .sorted((it1, it2) -> -(it1.sympathy - it2.sympathy))
                .map(it -> it.mentor);
    }

    @AllArgsConstructor
    static class MyTuple1 {
        private MentoringModel mentor;
        private SympathyResult sympathy;
    }

    @AllArgsConstructor
    static class MyTuple2 {
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

}

abstract class SympathyResult {

    static final SympathyResult None = new SympathyResult() {
    };

    @Value
    public static class Some extends SympathyResult {
        private int value;
    }

}
