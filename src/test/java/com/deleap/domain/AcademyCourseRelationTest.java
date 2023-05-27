package com.deleap.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.deleap.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AcademyCourseRelationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AcademyCourseRelation.class);
        AcademyCourseRelation academyCourseRelation1 = new AcademyCourseRelation();
        academyCourseRelation1.setId(1L);
        AcademyCourseRelation academyCourseRelation2 = new AcademyCourseRelation();
        academyCourseRelation2.setId(academyCourseRelation1.getId());
        assertThat(academyCourseRelation1).isEqualTo(academyCourseRelation2);
        academyCourseRelation2.setId(2L);
        assertThat(academyCourseRelation1).isNotEqualTo(academyCourseRelation2);
        academyCourseRelation1.setId(null);
        assertThat(academyCourseRelation1).isNotEqualTo(academyCourseRelation2);
    }
}
