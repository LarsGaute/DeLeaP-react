package com.deleap.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.deleap.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CourseGoalRelationsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseGoalRelations.class);
        CourseGoalRelations courseGoalRelations1 = new CourseGoalRelations();
        courseGoalRelations1.setId(1L);
        CourseGoalRelations courseGoalRelations2 = new CourseGoalRelations();
        courseGoalRelations2.setId(courseGoalRelations1.getId());
        assertThat(courseGoalRelations1).isEqualTo(courseGoalRelations2);
        courseGoalRelations2.setId(2L);
        assertThat(courseGoalRelations1).isNotEqualTo(courseGoalRelations2);
        courseGoalRelations1.setId(null);
        assertThat(courseGoalRelations1).isNotEqualTo(courseGoalRelations2);
    }
}
