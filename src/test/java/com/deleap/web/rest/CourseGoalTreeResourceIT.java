package com.deleap.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.deleap.IntegrationTest;
import com.deleap.domain.CourseGoalTree;
import com.deleap.repository.CourseGoalTreeRepository;
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
 * Integration tests for the {@link CourseGoalTreeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CourseGoalTreeResourceIT {

    private static final String DEFAULT_GOAL_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_GOAL_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/course-goal-trees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CourseGoalTreeRepository courseGoalTreeRepository;

    @Mock
    private CourseGoalTreeRepository courseGoalTreeRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourseGoalTreeMockMvc;

    private CourseGoalTree courseGoalTree;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseGoalTree createEntity(EntityManager em) {
        CourseGoalTree courseGoalTree = new CourseGoalTree().goalValue(DEFAULT_GOAL_VALUE);
        return courseGoalTree;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseGoalTree createUpdatedEntity(EntityManager em) {
        CourseGoalTree courseGoalTree = new CourseGoalTree().goalValue(UPDATED_GOAL_VALUE);
        return courseGoalTree;
    }

    @BeforeEach
    public void initTest() {
        courseGoalTree = createEntity(em);
    }

    @Test
    @Transactional
    void createCourseGoalTree() throws Exception {
        int databaseSizeBeforeCreate = courseGoalTreeRepository.findAll().size();
        // Create the CourseGoalTree
        restCourseGoalTreeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseGoalTree))
            )
            .andExpect(status().isCreated());

        // Validate the CourseGoalTree in the database
        List<CourseGoalTree> courseGoalTreeList = courseGoalTreeRepository.findAll();
        assertThat(courseGoalTreeList).hasSize(databaseSizeBeforeCreate + 1);
        CourseGoalTree testCourseGoalTree = courseGoalTreeList.get(courseGoalTreeList.size() - 1);
        assertThat(testCourseGoalTree.getGoalValue()).isEqualTo(DEFAULT_GOAL_VALUE);
    }

    @Test
    @Transactional
    void createCourseGoalTreeWithExistingId() throws Exception {
        // Create the CourseGoalTree with an existing ID
        courseGoalTree.setId(1L);

        int databaseSizeBeforeCreate = courseGoalTreeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseGoalTreeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseGoalTree))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseGoalTree in the database
        List<CourseGoalTree> courseGoalTreeList = courseGoalTreeRepository.findAll();
        assertThat(courseGoalTreeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCourseGoalTrees() throws Exception {
        // Initialize the database
        courseGoalTreeRepository.saveAndFlush(courseGoalTree);

        // Get all the courseGoalTreeList
        restCourseGoalTreeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseGoalTree.getId().intValue())))
            .andExpect(jsonPath("$.[*].goalValue").value(hasItem(DEFAULT_GOAL_VALUE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCourseGoalTreesWithEagerRelationshipsIsEnabled() throws Exception {
        when(courseGoalTreeRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCourseGoalTreeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(courseGoalTreeRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCourseGoalTreesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(courseGoalTreeRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCourseGoalTreeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(courseGoalTreeRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCourseGoalTree() throws Exception {
        // Initialize the database
        courseGoalTreeRepository.saveAndFlush(courseGoalTree);

        // Get the courseGoalTree
        restCourseGoalTreeMockMvc
            .perform(get(ENTITY_API_URL_ID, courseGoalTree.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(courseGoalTree.getId().intValue()))
            .andExpect(jsonPath("$.goalValue").value(DEFAULT_GOAL_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingCourseGoalTree() throws Exception {
        // Get the courseGoalTree
        restCourseGoalTreeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCourseGoalTree() throws Exception {
        // Initialize the database
        courseGoalTreeRepository.saveAndFlush(courseGoalTree);

        int databaseSizeBeforeUpdate = courseGoalTreeRepository.findAll().size();

        // Update the courseGoalTree
        CourseGoalTree updatedCourseGoalTree = courseGoalTreeRepository.findById(courseGoalTree.getId()).get();
        // Disconnect from session so that the updates on updatedCourseGoalTree are not directly saved in db
        em.detach(updatedCourseGoalTree);
        updatedCourseGoalTree.goalValue(UPDATED_GOAL_VALUE);

        restCourseGoalTreeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCourseGoalTree.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCourseGoalTree))
            )
            .andExpect(status().isOk());

        // Validate the CourseGoalTree in the database
        List<CourseGoalTree> courseGoalTreeList = courseGoalTreeRepository.findAll();
        assertThat(courseGoalTreeList).hasSize(databaseSizeBeforeUpdate);
        CourseGoalTree testCourseGoalTree = courseGoalTreeList.get(courseGoalTreeList.size() - 1);
        assertThat(testCourseGoalTree.getGoalValue()).isEqualTo(UPDATED_GOAL_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingCourseGoalTree() throws Exception {
        int databaseSizeBeforeUpdate = courseGoalTreeRepository.findAll().size();
        courseGoalTree.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseGoalTreeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courseGoalTree.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseGoalTree))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseGoalTree in the database
        List<CourseGoalTree> courseGoalTreeList = courseGoalTreeRepository.findAll();
        assertThat(courseGoalTreeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCourseGoalTree() throws Exception {
        int databaseSizeBeforeUpdate = courseGoalTreeRepository.findAll().size();
        courseGoalTree.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseGoalTreeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courseGoalTree))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseGoalTree in the database
        List<CourseGoalTree> courseGoalTreeList = courseGoalTreeRepository.findAll();
        assertThat(courseGoalTreeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCourseGoalTree() throws Exception {
        int databaseSizeBeforeUpdate = courseGoalTreeRepository.findAll().size();
        courseGoalTree.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseGoalTreeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courseGoalTree)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourseGoalTree in the database
        List<CourseGoalTree> courseGoalTreeList = courseGoalTreeRepository.findAll();
        assertThat(courseGoalTreeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCourseGoalTreeWithPatch() throws Exception {
        // Initialize the database
        courseGoalTreeRepository.saveAndFlush(courseGoalTree);

        int databaseSizeBeforeUpdate = courseGoalTreeRepository.findAll().size();

        // Update the courseGoalTree using partial update
        CourseGoalTree partialUpdatedCourseGoalTree = new CourseGoalTree();
        partialUpdatedCourseGoalTree.setId(courseGoalTree.getId());

        restCourseGoalTreeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseGoalTree.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourseGoalTree))
            )
            .andExpect(status().isOk());

        // Validate the CourseGoalTree in the database
        List<CourseGoalTree> courseGoalTreeList = courseGoalTreeRepository.findAll();
        assertThat(courseGoalTreeList).hasSize(databaseSizeBeforeUpdate);
        CourseGoalTree testCourseGoalTree = courseGoalTreeList.get(courseGoalTreeList.size() - 1);
        assertThat(testCourseGoalTree.getGoalValue()).isEqualTo(DEFAULT_GOAL_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateCourseGoalTreeWithPatch() throws Exception {
        // Initialize the database
        courseGoalTreeRepository.saveAndFlush(courseGoalTree);

        int databaseSizeBeforeUpdate = courseGoalTreeRepository.findAll().size();

        // Update the courseGoalTree using partial update
        CourseGoalTree partialUpdatedCourseGoalTree = new CourseGoalTree();
        partialUpdatedCourseGoalTree.setId(courseGoalTree.getId());

        partialUpdatedCourseGoalTree.goalValue(UPDATED_GOAL_VALUE);

        restCourseGoalTreeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseGoalTree.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourseGoalTree))
            )
            .andExpect(status().isOk());

        // Validate the CourseGoalTree in the database
        List<CourseGoalTree> courseGoalTreeList = courseGoalTreeRepository.findAll();
        assertThat(courseGoalTreeList).hasSize(databaseSizeBeforeUpdate);
        CourseGoalTree testCourseGoalTree = courseGoalTreeList.get(courseGoalTreeList.size() - 1);
        assertThat(testCourseGoalTree.getGoalValue()).isEqualTo(UPDATED_GOAL_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingCourseGoalTree() throws Exception {
        int databaseSizeBeforeUpdate = courseGoalTreeRepository.findAll().size();
        courseGoalTree.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseGoalTreeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, courseGoalTree.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseGoalTree))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseGoalTree in the database
        List<CourseGoalTree> courseGoalTreeList = courseGoalTreeRepository.findAll();
        assertThat(courseGoalTreeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCourseGoalTree() throws Exception {
        int databaseSizeBeforeUpdate = courseGoalTreeRepository.findAll().size();
        courseGoalTree.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseGoalTreeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courseGoalTree))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseGoalTree in the database
        List<CourseGoalTree> courseGoalTreeList = courseGoalTreeRepository.findAll();
        assertThat(courseGoalTreeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCourseGoalTree() throws Exception {
        int databaseSizeBeforeUpdate = courseGoalTreeRepository.findAll().size();
        courseGoalTree.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseGoalTreeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(courseGoalTree))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourseGoalTree in the database
        List<CourseGoalTree> courseGoalTreeList = courseGoalTreeRepository.findAll();
        assertThat(courseGoalTreeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCourseGoalTree() throws Exception {
        // Initialize the database
        courseGoalTreeRepository.saveAndFlush(courseGoalTree);

        int databaseSizeBeforeDelete = courseGoalTreeRepository.findAll().size();

        // Delete the courseGoalTree
        restCourseGoalTreeMockMvc
            .perform(delete(ENTITY_API_URL_ID, courseGoalTree.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CourseGoalTree> courseGoalTreeList = courseGoalTreeRepository.findAll();
        assertThat(courseGoalTreeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
