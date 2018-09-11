package gft.mentoring.matching;

import gft.mentoring.matching.model.Family;
import gft.mentoring.matching.model.Mentee;
import gft.mentoring.matching.model.Mentor;
import lombok.Value;
import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;


public class TestMatching {

    @ParameterizedTest
    @MethodSource
    void findSingleCandidateMentorFromProposals(SingleMatchingParam singleMatchingParam) {
        val candidate = new Mentor(1, singleMatchingParam.mentorCandidateFamily);
        val mentee = new Mentee(1,  singleMatchingParam.menteeFamily);
        val proposals = new MatchingEngine().findProposals(mentee, candidate);
        Assertions.assertThat(proposals.size() == 1).isEqualTo(singleMatchingParam.accepted);
    }

    static Stream<SingleMatchingParam> findSingleCandidateMentorFromProposals() {
        return Stream.of(
                new SingleMatchingParam(Family.PROJECT_DEVELOPMENT, Family.PROJECT_DEVELOPMENT, true),
                new SingleMatchingParam(Family.ARCHITECTURE, Family.CORPORATE_SERVICES, false));
    }

    @Value
    static class SingleMatchingParam {
        private Family menteeFamily;
        private Family mentorCandidateFamily;
        private boolean accepted;
    }
}
