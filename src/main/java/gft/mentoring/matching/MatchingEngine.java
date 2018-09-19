package gft.mentoring.matching;
/* This class implements logic to meet business requirements described in requirements.md*/

import gft.mentoring.matching.model.MentoringModel;
import gft.mentoring.matching.voting.result.Rejected;
import gft.mentoring.matching.voting.result.Support;
import gft.mentoring.matching.voting.result.VotingResult;
import gft.mentoring.matching.voting.strategy.PreferDevManFromCorporateServicesWithSameSpecializaton;
import gft.mentoring.matching.voting.strategy.PreferDevManFromDevGroupStrategy;
import gft.mentoring.matching.voting.strategy.PreferDevManFromSameJobFamily;
import gft.mentoring.matching.voting.strategy.VotingStrategy;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.val;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

class MatchingEngine {

    static VotingStrategy[] strategies = {
            new PreferDevManFromDevGroupStrategy(),
            new PreferDevManFromSameJobFamily(),
            new PreferDevManFromCorporateServicesWithSameSpecializaton()
    };

    Stream<MentoringModel> findProposalsStream(MentoringModel mentee, MentoringModel... candidates) {
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
//        konwersja
        List<VotingResult> votings = new ArrayList<>();
        for (val strategy : strategies) {
            val result = strategy.calculateSympathy(mentee, mentor);
            votings.add(result);
        }

        int sympathy = 0;
        for (VotingResult vote : votings) {
            if (vote instanceof Support) {
                sympathy += ((Support) vote).getSympathy();
            }
            if (vote instanceof Rejected) {
                return SympathyResult.None;
            }
        }

        if (sympathy == 0) return SympathyResult.None;
        else
            return new SympathyResult.Some(sympathy);
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
