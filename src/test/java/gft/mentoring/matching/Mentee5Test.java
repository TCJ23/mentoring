package gft.mentoring.matching;

import lombok.Value;
import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class Mentee5Test {


    @ParameterizedTest
    @MethodSource
    void findMentorProposals(SingleMatchingParam singleMatchingParam) {
        val candidate = new Mentor(1, 4, singleMatchingParam.mentorCandidateFamily);
        val mentee = new Mentee(1, 2, singleMatchingParam.menteeFamily);
        val proposals = mentee.findProposals(candidate); // TAKE TO ENGINE
        Assertions.assertThat(proposals.size() == 1).isEqualTo(singleMatchingParam.accepted);
    }

    static Stream<SingleMatchingParam> findMentorProposals() {
        return Stream.of(
                new SingleMatchingParam(Family.DEVELOPMENT, Family.DEVELOPMENT, true),
                new SingleMatchingParam(Family.ARCHITECTURE, Family.OTHER, false));
    }

    @Value
    static class SingleMatchingParam {
        private Family menteeFamily;
        private Family mentorCandidateFamily;
        private boolean accepted;
    }
}
