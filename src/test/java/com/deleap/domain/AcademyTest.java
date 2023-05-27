package com.deleap.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.deleap.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AcademyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Academy.class);
        Academy academy1 = new Academy();
        academy1.setId(1L);
        Academy academy2 = new Academy();
        academy2.setId(academy1.getId());
        assertThat(academy1).isEqualTo(academy2);
        academy2.setId(2L);
        assertThat(academy1).isNotEqualTo(academy2);
        academy1.setId(null);
        assertThat(academy1).isNotEqualTo(academy2);
    }
}
