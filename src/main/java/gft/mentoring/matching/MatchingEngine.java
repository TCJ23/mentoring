package gft.mentoring.matching;
/* This class implements logic to meet business requirements described in requirements.md*/

import gft.mentoring.matching.model.MentoringModel;
import gft.mentoring.matching.voting.result.Support;
import gft.mentoring.matching.voting.strategy.PreferDevManFromDevGroupStrategy;
import gft.mentoring.matching.voting.strategy.PreferDevManFromSameJobFamily;
import gft.mentoring.matching.voting.strategy.VotingStrategy;
import lombok.AllArgsConstructor;
import lombok.val;

import java.util.Arrays;
import java.util.stream.Stream;

class MatchingEngine {

    static VotingStrategy[] strategies = {
            new PreferDevManFromDevGroupStrategy(),
            new PreferDevManFromSameJobFamily(),
//            new PreferDevManFromCorporateServicesWithSameSpecializaton()
    };

    Stream<MentoringModel> findProposalsStream(MentoringModel mentee, MentoringModel... candidates) {
        return Arrays.stream(candidates)
                .map(it -> new MyTuple(it, sympathy(mentee, it)))
                .filter(it -> it.sympathy > 0)
                .sorted((v1, v2) -> -(v1.sympathy - v2.sympathy))
                .map(it -> it.mentor);
    }

    @AllArgsConstructor
    static class MyTuple {
        private MentoringModel mentor;
        private int sympathy;
    }

    static int sympathy(MentoringModel mentee, MentoringModel mentor) {
        int sympathy = -1;
        for (val strategy : strategies) {
            val result = strategy.calculateSympathy(mentee, mentor);
            if (result instanceof Support) {
                sympathy += ((Support) result).getSympathy();
            }
        }
        return sympathy;
    }
}