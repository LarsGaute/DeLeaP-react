package com.deleap.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.deleap.IntegrationTest;
import com.deleap.domain.Curriculum;
import com.deleap.repository.CurriculumRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CurriculumResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CurriculumResourceIT {

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/curricula";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CurriculumRepository curriculumRepository;

    @Mock
    private CurriculumRepository curriculumRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCurriculumMockMvc;

    private Curriculum curriculum;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Curriculum createEntity(EntityManager em) {
        Curriculum curriculum = new Curriculum().text(DEFAULT_TEXT).url(DEFAULT_URL);
        return curriculum;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Curriculum createUpdatedEntity(EntityManager em) {
        Curriculum curriculum = new Curriculum().text(UPDATED_TEXT).url(UPDATED_URL);
        return curriculum;
    }

    @BeforeEach
    public void initTest() {
        curriculum = createEntity(em);
    }

    @Test
    @Transactional
    void createCurriculum() throws Exception {
        int databaseSizeBeforeCreate = curriculumRepository.findAll().size();
        // Create the Curriculum
        restCurriculumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(curriculum)))
            .andExpect(status().isCreated());

        // Validate the Curriculum in the database
        List<Curriculum> curriculumList = curriculumRepository.findAll();
        assertThat(curriculumList).hasSize(databaseSizeBeforeCreate + 1);
        Curriculum testCurriculum = curriculumList.get(curriculumList.size() - 1);
        assertThat(testCurriculum.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testCurriculum.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    void createCurriculumWithExistingId() throws Exception {
        // Create the Curriculum with an existing ID
        curriculum.setId(1L);

        int databaseSizeBeforeCreate = curriculumRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCurriculumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(curriculum)))
            .andExpect(status().isBadRequest());

        // Validate the Curriculum in the database
        List<Curriculum> curriculumList = curriculumRepository.findAll();
        assertThat(curriculumList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCurricula() throws Exception {
        // Initialize the database
        curriculumRepository.saveAndFlush(curriculum);

        // Get all the curriculumList
        restCurriculumMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(curriculum.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCurriculaWithEagerRelationshipsIsEnabled() throws Exception {
        when(curriculumRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCurriculumMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(curriculumRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCurriculaWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(curriculumRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCurriculumMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(curriculumRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCurriculum() throws Exception {
        // Initialize the database
        curriculumRepository.saveAndFlush(curriculum);

        // Get the curriculum
        restCurriculumMockMvc
            .perform(get(ENTITY_API_URL_ID, curriculum.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(curriculum.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL));
    }

    @Test
    @Transactional
    void getNonExistingCurriculum() throws Exception {
        // Get the curriculum
        restCurriculumMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCurriculum() throws Exception {
        // Initialize the database
        curriculumRepository.saveAndFlush(curriculum);

        int databaseSizeBeforeUpdate = curriculumRepository.findAll().size();

        // Update the curriculum
        Curriculum updatedCurriculum = curriculumRepository.findById(curriculum.getId()).get();
        // Disconnect from session so that the updates on updatedCurriculum are not directly saved in db
        em.detach(updatedCurriculum);
        updatedCurriculum.text(UPDATED_TEXT).url(UPDATED_URL);

        restCurriculumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCurriculum.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCurriculum))
            )
            .andExpect(status().isOk());

        // Validate the Curriculum in the database
        List<Curriculum> curriculumList = curriculumRepository.findAll();
        assertThat(curriculumList).hasSize(databaseSizeBeforeUpdate);
        Curriculum testCurriculum = curriculumList.get(curriculumList.size() - 1);
        assertThat(testCurriculum.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testCurriculum.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void putNonExistingCurriculum() throws Exception {
        int databaseSizeBeforeUpdate = curriculumRepository.findAll().size();
        curriculum.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCurriculumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, curriculum.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(curriculum))
            )
            .andExpect(status().isBadRequest());

        // Validate the Curriculum in the database
        List<Curriculum> curriculumList = curriculumRepository.findAll();
        assertThat(curriculumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCurriculum() throws Exception {
        int databaseSizeBeforeUpdate = curriculumRepository.findAll().size();
        curriculum.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurriculumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(curriculum))
            )
            .andExpect(status().isBadRequest());

        // Validate the Curriculum in the database
        List<Curriculum> curriculumList = curriculumRepository.findAll();
        assertThat(curriculumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCurriculum() throws Exception {
        int databaseSizeBeforeUpdate = curriculumRepository.findAll().size();
        curriculum.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurriculumMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(curriculum)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Curriculum in the database
        List<Curriculum> curriculumList = curriculumRepository.findAll();
        assertThat(curriculumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCurriculumWithPatch() throws Exception {
        // Initialize the database
        curriculumRepository.saveAndFlush(curriculum);

        int databaseSizeBeforeUpdate = curriculumRepository.findAll().size();

        // Update the curriculum using partial update
        Curriculum partialUpdatedCurriculum = new Curriculum();
        partialUpdatedCurriculum.setId(curriculum.getId());

        partialUpdatedCurriculum.text(UPDATED_TEXT);

        restCurriculumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCurriculum.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCurriculum))
            )
            .andExpect(status().isOk());

        // Validate the Curriculum in the database
        List<Curriculum> curriculumList = curriculumRepository.findAll();
        assertThat(curriculumList).hasSize(databaseSizeBeforeUpdate);
        Curriculum testCurriculum = curriculumList.get(curriculumList.size() - 1);
        assertThat(testCurriculum.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testCurriculum.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    void fullUpdateCurriculumWithPatch() throws Exception {
        // Initialize the database
        curriculumRepository.saveAndFlush(curriculum);

        int databaseSizeBeforeUpdate = curriculumRepository.findAll().size();

        // Update the curriculum using partial update
        Curriculum partialUpdatedCurriculum = new Curriculum();
        partialUpdatedCurriculum.setId(curriculum.getId());

        partialUpdatedCurriculum.text(UPDATED_TEXT).url(UPDATED_URL);

        restCurriculumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCurriculum.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCurriculum))
            )
            .andExpect(status().isOk());

        // Validate the Curriculum in the database
        List<Curriculum> curriculumList = curriculumRepository.findAll();
        assertThat(curriculumList).hasSize(databaseSizeBeforeUpdate);
        Curriculum testCurriculum = curriculumList.get(curriculumList.size() - 1);
        assertThat(testCurriculum.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testCurriculum.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void patchNonExistingCurriculum() throws Exception {
        int databaseSizeBeforeUpdate = curriculumRepository.findAll().size();
        curriculum.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCurriculumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, curriculum.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(curriculum))
            )
            .andExpect(status().isBadRequest());

        // Validate the Curriculum in the database
        List<Curriculum> curriculumList = curriculumRepository.findAll();
        assertThat(curriculumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCurriculum() throws Exception {
        int databaseSizeBeforeUpdate = curriculumRepository.findAll().size();
        curriculum.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurriculumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(curriculum))
            )
            .andExpect(status().isBadRequest());

        // Validate the Curriculum in the database
        List<Curriculum> curriculumList = curriculumRepository.findAll();
        assertThat(curriculumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCurriculum() throws Exception {
        int databaseSizeBeforeUpdate = curriculumRepository.findAll().size();
        curriculum.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCurriculumMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(curriculum))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Curriculum in the database
        List<Curriculum> curriculumList = curriculumRepository.findAll();
        assertThat(curriculumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCurriculum() throws Exception {
        // Initialize the database
        curriculumRepository.saveAndFlush(curriculum);

        int databaseSizeBeforeDelete = curriculumRepository.findAll().size();

        // Delete the curriculum
        restCurriculumMockMvc
            .perform(delete(ENTITY_API_URL_ID, curriculum.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Curriculum> curriculumList = curriculumRepository.findAll();
        assertThat(curriculumList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
