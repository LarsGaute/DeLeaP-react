package com.deleap.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.deleap.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GoalTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Goal.class);
        Goal goal1 = new Goal();
        goal1.setId(1L);
        Goal goal2 = new Goal();
        goal2.setId(goal1.getId());
        assertThat(goal1).isEqualTo(goal2);
        goal2.setId(2L);
        assertThat(goal1).isNotEqualTo(goal2);
        goal1.setId(null);
        assertThat(goal1).isNotEqualTo(goal2);
    }
}
