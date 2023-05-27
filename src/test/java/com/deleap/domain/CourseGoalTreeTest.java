package com.deleap.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.deleap.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CourseGoalTreeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseGoalTree.class);
        CourseGoalTree courseGoalTree1 = new CourseGoalTree();
        courseGoalTree1.setId(1L);
        CourseGoalTree courseGoalTree2 = new CourseGoalTree();
        courseGoalTree2.setId(courseGoalTree1.getId());
        assertThat(courseGoalTree1).isEqualTo(courseGoalTree2);
        courseGoalTree2.setId(2L);
        assertThat(courseGoalTree1).isNotEqualTo(courseGoalTree2);
        courseGoalTree1.setId(null);
        assertThat(courseGoalTree1).isNotEqualTo(courseGoalTree2);
    }
}
