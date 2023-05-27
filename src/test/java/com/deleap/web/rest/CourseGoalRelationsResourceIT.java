package com.deleap.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.deleap.IntegrationTest;
import com.deleap.domain.CourseGoalRelations;
import com.deleap.repository.CourseGoalRelationsRepository;
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
 * Integration tests for the {@link CourseGoalRelationsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CourseGoalRelationsResourceIT {

    private static final String DEFAULT_GOAL_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_GOAL_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/course-goal-relations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CourseGoalRelationsRepository courseGoalRelationsRepository;

    @Mock
    private CourseGoalRelationsRepository courseGoalRelationsRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourseGoalRelationsMockMvc;

    private CourseGoalRelations courseGoalRelations;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseGoalRelations createEntity(EntityManager em) {
        CourseGoalRelations courseGoalRelations = new CourseGoalRelations().goalValue(DEFAULT_GOAL_VALUE);
        return courseGoalRelations;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseGoalRelations createUpdatedEntity(EntityManager em) {
        CourseGoalRelations courseGoalRelations = new CourseGoalRelations().goalValue(UPDATED_GOAL_VALUE);
        return courseGoalRelations;
    }

    @BeforeEach
    public void initTest() {
        courseGoalRelations = createEntity(em);
    }

    @Test
    @Transactional
    void createCourseGoalRelations() throws Exception {
        int databaseSizeBeforeCreate = courseGoalRelationsRepository.findAll().size();
        // Create the CourseGoalRelations
        restCourseGoalRelationsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseGoalRelations))
            )
            .andExpect(status().isCreated());

        // Validate the CourseGoalRelations in the database
        List<CourseGoalRelations> courseGoalRelationsList = courseGoalRelationsRepository.findAll();
        assertThat(courseGoalRelationsList).hasSize(databaseSizeBeforeCreate + 1);
        CourseGoalRelations testCourseGoalRelations = courseGoalRelationsList.get(courseGoalRelationsList.size() - 1);
        assertThat(testCourseGoalRelations.getGoalValue()).isEqualTo(DEFAULT_GOAL_VALUE);
    }

    @Test
    @Transactional
    void createCourseGoalRelationsWithExistingId() throws Exception {
        // Create the CourseGoalRelations with an existing ID
        courseGoalRelations.setId(1L);

        int databaseSizeBeforeCreate = courseGoalRelationsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseGoalRelationsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseGoalRelations))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseGoalRelations in the database
        List<CourseGoalRelations> courseGoalRelationsList = courseGoalRelationsRepository.findAll();
        assertThat(courseGoalRelationsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCourseGoalRelations() throws Exception {
        // Initialize the database
        courseGoalRelationsRepository.saveAndFlush(courseGoalRelations);

        // Get all the courseGoalRelationsList
        restCourseGoalRelationsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseGoalRelations.getId().intValue())))
            .andExpect(jsonPath("$.[*].goalValue").value(hasItem(DEFAULT_GOAL_VALUE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCourseGoalRelationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(courseGoalRelationsRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCourseGoalRelationsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(courseGoalRelationsRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCourseGoalRelationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(courseGoalRelationsRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCourseGoalRelationsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(courseGoalRelationsRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCourseGoalRelations() throws Exception {
        // Initialize the database
        courseGoalRelationsRepository.saveAndFlush(courseGoalRelations);

        // Get the courseGoalRelations
        restCourseGoalRelationsMockMvc
            .perform(get(ENTITY_API_URL_ID, courseGoalRelations.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(courseGoalRelations.getId().intValue()))
            .andExpect(jsonPath("$.goalValue").value(DEFAULT_GOAL_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingCourseGoalRelations() throws Exception {
        // Get the courseGoalRelations
        restCourseGoalRelationsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCourseGoalRelations() throws Exception {
        // Initialize the database
        courseGoalRelationsRepository.saveAndFlush(courseGoalRelations);

        int databaseSizeBeforeUpdate = courseGoalRelationsRepository.findAll().size();

        // Update the courseGoalRelations
        CourseGoalRelations updatedCourseGoalRelations = courseGoalRelationsRepository.findById(courseGoalRelations.getId()).get();
        // Disconnect from session so that the updates on updatedCourseGoalRelations are not directly saved in db
        em.detach(updatedCourseGoalRelations);
        updatedCourseGoalRelations.goalValue(UPDATED_GOAL_VALUE);

        restCourseGoalRelationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCourseGoalRelations.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCourseGoalRelations))
            )
            .andExpect(status().isOk());

        // Validate the CourseGoalRelations in the database
        List<CourseGoalRelations> courseGoalRelationsList = courseGoalRelationsRepository.findAll();
        assertThat(courseGoalRelationsList).hasSize(databaseSizeBeforeUpdate);
        CourseGoalRelations testCourseGoalRelations = courseGoalRelationsList.get(courseGoalRelationsList.size() - 1);
        assertThat(testCourseGoalRelations.getGoalValue()).isEqualTo(UPDATED_GOAL_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingCourseGoalRelations() throws Exception {
        int databaseSizeBeforeUpdate = courseGoalRelationsRepository.findAll().size();
        courseGoalRelations.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseGoalRelationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courseGoalRelations.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseGoalRelations))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseGoalRelations in the database
        List<CourseGoalRelations> courseGoalRelationsList = courseGoalRelationsRepository.findAll();
        assertThat(courseGoalRelationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCourseGoalRelations() throws Exception {
        int databaseSizeBeforeUpdate = courseGoalRelationsRepository.findAll().size();
        courseGoalRelations.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseGoalRelationsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseGoalRelations))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseGoalRelations in the database
        List<CourseGoalRelations> courseGoalRelationsList = courseGoalRelationsRepository.findAll();
        assertThat(courseGoalRelationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCourseGoalRelations() throws Exception {
        int databaseSizeBeforeUpdate = courseGoalRelationsRepository.findAll().size();
        courseGoalRelations.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseGoalRelationsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseGoalRelations))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourseGoalRelations in the database
        List<CourseGoalRelations> courseGoalRelationsList = courseGoalRelationsRepository.findAll();
        assertThat(courseGoalRelationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCourseGoalRelationsWithPatch() throws Exception {
        // Initialize the database
        courseGoalRelationsRepository.saveAndFlush(courseGoalRelations);

        int databaseSizeBeforeUpdate = courseGoalRelationsRepository.findAll().size();

        // Update the courseGoalRelations using partial update
        CourseGoalRelations partialUpdatedCourseGoalRelations = new CourseGoalRelations();
        partialUpdatedCourseGoalRelations.setId(courseGoalRelations.getId());

        partialUpdatedCourseGoalRelations.goalValue(UPDATED_GOAL_VALUE);

        restCourseGoalRelationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseGoalRelations.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourseGoalRelations))
            )
            .andExpect(status().isOk());

        // Validate the CourseGoalRelations in the database
        List<CourseGoalRelations> courseGoalRelationsList = courseGoalRelationsRepository.findAll();
        assertThat(courseGoalRelationsList).hasSize(databaseSizeBeforeUpdate);
        CourseGoalRelations testCourseGoalRelations = courseGoalRelationsList.get(courseGoalRelationsList.size() - 1);
        assertThat(testCourseGoalRelations.getGoalValue()).isEqualTo(UPDATED_GOAL_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateCourseGoalRelationsWithPatch() throws Exception {
        // Initialize the database
        courseGoalRelationsRepository.saveAndFlush(courseGoalRelations);

        int databaseSizeBeforeUpdate = courseGoalRelationsRepository.findAll().size();

        // Update the courseGoalRelations using partial update
        CourseGoalRelations partialUpdatedCourseGoalRelations = new CourseGoalRelations();
        partialUpdatedCourseGoalRelations.setId(courseGoalRelations.getId());

        partialUpdatedCourseGoalRelations.goalValue(UPDATED_GOAL_VALUE);

        restCourseGoalRelationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseGoalRelations.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourseGoalRelations))
            )
            .andExpect(status().isOk());

        // Validate the CourseGoalRelations in the database
        List<CourseGoalRelations> courseGoalRelationsList = courseGoalRelationsRepository.findAll();
        assertThat(courseGoalRelationsList).hasSize(databaseSizeBeforeUpdate);
        CourseGoalRelations testCourseGoalRelations = courseGoalRelationsList.get(courseGoalRelationsList.size() - 1);
        assertThat(testCourseGoalRelations.getGoalValue()).isEqualTo(UPDATED_GOAL_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingCourseGoalRelations() throws Exception {
        int databaseSizeBeforeUpdate = courseGoalRelationsRepository.findAll().size();
        courseGoalRelations.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseGoalRelationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, courseGoalRelations.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseGoalRelations))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseGoalRelations in the database
        List<CourseGoalRelations> courseGoalRelationsList = courseGoalRelationsRepository.findAll();
        assertThat(courseGoalRelationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCourseGoalRelations() throws Exception {
        int databaseSizeBeforeUpdate = courseGoalRelationsRepository.findAll().size();
        courseGoalRelations.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseGoalRelationsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseGoalRelations))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseGoalRelations in the database
        List<CourseGoalRelations> courseGoalRelationsList = courseGoalRelationsRepository.findAll();
        assertThat(courseGoalRelationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCourseGoalRelations() throws Exception {
        int databaseSizeBeforeUpdate = courseGoalRelationsRepository.findAll().size();
        courseGoalRelations.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseGoalRelationsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseGoalRelations))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourseGoalRelations in the database
        List<CourseGoalRelations> courseGoalRelationsList = courseGoalRelationsRepository.findAll();
        assertThat(courseGoalRelationsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCourseGoalRelations() throws Exception {
        // Initialize the database
        courseGoalRelationsRepository.saveAndFlush(courseGoalRelations);

        int databaseSizeBeforeDelete = courseGoalRelationsRepository.findAll().size();

        // Delete the courseGoalRelations
        restCourseGoalRelationsMockMvc
            .perform(delete(ENTITY_API_URL_ID, courseGoalRelations.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CourseGoalRelations> courseGoalRelationsList = courseGoalRelationsRepository.findAll();
        assertThat(courseGoalRelationsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
