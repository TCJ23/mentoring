package gft.mentoring.matching.model;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenteeTest {

    @Test
    void checkPOJO() {
        Mentee mentee = new Mentee(1, Family.DATA);
        assertAll(() -> assertThat(mentee.getClass()).isEqualTo(Mentee.class),
                () -> assertThat(mentee.getId()).isNotNull());
//        () -> assertThat(mentee.getFamily().isDevelopmentGroup()).isEqualTo(true);

    }
}